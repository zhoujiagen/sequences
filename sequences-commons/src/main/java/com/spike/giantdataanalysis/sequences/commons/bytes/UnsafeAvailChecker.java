package com.spike.giantdataanalysis.sequences.commons.bytes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnsafeAvailChecker {

  private static final String CLASS_NAME = "sun.misc.Unsafe";
  private static final Logger LOG = LoggerFactory.getLogger(UnsafeAvailChecker.class);
  private static boolean avail = false;
  private static boolean unaligned = false;

  static {
    avail = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
      @Override
      public Boolean run() {
        try {
          Class<?> clazz = Class.forName(CLASS_NAME);
          Field f = clazz.getDeclaredField("theUnsafe");
          f.setAccessible(true);
          return f.get(null) != null;
        } catch (Throwable e) {
          LOG.warn("sun.misc.Unsafe is not available/accessible", e);
        }
        return false;
      }
    });
    // When Unsafe itself is not available/accessible consider unaligned as false.
    if (avail) {
      try {
        // Using java.nio.Bits#unaligned() to check for unaligned-access capability
        Class<?> clazz = Class.forName("java.nio.Bits");
        Method m = clazz.getDeclaredMethod("unaligned");
        m.setAccessible(true);
        unaligned = (Boolean) m.invoke(null);
      } catch (Exception e) {
        LOG.warn("java.nio.Bits#unaligned() check failed."
            + "Unsafe based read/write of primitive types won't be used",
          e);
      }
    }
  }

  /**
   * @return true when running JVM is having sun's Unsafe package available in it and it is
   *         accessible.
   */
  public static boolean isAvailable() {
    return avail;
  }

  /**
   * @return true when running JVM is having sun's Unsafe package available in it and underlying
   *         system having unaligned-access capability.
   */
  public static boolean unaligned() {
    return unaligned;
  }

  private UnsafeAvailChecker() {
    // private constructor to avoid instantiation
  }
}