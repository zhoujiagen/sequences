package com.spike.giantdataanalysis.sequences.core.locking;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import com.spike.giantdataanalysis.sequences.core.cb.PCB;
import com.spike.giantdataanalysis.sequences.core.locking.lock.LOCK_MODE;
import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.ICUpdateable;

/**
 * Semaphore: share, exclusive.
 */
public class semaphore implements ICUpdateable<semaphore> {
  public LOCK_MODE mode;
  public int count; // 0 free, 1 exclusive, >=1 share
  public PCB wait_list;

  public static semaphore NULL = new semaphore(LOCK_MODE.LOCK_FREE, 0, null);

  public semaphore() {
  }

  public semaphore(LOCK_MODE mode, int count, PCB wait_list) {
    this.mode = mode;
    this.count = count;
    this.wait_list = wait_list;
  }

  @Override
  public void update(semaphore another) {
    this.mode = another.mode;
    this.count = another.count;
    this.wait_list = another.wait_list;
  }

  // ---------------------------------------------------------------------------
  // atomic swap all fields helper
  // ---------------------------------------------------------------------------
  public static class semaphoreSwapableObject {
    public volatile semaphore value;

    public semaphoreSwapableObject(semaphore value) {
      this.value = value;
    }

    public semaphore value() {
      return value;
    }

    public void value(semaphore value) {
      this.value = value;
    }

    public static AtomicReferenceFieldUpdater<semaphoreSwapableObject, semaphore> updater =
        AtomicReferenceFieldUpdater.newUpdater(semaphoreSwapableObject.class, semaphore.class,
          "value");

    @Override
    public String toString() {
      return value == null ? "NULL" : value.toString();
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + count;
    result = prime * result + ((mode == null) ? 0 : mode.hashCode());
    result = prime * result + ((wait_list == null) ? 0 : wait_list.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    semaphore other = (semaphore) obj;
    if (count != other.count) return false;
    if (mode != other.mode) return false;
    if (wait_list == null) {
      if (other.wait_list != null) return false;
    } else if (!wait_list.equals(other.wait_list)) return false;
    return true;
  }

  @Override
  public String toString() {
    return "semaphore [mode=" + mode + ", count=" + count + ", wait_list=" + wait_list + "]";
  }
}
