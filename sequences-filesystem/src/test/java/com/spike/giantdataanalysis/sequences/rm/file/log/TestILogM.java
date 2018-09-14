package com.spike.giantdataanalysis.sequences.rm.file.log;

import com.spike.giantdataanalysis.sequences.core.file.log.LSN;
import com.spike.giantdataanalysis.sequences.core.file.log.LogRecord;
import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.filesystem.core.FileAccessModeEnum;
import com.spike.giantdataanalysis.sequences.rm.file.ILogM;

public class TestILogM {
  public static void main(String[] args) {

    ILogM.Configuration configuration = new ILogM.Configuration();
    configuration.directory1 = "target/";
    configuration.directory2 = configuration.directory1;
    configuration.filePreix = "LOG";
    configuration.nameLength = 10;
    configuration.logRecordSizeInOneFile = 2;
    ILogM lm = new DuplexingLogManager(configuration);

    log_insert(lm);

    read(lm);
  }

  static void read(ILogM lm) {
    lm.logtable_open(FileAccessModeEnum.R);

    LSN lsn = new LSN(LSN.NULL.file, 0L);
    OutParameter<LogRecord> header = new OutParameter<>();
    lm.log_read_lsn(lsn, header, 0, null);
    System.out.println(header.value().asString());

    lm.logtable_close();
  }

  static void log_insert(ILogM lm) {
    lm.logtable_open(FileAccessModeEnum.A);

    int rmid = 1;
    int txnid1 = 1;
    byte[] body = "Hello, there".getBytes();
    LSN lsn = null;

    int N = 10;
    for (int i = 0; i < N; i++) {
      lsn = lm.log_insert(rmid, txnid1, body);
      System.out.println(lsn.asString());
    }

    lm.log_flush(lsn, true);

    lm.logtable_close();
  }

}
