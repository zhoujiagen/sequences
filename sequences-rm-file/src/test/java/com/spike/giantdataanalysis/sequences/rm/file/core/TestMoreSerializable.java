package com.spike.giantdataanalysis.sequences.rm.file.core;

import com.spike.giantdataanalysis.sequences.rm.file.core.log.LSN;
import com.spike.giantdataanalysis.sequences.rm.file.core.log.LogRecord;

public class TestMoreSerializable {

  public static void main(String[] args) {
    testLSN();
    testLogRecord();
  }

  static void testLogRecord() {
    LogRecord logRecord = LogRecord.NULL;
    String logRecordString = logRecord.asString();
    System.out.println(logRecordString);
    LogRecord logRecord2 = LogRecord.NULL.fromString(logRecordString);
    System.out.println(logRecord2.asString());

    System.out.println(LogRecord.NULL.fromString(logRecord2.asString()).asString());
  }

  static void testLSN() {
    LSN lsn = LSN.NULL;
    String lsnString = lsn.asString();
    System.out.println(lsnString);
    LSN lsn2 = LSN.NULL.fromString(lsnString);
    System.out.println(lsn2.asString());

    System.out.println(LSN.NULL.fromString(lsn2.asString()).asString());
  }
}
