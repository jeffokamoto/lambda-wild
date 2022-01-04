package com.gaminggrunts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberTest {

    private static final Logger logger = LoggerFactory.getLogger(com.gaminggrunts.NumberTest.class);

    @Test
    void testNonzero() {
        Number number = new Number(4);
        number.roll();
        assertEquals(4, number.getResult());
        assertEquals("4", number.toString());
    }

    @Test
    void testZero() {
        Number number = new Number(0);
        number.roll();
        assertEquals(0, number.getResult());
        assertEquals("0", number.toString());
    }
}