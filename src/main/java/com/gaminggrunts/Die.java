package com.gaminggrunts;

public class Die {
    Integer sides;
    Integer result;
    String roll;

    public Die(final Integer sides) {
        this.sides = sides;
    }

    public Integer getSides() {
        return this.sides;
    }

    public Integer getResult() {
        return this.result;
    }

    public String getRoll() {
        return this.roll;
    }

    public Die setResult(final Integer arg) {
        this.result = arg;
        return this;
    }

    public Die setRoll(final String arg) {
        this.roll = arg;
        return this;
    }

    public void roll() {
        Integer n = DieUtil.rollDie(this.sides);
        this.result = n;
        this.roll = Integer.toString(n);
    }

    @Override
    public String toString() {
        return this.roll;
    }
}