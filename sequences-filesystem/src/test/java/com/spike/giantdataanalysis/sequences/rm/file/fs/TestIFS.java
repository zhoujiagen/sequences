package com.spike.giantdataanalysis.sequences.rm.file.fs;

import java.util.Date;

import com.google.common.primitives.Bytes;
import com.spike.giantdataanalysis.sequences.core.file.ACCESSMODE;
import com.spike.giantdataanalysis.sequences.core.file.file.FILE;
import com.spike.giantdataanalysis.sequences.core.file.catalog.BLOCK;
import com.spike.giantdataanalysis.sequences.core.file.log.LSN;
import com.spike.giantdataanalysis.sequences.core.file.log.LogRecord;
import com.spike.giantdataanalysis.sequences.core.support.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.rm.file.IFS;

public class TestIFS {

  public static void main(String[] args) throws Exception {
    write_log();
  }

  static void write_log() throws Exception {

    IFS fs = new JavaFileSystem();

    String base = "target/";
    String filename = base + "hello.txt";
    fs.create(filename, null);

    OutParameter<FILE> OUT_FILEID = new OutParameter<>();
    FILE FILEID = new FILE();
    OUT_FILEID.setValue(FILEID);
    fs.open(filename, ACCESSMODE.U, OUT_FILEID);

    System.out.println("=== write");
    String content = "hello, there!";
    LogRecord logRecord = new LogRecord();
    logRecord.lsn = LSN.NULL;
    logRecord.prev_lsn = LSN.NULL;
    logRecord.timestamp = new Date().getTime();
    logRecord.rmid = 1;
    logRecord.txnid = 1;
    logRecord.txn_prev_lsn = LSN.NULL;
    logRecord.length = content.length();
    logRecord.body = content.getBytes();

    BLOCK BLOCKP = new BLOCK();
    BLOCKP.contents = logRecord.asString().getBytes();
    BLOCKP.contents = Bytes.concat(BLOCKP.contents, System.lineSeparator().getBytes());
    fs.write(FILEID, -2, BLOCKP);

    System.out.println("=== read again");
    fs.close(FILEID);
    fs.open(filename, ACCESSMODE.R, OUT_FILEID);
    OutParameter<BLOCK> OUT_BLOCKP = new OutParameter<>();
    fs.read(FILEID, -1, OUT_BLOCKP);
    System.out.println(new String(OUT_BLOCKP.value().contents));
  }

  static void simple() {

    IFS fs = new JavaFileSystem();

    String base = "target/";
    String filename = base + "hello.txt";
    fs.create(filename, null);

    OutParameter<FILE> OUT_FILEID = new OutParameter<>();
    FILE FILEID = new FILE();
    OUT_FILEID.setValue(FILEID);
    fs.open(filename, ACCESSMODE.U, OUT_FILEID);

    BLOCK BLOCKP = new BLOCK();
    BLOCKP.contents = new String("hello, there!").getBytes();
    fs.write(FILEID, -1, BLOCKP);

    fs.close(FILEID);

  }

}
