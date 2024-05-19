package com.gaminggrunts;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.gaminggrunts.RollerDieListener;
import com.gaminggrunts.RollerDieLexer;
import com.gaminggrunts.RollerDieParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ParserUtil {

    private static final Logger logger = LoggerFactory.getLogger(ParserUtil.class);

    public static Roll runParser(final String rollSpec) {

        Roll roll = new Roll();
        RollerDieLexer lexer = null;
        lexer = new RollerDieLexer(new ANTLRInputStream(rollSpec));
        // Get a list of matched tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // Pass the tokens to the parser
        RollerDieParser parser = new RollerDieParser(tokens);
        // Specify our entry point
        RollerDieParser.CommandContext commandContext = parser.command();
        // Walk it and attach our listener
        ParseTreeWalker walker = new ParseTreeWalker();
        DiceListener listener = new DiceListener(parser, roll);
        // Walk it
        walker.walk(listener, commandContext);

        roll.calculate(); // Roll the dice and generate the result
        return roll;
    }

}
