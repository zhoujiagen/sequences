package com.spike.giantdataanalysis.sequences.rm.file.core;

import com.spike.giantdataanalysis.sequences.core.file.catalog.DISK;
import com.spike.giantdataanalysis.sequences.core.file.catalog.STORE;
import com.spike.giantdataanalysis.sequences.core.file.catalog.basic_file_descriptor;
import com.spike.giantdataanalysis.sequences.core.file.log.LSN;
import com.spike.giantdataanalysis.sequences.core.file.log.LogRecord;

public class TestMoreSerializable {

  public static void main(String[] args) {
    // testLSN();
    // testLogRecord();
    // testDISK();
    // testSTORE();
    testbasic_file_descriptor();
  }

  static void testbasic_file_descriptor() {
    basic_file_descriptor fd = basic_file_descriptor.NULL;
    String fdString = fd.asString();
    System.out.println(fdString);
    basic_file_descriptor fd2 = basic_file_descriptor.NULL.fromString(fdString);
    System.err.println(fd2.asString());
  }

  static void testSTORE() {
    STORE store = new STORE(new DISK());
    String storeString = store.asString();
    System.out.println(storeString);
    STORE store2 = STORE.NULL.fromString(storeString);
    System.err.println(store2.asString());

  }

  static void testDISK() {
    DISK disk = new DISK();
    String diskString = disk.asString();
    System.out.println(diskString);
    DISK disk2 = DISK.NULL.fromString(diskString);
    System.err.println(disk2.asString());
  }

  static void testLogRecord() {
    LogRecord logRecord = LogRecord.NULL;
    String logRecordString = logRecord.asString();
    System.out.println(logRecordString);
    LogRecord logRecord2 = LogRecord.NULL.fromString(logRecordString);
    System.err.println(logRecord2.asString());

    System.err.println(LogRecord.NULL.fromString(logRecord2.asString()).asString());
  }

  static void testLSN() {
    LSN lsn = LSN.NULL;
    String lsnString = lsn.asString();
    System.out.println(lsnString);
    LSN lsn2 = LSN.NULL.fromString(lsnString);
    System.err.println(lsn2.asString());

    System.err.println(LSN.NULL.fromString(lsn2.asString()).asString());
  }
}
