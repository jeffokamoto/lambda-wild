package com.gaminggrunts;

import java.util.Random;

public class DieUtil {
    private static final Random rng = new Random();

    public static Integer rollDie(final Integer sides) {
        return rng.nextInt(sides) + 1;
    }
}
