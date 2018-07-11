package com.spike.giantdataanalysis.sequences.rm.file.log;

import com.spike.giantdataanalysis.sequences.commons.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.commons.bytes.MoreBytes;
import com.spike.giantdataanalysis.sequences.rm.file.ILM;
import com.spike.giantdataanalysis.sequences.rm.file.core.AccessMode;
import com.spike.giantdataanalysis.sequences.rm.file.core.log.LSN;
import com.spike.giantdataanalysis.sequences.rm.file.core.log.LogRecord;

public class TestDuplexingLogManager {
  public static void main(String[] args) {

    ILM.Configuration configuration = new ILM.Configuration();
    configuration.directory1 = "target/";
    configuration.directory2 = configuration.directory1;
    configuration.filePreix = "LOG";
    configuration.nameLength = 10;
    ILM lm = new DuplexingLogManager(configuration);

    
    read(lm);

  }
  
  static void write_regular_file() {
    
  }

  static void read(ILM lm) {
    lm.logtable_open(AccessMode.R);

    OutParameter<LogRecord> header = new OutParameter<>();
    lm.log_read_lsn(LSN.NULL, header, 0, null);
    System.out.println(MoreBytes.toHex(header.value().toBytes()));

    lm.logtable_close();
  }

  static void log_insert(ILM lm) {

    lm.logtable_open(AccessMode.A);

    int rmid = 1;
    int txnid1 = 1;
    byte[] body = "Hello, there".getBytes();
    LSN lsn = null;

    int N = 1000;
    for (int i = 0; i < N; i++) {
      lsn = lm.log_insert(rmid, txnid1, body);
    }

    lm.log_flush(lsn, true);

    lm.logtable_close();

  }

}
