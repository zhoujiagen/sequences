package com.spike.giantdataanalysis.sequences.core.support;

/**
 * Adapter for specification using C.
 */
public interface ICJavaAdapter {
  /** function pointer */
  public interface IOperation<I, O> {
    O work(I input);
  }

  /** output parameter */
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

  /** pointer */
  public class CPtr<T> {
    protected T value;

    public CPtr() {
    }

    public CPtr(T value) {
      this.value = value;
    }

    public static <T> CPtr<T> of(T value) {
      return new CPtr<>(value);
    }

    public T value() {
      return this.value;
    }

    public void value(T value) {
      this.value = value;
    }
  }

  /** updatable pointer */
  public class CUPtr<T extends ICUpdateable<T>> {
    protected T value;

    public CUPtr() {
    }

    public CUPtr(T value) {
      this.value = value;
    }

    public static <T extends ICUpdateable<T>> CUPtr<T> of(T value) {
      return new CUPtr<>(value);
    }

    public T value() {
      return this.value;
    }

    public void value(T value) {
      this.value.update(value);
    }
  }

  /** updatable abstraction */
  public interface ICUpdateable<T> {
    void update(T t);
  }

}