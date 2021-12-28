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
        this.setResult(0);
        Integer n = 0;
        do {
            n = DieUtil.rollDie(this.getSides());
            this.setResult(this.getResult() + n);
            rolls.add(Integer.toString(n));
        } while (n == this.getSides());
        this.setRoll("(" + String.join(" ", rolls) + ")");
    }

    @Override
    public String toString() {
        return this.getRoll();
    }

}
