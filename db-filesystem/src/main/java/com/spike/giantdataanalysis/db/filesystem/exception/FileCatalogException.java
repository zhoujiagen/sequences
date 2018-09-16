package com.spike.giantdataanalysis.db.filesystem.exception;

public class FileCatalogException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public static FileCatalogException newE() {
    return new FileCatalogException();
  }

  public static FileCatalogException newE(String message) {
    return new FileCatalogException(message);
  }

  public static FileCatalogException newE(String message, Throwable cause) {
    return new FileCatalogException(message, cause);
  }

  public static FileCatalogException newE(Throwable cause) {
    return new FileCatalogException(cause);
  }

  public static FileCatalogException newE(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    return new FileCatalogException(message, cause, enableSuppression, writableStackTrace);
  }

  public FileCatalogException() {
    super();
  }

  public FileCatalogException(String message) {
    super(message);
  }

  public FileCatalogException(String message, Throwable cause) {
    super(message, cause);
  }

  public FileCatalogException(Throwable cause) {
    super(cause);
  }

  public FileCatalogException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}