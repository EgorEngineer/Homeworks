package com.example.task1_4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtility {

  public static void copyWithoutBuffer(Path src, Path dst) throws IOException {
    InputStream in = new FileInputStream(src.toFile());
    OutputStream out = new FileOutputStream(dst.toFile());
    int b;
    long count = 0;

    while ((b = in.read()) != -1) {
      out.write(b);
      count++;
      if (count % (20L * 1024 * 1024) == 0)
        System.out.println("Copied " + (count / 1024 / 1024) + " MB");
    }
    in.close();
    out.close();
  }

  public static void copyWithBuffer(Path src, Path dst) throws IOException {
    InputStream in = new BufferedInputStream(new FileInputStream(src.toFile()), 64 * 1024);
    OutputStream out = new BufferedOutputStream(new FileOutputStream(dst.toFile()), 64 * 1024);
    int b;
    long count = 0;

    while ((b = in.read()) != -1) {
      out.write(b);
      count++;
      if (count % (20L * 1024 * 1024) == 0)
        System.out.println("Copied " + (count / 1024 / 1024) + " MB");
    }

    in.close();
    out.close();
  }

  public static void createFile(Path path, long size) throws IOException {
    OutputStream out = new BufferedOutputStream(new FileOutputStream(path.toFile()));
    byte[] block = new byte[64 * 1024];
    long written = 0;

    for (int i = 0; i < block.length; i++) {
      block[i] = (byte) i;
    }

    while (written < size) {
      int n = (int) Math.min(block.length, size - written);
      out.write(block, 0, n);
      written += n;
    }
    out.close();
  }


  public static void main(String[] args) throws IOException {
    long size = 100L * 1024 * 1024;

    Path src = Path.of("big.bin");
    Path dst = Path.of("copy.bin");

    if (!Files.exists(src)) {
      System.out.println("Creating big.bin...");
      createFile(src, size);
      System.out.println("big.bin created");
    }

    System.out.println("Copy without buffer");
    long start1 = System.nanoTime();
    copyWithoutBuffer(src, dst);
    long end1 = System.nanoTime();
    long time1 = end1 - start1;
    System.out.println("Done without buffer: " + time1 / 1_000_000 + " ms");

    System.out.println("Copy with buffer");
    long start2 = System.nanoTime();
    copyWithBuffer(src, dst);
    long end2 = System.nanoTime();
    long time2 = end2 - start2;
    System.out.println("Done with buffer: " + time2 / 1_000_000 + " ms");

    System.out.println();
    System.out.println("Without buffer: " + time1 / 1_000_000 + " ms");
    System.out.println("Buffered: " + time2 / 1_000_000 + " ms");
    System.out.println("Buffered " + (time1 / time2) + " times faster");
  }
}
