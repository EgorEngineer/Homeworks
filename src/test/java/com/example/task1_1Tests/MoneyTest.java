package com.example.task1_1Tests;

import com.example.task1_1.Currency;
import com.example.task1_1.Money;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class MoneyTest {

  /*
  При неправильном переопределении hashCode() равные по equals() объекты получают разные хэш-коды.
  Из-за этого set будет содержать дубликаты, что противоречит спецификации Set как коллекции без дубликатов.
  */
  @Test
  void hashSetCollapsesEqualObjects() {
    Set<Money> set = new HashSet<>();
    set.add(new Money(10_050L, Currency.USD));
    set.add(new Money(10_050L, Currency.USD));
    set.add(new Money(10_050L, Currency.USD));
    assertEquals(1, set.size());
  }
}