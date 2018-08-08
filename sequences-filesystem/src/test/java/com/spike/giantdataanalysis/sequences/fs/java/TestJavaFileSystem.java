package com.spike.giantdataanalysis.sequences.fs.java;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.Date;

public class TestJavaFileSystem {

  public static void main(String[] args) throws Exception {
    raf();
  }

  // https://docs.oracle.com/javase/tutorial/essential/io/rafs.html
  static void raf() throws IOException {
    RandomAccessFile raf = new RandomAccessFile("target/raf.txt", "rwd");
    long pos = 0;
    raf.seek(pos);

    String line = "hello, there!";
    raf.writeBytes(line);

    pos = line.length() - 2;
    raf.seek(pos);
    raf.writeBytes("E!");

    raf.close();
  }

  static void file() throws IOException {

    // 250790436864
    // 179977633792
    // 177146839040
    //
    // 250790436864
    // 179977875456
    // 177147080704
    File thisFile = new File(
        "src/test/java/com/spike/giantdataanalysis/sequences/fs/java/TestJavaFileSystem.java");
    System.out.println(thisFile.getTotalSpace()); // disk space
    System.out.println(thisFile.getFreeSpace());
    System.out.println(thisFile.getUsableSpace());

    // in bytes
    // 1368
    System.out.println(java.nio.file.Files.size(Paths.get(thisFile.getAbsolutePath())));
  }

  static void time() throws InterruptedException {

    // can Java support ns???
    long now = new Date().getTime();
    System.out.println(now);
    System.out.println(System.currentTimeMillis());
    System.out.println(System.nanoTime());

    long startMS = System.currentTimeMillis();
    long startNS = System.nanoTime();
    Thread.sleep(300L);

    long endMS = System.currentTimeMillis();
    long endNS = System.nanoTime();

    System.out.println(endMS - startMS);
    System.out.println(endNS - startNS);
  }

}
