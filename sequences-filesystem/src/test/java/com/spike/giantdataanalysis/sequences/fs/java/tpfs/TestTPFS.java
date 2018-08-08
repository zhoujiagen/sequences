package com.spike.giantdataanalysis.sequences.fs.java.tpfs;

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

// NEED TEST!!!
public class TestTPFS {
  public static void main(String[] args) throws IOException {

    // TPFSConfiguration

    FileSystem fs = FileSystems.getFileSystem(URI.create("tp:///"));
    Path path = fs.getPath("/foo");
    System.out.println(path.normalize().toUri().toString());
    Files.createDirectories(path);
    Path hello = path.resolve("hello.txt");
    
    FileStore fileStore = Files.getFileStore(path);
    System.out.println(fileStore.name());
  }

  static void jimfs() throws IOException {

    FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
    Path foo = fs.getPath("/foo");
    Files.createDirectory(foo);

    // write
    Path hello = foo.resolve("hello.txt"); // /foo/hello.txt
    Files.write(hello, ImmutableList.of("hello world"), StandardCharsets.UTF_8);

    // read
    try (BufferedReader reader = Files.newBufferedReader(hello);) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
    }

  }
}
