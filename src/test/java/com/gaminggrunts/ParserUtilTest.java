package com.gaminggrunts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ParserUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(com.gaminggrunts.ParserUtil.class);

    @Test
    void testEmptyString() {
        Roll actual = null;
        actual = ParserUtil.runParser("");
        assertEquals(0, actual.getGroupList().size());
    }

    @Test
    void testSingleNumber() {
        Roll actual = null;
        actual = ParserUtil.runParser("3");
        List<Group> groupList = actual.getGroupList();
        assertEquals(1, groupList.size(), "number of groups");
        Group group = groupList.get(0);
        List<Die> dieList = group.getDieList();
        assertEquals(1, dieList.size(), "number of Die objects");
        Die die = dieList.get(0);
        assertTrue(die instanceof Number, "Die object is a Number");
        assertEquals(3, die.getSides());
    }

    @Test
    void testSingleDie() {
        Roll actual = null;
        actual = ParserUtil.runParser("3D6");
        List<Group> groupList = actual.getGroupList();
        assertEquals(1, groupList.size(), "number of groups");
        Group group = groupList.get(0);
        List<Die> dieList = group.getDieList();
        assertEquals(3, dieList.size(), "number of Die objects");
        Die die = dieList.get(0);
        assertTrue(die instanceof Die, "Die object is a Die");
        assertEquals(6, die.getSides());
    }

    @Test
    void testTwoDice() {
        Roll actual = null;
        actual = ParserUtil.runParser("2D6 + W6");
        List<Group> groupList = actual.getGroupList();
        assertEquals(2, groupList.size(), "number of groups");

        Group group = groupList.get(0);
        List<Die> dieList = group.getDieList();
        assertEquals(2, dieList.size(), "number of Die objects");
        Die die = dieList.get(0);
        assertTrue(die instanceof Die, "Die object is a Die");
        assertEquals(6, die.getSides());

        group = groupList.get(1);
        dieList = group.getDieList();
        assertEquals(1, dieList.size(), "number of Die objects");
        die = dieList.get(0);
        assertTrue(die instanceof WildDie, "Die object is a WildDie");
        assertEquals(6, die.getSides());
    }

    @Test
    void testOneDieWithNegativeNumber() {
        Roll actual = null;
        actual = ParserUtil.runParser("4D8 - 3");
        List<Group> groupList = actual.getGroupList();
        assertEquals(2, groupList.size(), "number of groups");

        Group group = groupList.get(0);
        List<Die> dieList = group.getDieList();
        assertEquals(4, dieList.size(), "number of Die objects");
        Die die = dieList.get(0);
        assertTrue(die instanceof Die, "Die object is a Die");
        assertEquals(8, die.getSides());

        group = groupList.get(1);
        assertEquals(-1, group.getPlusminus(), "gropu is negative");
        dieList = group.getDieList();
        assertEquals(1, dieList.size(), "number of Die objects");
        die = dieList.get(0);
        assertTrue(die instanceof Number, "Die object is a Number");
        assertEquals(3, die.getSides());
    }
}
