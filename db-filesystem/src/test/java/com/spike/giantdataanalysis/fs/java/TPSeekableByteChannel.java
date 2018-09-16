package com.spike.giantdataanalysis.fs.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

public class TPSeekableByteChannel implements SeekableByteChannel {

  private File file;
  private String mode;
  private RandomAccessFile raf;

  public TPSeekableByteChannel(File file, String mode) {
    try {
      this.file = file;
      this.mode = mode;
      raf = new RandomAccessFile(file, mode);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean isOpen() {
    return true;
  }

  @Override
  public void close() throws IOException {
    raf.close();
  }

  @Override
  public int read(ByteBuffer dst) throws IOException {
    int left = dst.remaining();
    byte[] b = new byte[left];
    int c = raf.read(b);
    dst.put(b);
    return c;
  }

  @Override
  public int write(ByteBuffer src) throws IOException {
    byte[] dst = new byte[src.remaining()];
    src.get(dst);
    raf.write(dst);
    return dst.length;
  }

  @Override
  public long position() throws IOException {
    return raf.getFilePointer();
  }

  @Override
  public SeekableByteChannel position(long newPosition) throws IOException {
    raf.close();
    TPSeekableByteChannel result = new TPSeekableByteChannel(file, mode);
    result.raf.seek(newPosition);
    return result;
  }

  @Override
  public long size() throws IOException {
    return raf.length();
  }

  @Override
  public SeekableByteChannel truncate(long size) throws IOException {
    raf.close();
    TPSeekableByteChannel result = new TPSeekableByteChannel(file, mode);
    result.raf.setLength(size);
    return result;
  }

}
