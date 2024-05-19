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
    Roll roll = ParserUtil.runParser(spec);

    String rv = "Die Roll for " +
            spec +
            ": " +
            roll.getRollResult() +
            "\nTotal is " +
            roll.getResult() +
            "\n";

    return rv;
  }
}