package com.gaminggrunts;

import org.antlr.v4.runtime.TokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.IntStream;

public class DiceListener extends RollerDieBaseListener {

    private final RollerDieParser parser;
    private Roll roll;

    private static final Logger logger = LoggerFactory.getLogger(DiceListener.class);

    public DiceListener(final RollerDieParser parser, Roll roll) {
        this.parser = parser;
        this.roll = roll;
    }

    @Override
    public void exitDiceCommand(final RollerDieParser.DiceCommandContext ctx) {
        RollerDieParser.NoModifierTermContext nModCtx;
        TokenStream tokens = parser.getTokenStream();

        // Handle first term (has no modifier)
        nModCtx = ctx.noModifierTerm();
        handleTerm(roll, nModCtx, 1);

        for (RollerDieParser.ModifierTermContext mtc: ctx.dieList) {
            int modifier = 0;
            RollerDieParser.ModContext modCtx = mtc.mod();
            String modString = modCtx.getText();
            if ("+".equals(modString)) {
                modifier = 1;
            } else if ("-".equals(modString)) {
                modifier = -1;
            }
            nModCtx = mtc.noModifierTerm();
            handleTerm(roll, nModCtx, modifier);
        }
    }

    public void handleTerm(Roll roll,
                           final RollerDieParser.NoModifierTermContext termCtx,
                           final int modifier) {
        int numDice = 0;
        Integer dieSize = 0;
        IntStream range = null;

        RollerDieParser.DieSpecContext dieSpecContext = termCtx.dieSpec();

        // Do we have a dice spec (e.g., 3d6)?
        if (null != dieSpecContext) {
            List<RollerDieParser.NumContext> numContextList = dieSpecContext.num();
            switch (numContextList.size()) {
                case 1:
                    numDice = 1;
                    dieSize = Integer.parseInt(numContextList.get(0).getText());
                    break;
                case 2:
                    numDice = Integer.parseInt(numContextList.get(0).getText());
                    dieSize = Integer.parseInt(numContextList.get(1).getText());
                    break;
                default:
                    // Let it fall through
            }
            if (numDice == 0) {
                // Error
                return;
            }
            range = IntStream.range(0, numDice); // Upper bound is exclusive, so start at 0

            RollerDieParser.DieTypeContext typeCtx = dieSpecContext.dieType();
            RollerDieParser.DieContext dieContext = typeCtx.die();
            RollerDieParser.WildContext wildContext = typeCtx.wild();

            Group group = new Group(modifier);
            Integer finalDieSize = dieSize;
            if (null != dieContext) {
                range.forEach(i -> { group.addDie(new Die(finalDieSize)); });
            } else if (null != wildContext) {
                range.forEach(i -> { group.addDie(new WildDie(finalDieSize)); });
            } else {
                // Error
            }

            roll.add(group);

            // Or just a modifier (e.g., 1)?
        } else {
            RollerDieParser.NumContext numContext = termCtx.num();
            Group group = new Group(modifier);
            if (null != numContext) {
                Integer number = Integer.parseInt(numContext.getText());
                Number num = new Number(number);
                group.addDie(num);
                roll.add(group);
            } else {
                // Shouldn't get here
            }
        }
    }
}