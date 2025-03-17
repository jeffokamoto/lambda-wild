package com.gaminggrunts;

public class Number extends Die {

    public Number(final Integer sides) {
        super(sides);
    }

    @Override
    public void roll() {
        setResult(getSides());
        setRoll(Integer.toString(getSides()));
    }

    @Override
    public String toString() {
        return getRoll();
    }

}