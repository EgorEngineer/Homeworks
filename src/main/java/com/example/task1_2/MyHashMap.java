package com.example.task1_2;

import java.util.HashMap;
import java.util.Objects;

public class MyHashMap<K,V> {

  private static final int DEFAULT_CAPACITY = 16;
  private static final float LOAD_FACTOR = 0.75f;

  private static final class Node<K,V>{
    K key;
    V value;
    Node<K,V> next;

    Node(K key, V value, Node<K,V> next ){
      this.key = key;
      this.value = value;
      this.next = next;
    }
  }

  private Node<K,V>[] buckets;
  private int size;

  private int bucketIndex(K key){
    if(key == null) return 0;
    return Math.floorMod(key.hashCode(),buckets.length);
  }

  private void resize(){
    Node<K,V>[] old = buckets;
    buckets = (Node<K, V>[]) new Node[old.length * 2];

    for(Node<K,V> head : old){
      Node<K,V> n = head;
      while(n != null){
        Node<K,V> next = n.next;
        int index = bucketIndex(n.key);
        n.next = buckets[index];
        buckets[index] = n;
        n = next;
      }
    }
  }

  public MyHashMap(){
    this.buckets = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
  }

  public int size(){
    return this.size;
  }

  public boolean isEmpty(){
    return size == 0;
  }

  public V get(K key){
    int index = bucketIndex(key);

    for(Node<K,V> n = buckets[index];n != null; n=n.next){
      if(Objects.equals(n.key,key))
        return n.value;
    }

    return null;
  }

  public V put(K key, V value){
    int index = bucketIndex(key);

    for(Node<K,V> n = buckets[index]; n!=null; n=n.next){
      if(Objects.equals(n.key,key)){
        V old = n.value;
        n.value = value;
        return old;
      }
    }

    buckets[index] = new Node<>(key,value,buckets[index]);
    size++;

    if(size > buckets.length*LOAD_FACTOR)
      resize();

    return null;
  }

  public V remove(K key){
    int index = bucketIndex(key);
    Node<K, V> prev = null;

    for(Node<K,V> n=buckets[index]; n != null; n= n.next) {
      if (Objects.equals(key, n.key)) {
        if (prev == null)
          buckets[index] = n.next;
        else
          prev.next = n.next;

        size--;
        return n.value;
      }
      prev =n;
    }
    return null;
  }
}
