package com.spike.giantdataanalysis.sequences.exception;

public class LogManagerException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static LogManagerException newE() {
    return new LogManagerException();
  }

  public static LogManagerException newE(String message) {
    return new LogManagerException(message);
  }

  public static LogManagerException newE(String message, Throwable cause) {
    return new LogManagerException(message, cause);
  }

  public static LogManagerException newE(Throwable cause) {
    return new LogManagerException(cause);
  }

  public static LogManagerException newE(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    return new LogManagerException(message, cause, enableSuppression, writableStackTrace);
  }

  public LogManagerException() {
    super();
  }

  public LogManagerException(String message) {
    super(message);
  }

  public LogManagerException(String message, Throwable cause) {
    super(message, cause);
  }

  public LogManagerException(Throwable cause) {
    super(cause);
  }

  public LogManagerException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}