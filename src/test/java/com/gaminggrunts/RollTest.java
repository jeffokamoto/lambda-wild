package com.gaminggrunts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RollTest {
  private static final Logger logger = LoggerFactory.getLogger(RollTest.class);

  @Test
  void testRollPositive() {
    try (MockedStatic<DieUtil> utilities = Mockito.mockStatic(DieUtil.class)) {
      utilities.when(() -> DieUtil.rollDie(6)).thenReturn(6).thenReturn(4).thenReturn(3);
      Group group = new Group();
      group.addDie(new Die(6))
        .addDie(new Die(6))
        .addDie(new Die(6));
      Roll roll = new Roll();
      roll.add(group);
      roll.calculate();
      assertEquals(roll.getResult(), 13);
      assertEquals(roll.getRollResult(), "6 4 3");
    }
  }

  @Test
  void testRollNegative() {
    try (MockedStatic<DieUtil> utilities = Mockito.mockStatic(DieUtil.class)) {
      utilities.when(() -> DieUtil.rollDie(6)).thenReturn(5).thenReturn(2).thenReturn(1);
      Group group = new Group(-1);
      group.addDie(new Die(6))
          .addDie(new Die(6))
          .addDie(new Die(6));
      Roll roll = new Roll();
      roll.add(group);
      roll.calculate();
      assertEquals(roll.getResult(), -8);
      assertEquals(roll.getRollResult(), "- 5 2 1");
    }
  }

  @Test
  void testRollPositivePlusNumber() {
    try (MockedStatic<DieUtil> utilities = Mockito.mockStatic(DieUtil.class)) {
      utilities.when(() -> DieUtil.rollDie(6)).thenReturn(6).thenReturn(4).thenReturn(3);
      Group group = new Group();
      group.addDie(new Die(6))
          .addDie(new Die(6))
          .addDie(new Die(6));
      Roll roll = new Roll();
      roll.add(group);
      Group group2 = new Group();
      group2.addDie(new Number(1));
      roll.add(group2);
      roll.calculate();
      assertEquals(roll.getResult(), 14);
      assertEquals(roll.getRollResult(), "6 4 3 + 1");
    }
  }

  @Test
  void testRollNegativeMinusNumber() {
    try (MockedStatic<DieUtil> utilities = Mockito.mockStatic(DieUtil.class)) {
      utilities.when(() -> DieUtil.rollDie(6)).thenReturn(5).thenReturn(2).thenReturn(1);
      Group group = new Group(-1);
      group.addDie(new Die(6))
          .addDie(new Die(6))
          .addDie(new Die(6));
      Roll roll = new Roll();
      roll.add(group);
      Group group2 = new Group(-1);
      group2.addDie(new Number(2));
      roll.add(group2);
      roll.calculate();
      assertEquals(roll.getResult(), -10);
      assertEquals(roll.getRollResult(), "- 5 2 1 - 2");
    }
  }

}
