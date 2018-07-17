package com.spike.giantdataanalysis.sequences.locking;

import com.spike.giantdataanalysis.sequences.locking.core.lock.LOCK_CLASS;
import com.spike.giantdataanalysis.sequences.locking.core.lock.LOCK_MODE;
import com.spike.giantdataanalysis.sequences.locking.core.lock.lock_name;

/**
 * Lock Manager.
 */
public interface ILockM {
  enum LOCK_REPLY {
    LOCK_OK(0, "acquired"), //
    LOCK_TIMEOUT(1, "timeout"), //
    LOCK_DEADLOCK(2, "dead lock"), //
    LOCK_NOT_LOCKED(3, "not acquired");//

    int value;
    String description;

    private LOCK_REPLY(int value, String description) {
      this.value = value;
      this.description = description;
    }
  }

  /**
   * lock.
   * @param lockName
   * @param lockMode
   * @param lockClass
   * @param timeout
   * @return
   */
  LOCK_REPLY lock(lock_name lockName, LOCK_MODE lockMode, LOCK_CLASS lockClass, long timeout);

  /**
   * unlock.
   * @param lockName
   * @return
   */
  LOCK_REPLY unlock(lock_name lockName);

  /**
   * unlock the lock class.
   * @param lockClass
   * @param all_le
   * @param rmid
   * @return
   */
  LOCK_REPLY unlock_class(LOCK_CLASS lockClass, boolean all_le, int rmid);
}
