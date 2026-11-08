package com.example.task1_3;


public class Connection implements AutoCloseable {

  private final String name;
  private final boolean failOnClose;
  private boolean closed;

  public Connection(String name,boolean failOnClose){
    this.name = name;
    this.failOnClose = failOnClose;

    System.out.println("Соединение " + name + " открыто.");
  }

  public void query(String sql){
    if(closed)
      throw new IllegalStateException("Соединение" + name + "уже закрыто.");

    System.out.println("Соединение "+ name + "запрос: "+ sql);
  }

  public void close() throws Exception{
    if(closed)
      return;

    closed = true;
    System.out.println("Соединение " + name + " закрывается");
    if (failOnClose) {
      throw new Exception("Connection " + name + ".close() failed");
    }
  }

}