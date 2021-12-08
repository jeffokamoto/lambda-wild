package com.gaminggrunts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class WildDieTest {
  private static final Logger logger = LoggerFactory.getLogger(WildDieTest.class);

  final DieUtil mockDieUtil = mock(DieUtil.class);

  @Test
  void testWildDieNoWild() {
    try (MockedStatic<DieUtil> utilities = Mockito.mockStatic(DieUtil.class)) {
      utilities.when(() -> DieUtil.rollDie(6)).thenReturn(1);
      WildDie wildDie = new WildDie(6);
      wildDie.roll();
      assertEquals(wildDie.getResult(), 1);
      assertEquals(wildDie.toString(), "(1)");
    }
  }

  @Test
  void testWildDieYesWild() {
    try (MockedStatic<DieUtil> utilities = Mockito.mockStatic(DieUtil.class)) {
      utilities.when(() -> DieUtil.rollDie(6)).thenReturn(6).thenReturn(6).thenReturn(4);
      WildDie wildDie = new WildDie(6);
      wildDie.roll();
      assertEquals(wildDie.getResult(), 16);
      assertEquals(wildDie.toString(), "(6 6 4)");
    }
  }

}
