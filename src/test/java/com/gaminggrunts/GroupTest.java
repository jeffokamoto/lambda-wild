package com.gaminggrunts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class GroupTest {
  private static final Logger logger = LoggerFactory.getLogger(GroupTest.class);

  @Test
  void testGroup() {
    try (MockedStatic<DieUtil> utilities = Mockito.mockStatic(DieUtil.class)) {
      utilities.when(() -> DieUtil.rollDie(6)).thenReturn(6).thenReturn(4).thenReturn(3);
      Group group = new Group();
      group.addDie(new Die(6))
        .addDie(new Die(6))
        .addDie(new Die(6));
      group.calculate();
      assertEquals(13, group.getResult());
      assertEquals("+ 6 4 3", group.toString());
    }
  }

  @Test
  void testNegativeGroup() {
    try (MockedStatic<DieUtil> utilities = Mockito.mockStatic(DieUtil.class)) {
      utilities.when(() -> DieUtil.rollDie(6)).thenReturn(5).thenReturn(2).thenReturn(1);
      Group group = new Group(-1);
      group.addDie(new Die(6))
          .addDie(new Die(6))
          .addDie(new Die(6));
      group.calculate();
      assertEquals(-8, group.getResult());
      assertEquals("- 5 2 1", group.toString());
    }
  }
}
