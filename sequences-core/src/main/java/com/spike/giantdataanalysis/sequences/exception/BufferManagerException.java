package com.spike.giantdataanalysis.sequences.exception;

public class BufferManagerException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static BufferManagerException newE() {
    return new BufferManagerException();
  }

  public static BufferManagerException newE(String message) {
    return new BufferManagerException(message);
  }

  public static BufferManagerException newE(String message, Throwable cause) {
    return new BufferManagerException(message, cause);
  }

  public static BufferManagerException newE(Throwable cause) {
    return new BufferManagerException(cause);
  }

  public static BufferManagerException newE(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    return new BufferManagerException(message, cause, enableSuppression, writableStackTrace);
  }

  public BufferManagerException() {
    super();
  }

  public BufferManagerException(String message) {
    super(message);
  }

  public BufferManagerException(String message, Throwable cause) {
    super(message, cause);
  }

  public BufferManagerException(Throwable cause) {
    super(cause);
  }

  public BufferManagerException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}