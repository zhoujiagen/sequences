package com.spike.giantdataanalysis.sequences.core.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.giantdataanalysis.sequences.core.locking.semaphore;
import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.CPtr;
import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.CUPtr;
import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.ICUpdateable;

/**
 * Utilities like {@code com.sun.Unsafe}.
 * <p>
 * Note <em>CAS</em> methods are just act as demonstraton, not production ready.
 */
public class NativeOps {
  private static final Logger LOG = LoggerFactory.getLogger(NativeOps.class);

  // ---------------------------------------------------------------------------
  // CAS
  // ---------------------------------------------------------------------------

  /** compare and swap on integer */
  public static synchronized boolean CS(CPtr<Integer> cell, CPtr<Integer> old, CPtr<Integer> _new) {
    if (cell.value().equals(old.value())) {
      cell.value(_new.value());
      return true;
    } else {
      old.value(cell.value());
      return false;
    }
  }

  /** compare and swap on long */
  public static synchronized boolean CSD(CPtr<Long> cell, CPtr<Long> old, CPtr<Long> _new) {
    if (cell.value().equals(old.value())) {
      cell.value(_new.value());
      return true;
    } else {
      old.value(cell.value());
      return false;
    }
  }

  /** compare and swap on object */
  public static synchronized <T extends ICUpdateable<T>> boolean CSP(CUPtr<T> cell, CUPtr<T> old,
      CUPtr<T> _new) {
    if (cell.value().equals(old.value())) {

      LOG.debug("cell<=new: {} <= {}", cell.value(), _new.value());
      cell.value().update(_new.value());
      return true;

    } else {

      LOG.debug("old<=cell: {} <= {}", old.value(), cell.value());
      old.value.update(cell.value());
      return false;
    }
  }

  /**
   * compare and swap on field of object.
   * @param cell
   * @param old
   * @param _new
   * @return
   */
  public static synchronized <T> boolean CSF(FieldSwapableObject<T> cell,
      FieldSwapableObject<T> old, FieldSwapableObject<T> _new) {
    LOG.debug("cell={}, old={}, _new={}", cell, old, _new);

    if (cell.out().equals(old.out())) {

      LOG.debug("cell<=new: {} <= {}", cell.out(), _new.out());
      cell.in(_new.out());
      return true;

    } else {

      LOG.debug("old<=cell: {} <= {}", old.out(), cell.out());
      old.in(cell.out());
      return false;
    }
  }

  /**
   * compare and swap on object - {@link semaphore}.
   * @param updater
   * @param cell
   * @param old
   * @param _new
   * @return
   * @see semaphore
   * @see semaphore.semaphoreSwapableObject
   */
  public static boolean CSO(semaphore.semaphoreSwapableObject cell,
      semaphore.semaphoreSwapableObject old, semaphore.semaphoreSwapableObject _new) {

    if ((cell.value()).equals(old.value())) {
      boolean result =
          semaphore.semaphoreSwapableObject.updater.compareAndSet(cell, cell.value(), _new.value());
      LOG.debug("cell<=new[{}]: {} <= {}", result, cell.value(), _new.value());
      return true;
    } else {
      boolean result =
          semaphore.semaphoreSwapableObject.updater.compareAndSet(old, old.value(), cell.value());
      LOG.debug("old<=cell[{}]: {} <= {}", result, old.value(), cell.value());
      return false;
    }
  }

}
