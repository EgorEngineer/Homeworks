package com.example.task1_3;

import java.io.IOException;

public class TextFile implements AutoCloseable {

  private final String name;
  private final boolean failOnClose;
  private final StringBuilder buffer = new StringBuilder();
  private boolean closed;

  public TextFile(String name, boolean failOnClose) {
    this.name = name;
    this.failOnClose = failOnClose;
    System.out.println("Открыт файл " + name);
  }

  public void write(String line) {
    if (closed)
      throw new IllegalStateException("TextFile" + name + " уже закрыт");

    System.out.println("Файл " + name + ".write: " + line);
    buffer.append(line).append(System.lineSeparator());
  }

  public String content() {
    return buffer.toString();
  }

  @Override
  public void close() throws IOException {
    if (closed)
      return;

    closed = true;
    System.out.println("Файл " + name + " закрывается");
    if (failOnClose)
      throw new IOException("TextFile " + name + ".close() failed");
  }
}
