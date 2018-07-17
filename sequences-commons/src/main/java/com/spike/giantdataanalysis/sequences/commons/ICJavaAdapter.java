package com.spike.giantdataanalysis.sequences.commons;

/**
 * Adapter for specification using C.
 */
public interface ICJavaAdapter {
  // function pointer
  public interface IOperation<I, O> {
    O work(I input);
  }

  // output parameter
  public class OutParameter<T> {
    private T t;

    public OutParameter() {
    }

    public OutParameter(T t) {
      this.t = t;
    }

    public void setValue(T t) {
      this.t = t;
    }

    public T value() {
      return t;
    }
  }

  // pointer
  public class CPointer<T> {
    protected T value;

    public CPointer() {
    }

    public CPointer(T value) {
      this.value = value;
    }

    public static <T> CPointer<T> of(T value) {
      return new CPointer<>(value);
    }

    public T value() {
      return this.value;
    }

    public void value(T value) {
      this.value = value;
    }
  }

}