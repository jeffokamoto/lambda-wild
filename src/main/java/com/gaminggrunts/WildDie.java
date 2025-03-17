package com.gaminggrunts;

import java.util.ArrayList;
import java.util.List;

public class WildDie extends Die {
    public WildDie(final Integer sides) {
        super(sides);
    }

    @Override
    public void roll() {
        List<String> rolls = new ArrayList<>();
        setResult(0);
        Integer n = 0;
        do {
            n = DieUtil.rollDie(getSides());
            setResult(getResult() + n);
            rolls.add(Integer.toString(n));
        } while (n == getSides());
        setRoll("(" + String.join(" ", rolls) + ")");
    }

    @Override
    public String toString() {
        return getRoll();
    }

}
