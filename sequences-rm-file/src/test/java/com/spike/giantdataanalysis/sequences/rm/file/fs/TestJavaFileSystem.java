package com.spike.giantdataanalysis.sequences.rm.file.fs;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import com.spike.giantdataanalysis.sequences.commons.ICJavaAdapter.OutParameter;
import com.spike.giantdataanalysis.sequences.commons.bytes.MoreBytes;
import com.spike.giantdataanalysis.sequences.rm.file.IFileSystem;
import com.spike.giantdataanalysis.sequences.rm.file.core.AccessMode;
import com.spike.giantdataanalysis.sequences.rm.file.core.FILE;
import com.spike.giantdataanalysis.sequences.rm.file.core.FILEBlock;
import com.spike.giantdataanalysis.sequences.rm.file.core.log.LSN;
import com.spike.giantdataanalysis.sequences.rm.file.core.log.LogRecord;

// FIXME(zhoujiagen) fix the byte write/read problem!!!
public class TestJavaFileSystem {

  public static void main(String[] args) throws Exception {
    write_log();
  }

  static void write_log() throws Exception {

    LogRecord nullLogRecord = LogRecord.NULL;
    int size = nullLogRecord.size();
    System.out.println("smallest LogRecord size: " + nullLogRecord.size());
    System.out.println("smallest LogRecord: \n" + MoreBytes.toHex(nullLogRecord.toBytes()));

    IFileSystem fs = new JavaFileSystem();

    String base = "target/";
    String filename = base + "hello.txt";
    fs.create(filename, null);

    OutParameter<FILE> OUT_FILEID = new OutParameter<>();
    FILE FILEID = new FILE();
    OUT_FILEID.setValue(FILEID);
    fs.open(filename, AccessMode.U, OUT_FILEID);

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
    System.out.println("logRecord: \n" + MoreBytes.toHex(logRecord.toBytes()));// toStringBinary
    System.out.println("size of logRecord: " + logRecord.size());

    System.err.println("contains new line ? "
        + MoreBytes.contains(logRecord.toBytes(), System.lineSeparator().getBytes()));

    System.out.println("=== write");
    // OutputStream os = new BufferedOutputStream(new FileOutputStream(filename));
    // os.write(logRecord.toBytes());
    // os.flush();
    // os.close();
    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
    oos.write(logRecord.toBytes());
    oos.writeUTF(System.lineSeparator());
    oos.flush();
    oos.close();

    // FILEBlock BLOCKP = new FILEBlock();
    // BLOCKP.contents = logRecord.toBytes();
    // BLOCKP.contents = Bytes.concat(BLOCKP.contents, System.lineSeparator().getBytes());
    // fs.write(FILEID, -1, BLOCKP);

    System.out.println("=== read again");
    // BufferedReader reader = new BufferedReader(new FileReader(filename));
    ObjectInputStream reader = new ObjectInputStream(new FileInputStream(filename));
    String line = null;
    while ((line = reader.readLine()) != null) {
      System.out.println(line.length());
      System.out.println(MoreBytes.toHex(line.getBytes()));

      if (line.length() > size) {
        System.err.println(nullLogRecord.fromBytes(line.getBytes()));
      }
    }
    reader.close();

  }

  static void simple() {

    IFileSystem fs = new JavaFileSystem();

    String base = "target/";
    String filename = base + "hello.txt";
    fs.create(filename, null);

    OutParameter<FILE> OUT_FILEID = new OutParameter<>();
    FILE FILEID = new FILE();
    OUT_FILEID.setValue(FILEID);
    fs.open(filename, AccessMode.U, OUT_FILEID);

    FILEBlock BLOCKP = new FILEBlock();
    BLOCKP.contents = new String("hello, there!").getBytes();
    fs.write(FILEID, -1, BLOCKP);

    fs.close(FILEID);

  }

}
