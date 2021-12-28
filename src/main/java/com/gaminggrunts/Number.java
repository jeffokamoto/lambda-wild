package com.gaminggrunts;

public class Number extends Die {

    public Number(final Integer sides) {
        super(sides);
    }

    @Override
    public void roll() {
        this.setResult(this.getSides());
        if (this.getSides() < 0) {
            this.setRoll("- " + Integer.toString(-this.getSides()));
        } else {
            this.setRoll("+ " + Integer.toString(this.getSides()));
        }
    }

    @Override
    public String toString() {
        return this.getRoll();
    }

}