package com.spike.giantdataanalysis.fs.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.common.collect.ImmutableList;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

public class TestTPFS {
  public static void main(String[] args) throws IOException {

    // set up TPFSConfiguration here
    URI uri = URI.create("tp:///");
    System.out.println(uri.getPath());
    FileSystem fs = FileSystems.getFileSystem(URI.create("tp:///"));
    Path path = fs.getPath("/foo");
    System.out.println(path.normalize().toUri().toString());
    Files.createDirectories(path); // create directory
    Path hello = path.resolve("hello.txt");
    System.out.println(hello.toAbsolutePath().toUri());

    FileStore fileStore = Files.getFileStore(path);
    System.out.println(fileStore.name()); // file store

    // write
    Files.write(hello, ImmutableList.of("hello world"), StandardCharsets.UTF_8);

    // read
    try (BufferedReader reader = Files.newBufferedReader(hello, StandardCharsets.UTF_8);) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
    }
  }

  static void jimfs() throws IOException {

    FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
    Path foo = fs.getPath("/foo");
    Files.createDirectory(foo);

    // write
    Path hello = foo.resolve("hello.txt"); // /foo/hello.txt
    Files.write(hello, ImmutableList.of("hello world"), StandardCharsets.UTF_8);

    // read
    try (BufferedReader reader = Files.newBufferedReader(hello, StandardCharsets.UTF_8);) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
    }

  }
}
