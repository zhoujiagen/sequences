package com.spike.giantdataanalysis.sequences.rm.file.exception;

public class FileSystemException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static FileSystemException newE() {
    return new FileSystemException();
  }

  public static FileSystemException newE(String message) {
    return new FileSystemException(message);
  }

  public static FileSystemException newE(String message, Throwable cause) {
    return new FileSystemException(message, cause);
  }

  public static FileSystemException newE(Throwable cause) {
    return new FileSystemException(cause);
  }

  public static FileSystemException newE(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    return new FileSystemException(message, cause, enableSuppression, writableStackTrace);
  }

  public FileSystemException() {
    super();
  }

  public FileSystemException(String message) {
    super(message);
  }

  public FileSystemException(String message, Throwable cause) {
    super(message, cause);
  }

  public FileSystemException(Throwable cause) {
    super(cause);
  }

  public FileSystemException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}