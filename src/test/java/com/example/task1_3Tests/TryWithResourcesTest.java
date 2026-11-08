package com.example.task1_3Tests;

import com.example.task1_3.Connection;
import com.example.task1_3.Session;
import com.example.task1_3.TextFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class TryWithResourcesTest {

  private static Captured capture(Executable action) {
    PrintStream original = System.out;
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
    Throwable error = null;
    try {
      action.execute();
    } catch (Throwable t) {
      error = t;
    } finally {
      System.setOut(original);
    }
    return new Captured(buffer.toString(StandardCharsets.UTF_8), error);
  }

  private static List<String> closeLines(String stdout) {
    return stdout.lines()
        .filter(line -> line.endsWith("закрывается"))
        .toList();
  }

  //порядок закрытия обратный порядку объявления
  @Test
  void resourcesClosedInReverseOrder() {
    Captured captured = capture(() -> {
      try (Connection c = new Connection("c", false);
           Session s = new Session("s", c, false);
           TextFile f = new TextFile("f", false)) {
        s.execute("SELECT *");
        f.write("query executed");
      }
    });

    assertNull(captured.error(), "исключений быть не должно");
    assertEquals(
        List.of(
            "Файл f закрывается",
            "Сессия s закрывается",
            "Соединение c закрывается"
        ),
        closeLines(captured.stdout())
    );
  }


  /*
  Если исключение возникает в теле try, а close() тоже выбрасывает исключение,
  то первое остаётся основным, а ошибка закрытия становится подавленной и доступна через getSuppressed().
   */
  @Test
  void primaryAndCloseFailure() {

    IllegalStateException bodyError = new IllegalStateException("ошибка в теле try");

    Captured captured = capture(() -> {
      try (Connection c = new Connection("c", false);
           Session s = new Session("s", c, false);
           TextFile f = new TextFile("f", true)) {
        throw bodyError;
      }
    });

    assertSame(bodyError, captured.error());

    /*
    При выходе из try-with-resources вызывается close() у TextFile.
    Он тоже выбрасывает исключение, но не заменяет bodyError,
    а прикрепляется к нему как второстепенное исключение.
    */
    Throwable[] suppressed = bodyError.getSuppressed();
    assertEquals(1, suppressed.length);
    assertInstanceOf(IOException.class, suppressed[0]);
    assertTrue(suppressed[0].getMessage().contains("TextFile f"));
  }

  // Если несколько ресурсов выбрасывают исключения при закрытии,
  // все они сохраняются в числе подавленных в порядке закрытия ресурсов.
  @Test
  void allCloseFailuresBecomeSuppressed() {
    IllegalStateException bodyError = new IllegalStateException("ошибка в теле try");

    Captured captured = capture(() -> {
      try (Connection c = new Connection("c", true);
           Session s = new Session("s", c, true);
           TextFile f = new TextFile("f", true)) {
        throw bodyError;
      }
    });

    assertSame(bodyError, captured.error());

    /*
    Все три ресурса выбрасывают исключения при закрытии.
    Они не мешают закрытию остальных ресурсов, а прикрепляются
    к основному исключению в порядке возникновения: TextFile -> Session -> Connection.
    */
    Throwable[] suppressed = bodyError.getSuppressed();
    assertEquals(3, suppressed.length);
    assertTrue(suppressed[0].getMessage().contains("TextFile f"));
    assertTrue(suppressed[1].getMessage().contains("Session s"));
    assertTrue(suppressed[2].getMessage().contains("Connection c"));
  }

  private record Captured(String stdout, Throwable error) {
  }
}
