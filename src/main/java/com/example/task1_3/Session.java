package com.example.task1_3;

public class Session implements AutoCloseable {

  private final String name;
  private final Connection connection;
  private final boolean failOnClose;
  private boolean closed;

  public Session(String name, Connection connection, boolean failOnClose) {
    this.name = name;
    this.connection = connection;
    this.failOnClose = failOnClose;
    System.out.println("Сессия " + name + " открыта");
  }

  public void execute(String sql) {
    if (closed)
      throw new IllegalStateException("Сессия " + name + " уже закрыта");

    System.out.println("Session " + name + " запрос: " + sql);
    connection.query(sql);
  }

  @Override
  public void close() throws Exception {
    if (closed)
      return;

    closed = true;
    System.out.println("Сессия " + name + " закрывается");

    if (failOnClose)
      throw new Exception("Session " + name + ".close() failed");
  }
}
