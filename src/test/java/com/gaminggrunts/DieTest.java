package com.gaminggrunts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DieTest {
  private static final Logger logger = LoggerFactory.getLogger(DieTest.class);

  @Test
  void testDie() {
    try (MockedStatic<DieUtil> utilities = Mockito.mockStatic(DieUtil.class)) {
      utilities.when(() -> DieUtil.rollDie(4)).thenReturn(6);
      Die die = new Die(4);
      die.roll();
      assertEquals(die.getResult(), 6);
      assertEquals(die.toString(), "6");
    }
  }

}
