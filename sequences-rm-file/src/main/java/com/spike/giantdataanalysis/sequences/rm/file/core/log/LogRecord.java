package com.spike.giantdataanalysis.sequences.rm.file.core.log;

import java.util.Date;

import com.spike.giantdataanalysis.sequences.commons.bytes.MoreBytes;
import com.spike.giantdataanalysis.sequences.rm.file.core.Byteable;

public class LogRecord implements Byteable<LogRecord> {
  public LSN lsn = LSN.NULL;
  public LSN prev_lsn = LSN.NULL; // previous lsn
  public long timestamp = new Date().getTime();
  public int rmid = -1; // RM id
  public int txnid = -1;// Transaction id
  public LSN txn_prev_lsn = LSN.NULL; // previous lsn in txnid
  public long length = 0; // length of body
  public byte[] body = new byte[0]; // body

  public static LogRecord NULL = new LogRecord();

  @Override
  public byte[] toBytes() {
    return MoreBytes.add(new byte[][] { //
        lsn.toBytes(), //
        prev_lsn == null ? LSN.NULL.toBytes() : prev_lsn.toBytes(), //
        MoreBytes.toBytes(timestamp), //
        MoreBytes.toBytes(rmid), //
        MoreBytes.toBytes(txnid), //
        txn_prev_lsn == null ? LSN.NULL.toBytes() : txn_prev_lsn.toBytes(), //
        MoreBytes.toBytes(length), //
        body });
  }

  @Override
  public LogRecord fromBytes(byte[] bytes) {
    LogRecord result = new LogRecord();

    int offset = 0;
    LSN lsn = LSN.NULL;
    result.lsn = lsn.fromBytes(MoreBytes.copy(bytes, offset, lsn.size()));
    offset += lsn.size();
    result.prev_lsn = lsn.fromBytes(MoreBytes.copy(bytes, offset, lsn.size()));
    offset += lsn.size();
    result.timestamp = MoreBytes.getLong(bytes, offset);
    offset += (Long.SIZE / Byte.SIZE);
    result.rmid = MoreBytes.getInt(bytes, offset);
    offset += (Integer.SIZE / Byte.SIZE);
    result.txnid = MoreBytes.getInt(bytes, offset);
    offset += (Integer.SIZE / Byte.SIZE);
    result.txn_prev_lsn = lsn.fromBytes(MoreBytes.copy(bytes, offset, lsn.size()));
    offset += lsn.size();
    result.length = MoreBytes.getLong(bytes, offset);
    offset += (Long.SIZE / Byte.SIZE);
    result.body = MoreBytes.tail(bytes, bytes.length - offset);

    return result;
  }

  @Override
  public int size() {
    return LSN.NULL.size() * 3 //
        + (Long.SIZE / Byte.SIZE) * 2 //
        + (Integer.SIZE / Byte.SIZE) * 2//
        + body.length;
  }

}
