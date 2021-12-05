package com.gaminggrunts;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gdata.util.common.net.UriParameterMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.lang.IllegalStateException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import org.apache.commons.codec.binary.Base64;

// Handler value: com.gaminggrunts.HandlerStream
public class HandlerStream implements RequestStreamHandler {

  private static final Random rng = new Random();

  private static final String NUM_DICE = "numDice";
  private static final String DICE_SIZE = "diceSize";
  private static final String MODIFIER = "modifier";
  private static final String TOTAL = "total";
  private static final String ROLL_STRING = "rollString";

  final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Override
  public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException
  {
    LambdaLogger logger = context.getLogger();
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("US-ASCII")));
    PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("US-ASCII"))));
    try
    {
      HashMap event = gson.fromJson(reader, HashMap.class);
      // logger.log("STREAM TYPE: " + inputStream.getClass().toString());
      // logger.log("EVENT TYPE: " + event.getClass().toString());

      String body = (String) event.get("body");
      // logger.log("Body is " + body);

      String decodedBody = new String(Base64.decodeBase64(body), "US-ASCII");
      // logger.log("Decoded body is " + decodedBody);

      UriParameterMap parameterMap = UriParameterMap.parse(decodedBody);
      // logger.log("Parameter map is " + parameterMap.toString());

      String text = parameterMap.getFirst("text");
      logger.log("Text from slack is " + text);

      String reply = handleText(logger, text);
      logger.log("Reply to slack is " + reply);

      String toSlack = "{\n"
                       + "    \"response_type\": \"in_channel\",\n"
                       + "    \"text\": \"" + reply + "\"\n"
                       + "}\n";

      writer.write(toSlack);
      if (writer.checkError())
      {
        logger.log("WARNING: Writer encountered an error.");
      }
    }
    catch (IllegalStateException | JsonSyntaxException exception)
    {
      logger.log(exception.toString());
    }
    finally
    {
      reader.close();
      writer.close();
    }
  }

  public String handleText(final LambdaLogger logger, final String spec) {
    Map<String, Integer> parsed = parseSpec(spec);
    if (null == parsed) {
      logger.log("Empty spec passed in, ignoring");
    }
    Map<String, String> rollResults = rollDiceWithModifier(parsed);
    if (null == rollResults) {
      logger.log("Error when rolling " + spec);
    }
    String rv = "Die Roll for " +
                       spec +
                       ": " +
                       rollResults.get(ROLL_STRING) +
                       "\nTotal is " +
                       rollResults.get(TOTAL) +
                       "\n";
    return rv;
  }
  public static Map<String, Integer> parseSpec(final String specArg) {
    String spec = specArg.replaceAll("\\s+", "");

    String pattern = "^(\\d*)[D|d](\\d+)([+|-]\\d+)?";
    Pattern r = Pattern.compile(pattern);

    Matcher m = r.matcher(spec);
    if (!m.find()) {
      return Collections.emptyMap();
    }

    int numDice;
    if (m.group(1).isEmpty()) {
      numDice = 1;
    } else {
      numDice = Integer.parseInt(m.group(1));
    }

    int diceSize;
    if (null == m.group(2) || m.group(2).isEmpty()) {
      diceSize = 1;   // Throw error
    } else {
      diceSize = Integer.parseInt(m.group(2));
    }

    int modifier;
    if (null == m.group(3) || m.group(3).isEmpty()) {
      modifier = 0;
    } else {
      modifier = Integer.parseInt(m.group(3));
    }

    Map<String, Integer> rv = new HashMap<>();
    rv.put(NUM_DICE, numDice);
    rv.put(DICE_SIZE, diceSize);
    rv.put(MODIFIER, modifier);

    return rv;
  }

  public static Map<String, String> rollDiceWithModifier(final Map<String, Integer> args) {
    int numDice = args.get(NUM_DICE);
    int diceSize = args.get(DICE_SIZE);
    int modifier = args.get(MODIFIER);
    List<Die> dieList = new ArrayList<>();
    WildDie wildDie = null;
    Map<String, String> rv = new HashMap<>();
    Integer total = 0;
    String rollString = "";
    List<String> rollStringList = new ArrayList<>();

    if (numDice > 1) {
      wildDie = new WildDie(diceSize);
      numDice--;
    }

    IntStream.range(0, numDice).forEach(i -> {
      dieList.add(new Die(diceSize));
    });


    dieList.stream().forEach(die -> { die.roll(); });
    for (Die die: dieList) {
      total += die.result;
      rollStringList.add(die.roll);
    }
    rollString = String.join(" ", rollStringList);

    if (wildDie != null) {
      wildDie.roll();
      total += wildDie.result;
      rollString += " Wild " + wildDie.roll;
    }

    if (modifier != 0) {
      total += modifier;
      rollString += ((modifier > 0) ? " + " : " - ") + Integer.toString(modifier);
    }

    rv.put(TOTAL, Integer.toString(total));
    rv.put(ROLL_STRING, rollString);
    return rv;
  }

  public static class Die {
    Integer sides;
    Integer result;
    List<String> rolls;
    String roll;

    public Die(final Integer sides) {
      this.sides = sides;
      this.rolls = new ArrayList<>();
    }

    public void roll() {
      Integer n = rng.nextInt(this.sides) + 1;
      this.result = n;
      this.roll = Integer.toString(n);
        this.rolls.add(Integer.toString(this.result));
      }
    }

    public static class WildDie extends Die {
      public WildDie(final Integer sides) {
                                              super(sides);
                                                           }

      @Override
      public void roll() {
        this.result = 0;
        Integer n = 0;
        do {
          n = rng.nextInt(this.sides) + 1;
          this.result += n;
          this.rolls.add(Integer.toString(n));
        } while (n == this.sides);
        this.roll = "(" + String.join(", ", this.rolls) + ")";
      }
    }

}