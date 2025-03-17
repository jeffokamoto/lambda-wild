package com.gaminggrunts;

public class Die {
    private Integer sides;
    private Integer result;
    private String roll;

    public Die(final Integer sides) {
        this.sides = sides;
        this.result = 0;
        this.roll = "";
    }

    public Integer getSides() {
        return sides;
    }

    public Integer getResult() {
        return result;
    }

    public String getRoll() {
        return roll;
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
        Integer n = DieUtil.rollDie(getSides());
        setResult(n);
        setRoll(Integer.toString(n));
    }

    @Override
    public String toString() {
        return roll;
    }
}