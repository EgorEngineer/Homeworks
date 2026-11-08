package com.example.task1_2Test;

import com.example.task1_2.MyHashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class MyHashMapTest {

  //ПУСТАЯ КОЛЛЕКЦИЯ
  @Test
  void emptyMapHasZeroSize() {
    MyHashMap<String, Integer> map = new MyHashMap<>();
    assertEquals(0, map.size());
  }

  @Test
  void emptyMapReturnsNullOnGet() {
    MyHashMap<String, Integer> map = new MyHashMap<>();
    assertNull(map.get("missing"));
  }

  @Test
  void emptyMapReturnsNullOnRemove() {
    MyHashMap<String, Integer> map = new MyHashMap<>();
    assertNull(map.remove("missing"));
    assertEquals(0, map.size());
  }

  //ОДИН ЭЛЕМЕНТ
  @Test
  void singleElementPutGet() {
    MyHashMap<String, Integer> map = new MyHashMap<>();
    map.put("a", 1);

    assertEquals(1, map.size());
    assertEquals(1, map.get("a"));
    assertNull(map.get("b"));
  }

  @Test
  void singleElementRemove() {
    MyHashMap<String, Integer> map = new MyHashMap<>();
    map.put("a", 1);

    assertEquals(1, map.remove("a"));
    assertEquals(0, map.size());
    assertNull(map.get("a"));
  }

  //РЕСАЙЗ / РЕХЭШИРОВАНИЕ
  @Test
  void resizePreservesAllElements() {
    MyHashMap<Integer, Integer> map = new MyHashMap<>();
    int n = 10_000;

    for (int i = 0; i < n; i++) {
      map.put(i, i * 2);
    }

    assertEquals(n, map.size());
    for (int i = 0; i < n; i++) {
      assertEquals(i * 2, map.get(i));
    }
  }

  @Test
  void removeAfterResize() {
    MyHashMap<Integer, Integer> map = new MyHashMap<>();
    int n = 10_000;

    for (int i = 0; i < n; i++) {
      map.put(i, i * 2);
    }
    for (int i = 0; i < n; i += 2) {
      assertEquals(i * 2, map.remove(i));
    }

    assertEquals(n / 2, map.size());
    for (int i = 1; i < n; i += 2) {
      assertEquals(i * 2, map.get(i));
    }
    for (int i = 0; i < n; i += 2) {
      assertNull(map.get(i));
    }
  }
}
