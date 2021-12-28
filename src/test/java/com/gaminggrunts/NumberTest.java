package com.gaminggrunts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberTest {

    private static final Logger logger = LoggerFactory.getLogger(com.gaminggrunts.NumberTest.class);

    @Test
    void testPositiveNumber() {
        Number number = new Number(4);
        number.roll();
        assertEquals(number.getResult(), 4);
        assertEquals(number.toString(), "+ 4");
    }

    @Test
    void testNegativeNumber() {
        Number number = new Number(-2);
        number.roll();
        assertEquals(number.getResult(), -2);
        assertEquals(number.toString(), "- 2");
    }

    @Test
    void testZero() {
        Number number = new Number(0);
        number.roll();
        assertEquals(number.getResult(), 0);
        assertEquals(number.toString(), "+ 0");
    }
}