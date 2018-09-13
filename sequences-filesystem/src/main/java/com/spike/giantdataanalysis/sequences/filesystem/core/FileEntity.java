package com.spike.giantdataanalysis.sequences.filesystem.core;

import java.io.RandomAccessFile;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.spike.giantdataanalysis.sequences.filesystem.FileAccessModeEnum;

/**
 * File Abstraction.
 */
public class FileEntity {
  private String name;
  private int number; // unique in file system
  private ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
  private RandomAccessFile handler;
  private FileAccessModeEnum accessMode;

  public FileEntity() {
  }

  public FileEntity(String name, FileAccessModeEnum accessMode) {
    this.name = name;
    this.accessMode = accessMode;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    FileEntity other = (FileEntity) obj;
    if (name == null) {
      if (other.name != null) return false;
    } else if (!name.equals(other.name)) return false;
    return true;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public ReentrantReadWriteLock getLock() {
    return lock;
  }

  public void setLock(ReentrantReadWriteLock lock) {
    this.lock = lock;
  }

  public RandomAccessFile getHandler() {
    return handler;
  }

  public void setHandler(RandomAccessFile handler) {
    this.handler = handler;
  }

  public FileAccessModeEnum getAccessMode() {
    return accessMode;
  }

  public void setAccessMode(FileAccessModeEnum accessMode) {
    this.accessMode = accessMode;
  }

  @Override
  public String toString() {
    long handlerOffset = 0;
    try {
      handlerOffset = handler.getFilePointer();
    } catch (Exception e) {
    }
    return "FileEntity [name=" + name + ", number=" + number + ", handler offset=" + handlerOffset
        + ", accessMode=" + accessMode + "]";
  }

}
