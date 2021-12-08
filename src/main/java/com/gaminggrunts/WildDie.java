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
        this.result = 0;
        Integer n = 0;
        do {
            n = DieUtil.rollDie(this.sides);
            this.result += n;
            rolls.add(Integer.toString(n));
        } while (n == this.sides);
        this.roll = "(" + String.join(" ", rolls) + ")";
    }

    @Override
    public String toString() {
        return this.roll;
    }

}
