package com.spike.giantdataanalysis.sequences.core.file.log;

import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.BEGIN;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.BYTE_MAX_STRING_LEN;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.END;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.INTEGER_MAX_STRING_LEN;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.LONG_MAX_STRING_LEN;
import static com.spike.giantdataanalysis.sequences.serialize.MoreSerializable.SEP;

import java.util.Date;

import com.google.common.base.Strings;
import com.spike.giantdataanalysis.sequences.serialize.Stringable;

public class LogRecord implements Stringable<LogRecord> {
  private static final long serialVersionUID = 6439536718753733691L;

  public LSN lsn = LSN.NULL;
  public LSN prev_lsn = LSN.NULL; // previous lsn
  public long timestamp = new Date().getTime();
  public int rmid = -1; // RM id
  public int txnid = -1;// Transaction id
  public LSN txn_prev_lsn = LSN.NULL; // previous lsn in txnid
  public long length = 0; // length of body
  public byte[] body = new byte[0]; // body

  public static LogRecord NULL = new LogRecord();

  // ---------------------------------------------------------------------------
  // Stringable
  // ---------------------------------------------------------------------------
  public static final String PREFIX = "LR";

  @Override
  public LogRecord fromString(String raw) {
    int start = PREFIX.length() + BEGIN.length();
    int end = start + LSN.NULL.size();
    LSN _lsn = LSN.NULL.fromString(raw.substring(start, end));
    start = end + SEP.length();
    end = start + LSN.NULL.size();
    LSN _prev_lsn = LSN.NULL.fromString(raw.substring(start, end));
    start = end + SEP.length();
    end = start + LONG_MAX_STRING_LEN;
    long _timestamp = Long.valueOf(raw.substring(start, end));
    start = end + SEP.length();
    end = start + INTEGER_MAX_STRING_LEN;
    int _rmid = Integer.valueOf(raw.substring(start, end));
    start = end + SEP.length();
    end = start + INTEGER_MAX_STRING_LEN;
    int _txnid = Integer.valueOf(raw.substring(start, end));
    start = end + SEP.length();
    end = start + LSN.NULL.size();
    LSN _txn_prev_lsn = LSN.NULL.fromString(raw.substring(start, end));
    start = end + SEP.length();
    end = start + LONG_MAX_STRING_LEN;
    long _length = Long.valueOf(raw.substring(start, end));
    start = end + SEP.length();
    end = raw.length() - SEP.length() + 1;
    byte[] _body = raw.substring(start, end).getBytes();

    LogRecord result = new LogRecord();
    result.lsn = _lsn;
    result.prev_lsn = _prev_lsn;
    result.timestamp = _timestamp;
    result.rmid = _rmid;
    result.txnid = _txnid;
    result.txn_prev_lsn = _txn_prev_lsn;
    result.length = _length;
    result.body = _body;

    return result;
  }

  @Override
  public String asString() {

    return PREFIX + BEGIN //
        + lsn.asString() + SEP//
        + prev_lsn.asString() + SEP//
        + Strings.padStart(String.valueOf(timestamp), LONG_MAX_STRING_LEN, '0') + SEP//
        + ((rmid < 0)
            ? "-" + Strings.padStart(String.valueOf(-1 * rmid), INTEGER_MAX_STRING_LEN - 1, '0')
            : Strings.padStart(String.valueOf(rmid), INTEGER_MAX_STRING_LEN, '0'))
        + SEP //
        + ((txnid < 0)
            ? "-" + Strings.padStart(String.valueOf(-1 * txnid), INTEGER_MAX_STRING_LEN - 1, '0')
            : Strings.padStart(String.valueOf(txnid), INTEGER_MAX_STRING_LEN, '0'))
        + SEP//
        + txn_prev_lsn.asString() + SEP//
        + Strings.padStart(String.valueOf(length), LONG_MAX_STRING_LEN, '0') + SEP//
        + new String(body)//
        + END;
  }

  @Override
  public int size() {
    return PREFIX.length() + BEGIN.length() + //
        lsn.size() + SEP.length()//
        + prev_lsn.size() + SEP.length()//
        + LONG_MAX_STRING_LEN + SEP.length()//
        + INTEGER_MAX_STRING_LEN + SEP.length()//
        + INTEGER_MAX_STRING_LEN + SEP.length()//
        + txn_prev_lsn.size() + SEP.length()//
        + LONG_MAX_STRING_LEN + SEP.length()//
        + body.length * BYTE_MAX_STRING_LEN//
        + END.length();
  }
  // ---------------------------------------------------------------------------
  // Byteable
  // ---------------------------------------------------------------------------
  // @Override
  // public byte[] toBytes() {
  // return MoreBytes.add(new byte[][] { //
  // lsn.toBytes(), //
  // prev_lsn == null ? LSN.NULL.toBytes() : prev_lsn.toBytes(), //
  // MoreBytes.toBytes(timestamp), //
  // MoreBytes.toBytes(rmid), //
  // MoreBytes.toBytes(txnid), //
  // txn_prev_lsn == null ? LSN.NULL.toBytes() : txn_prev_lsn.toBytes(), //
  // MoreBytes.toBytes(length), //
  // body });
  // }
  //
  // @Override
  // public LogRecord fromBytes(byte[] bytes) {
  // LogRecord result = new LogRecord();
  //
  // int offset = 0;
  // LSN lsn = LSN.NULL;
  // result.lsn = lsn.fromBytes(MoreBytes.copy(bytes, offset, lsn.size()));
  // offset += lsn.size();
  // result.prev_lsn = lsn.fromBytes(MoreBytes.copy(bytes, offset, lsn.size()));
  // offset += lsn.size();
  // result.timestamp = MoreBytes.getLong(bytes, offset);
  // offset += (Long.SIZE / Byte.SIZE);
  // result.rmid = MoreBytes.getInt(bytes, offset);
  // offset += (Integer.SIZE / Byte.SIZE);
  // result.txnid = MoreBytes.getInt(bytes, offset);
  // offset += (Integer.SIZE / Byte.SIZE);
  // result.txn_prev_lsn = lsn.fromBytes(MoreBytes.copy(bytes, offset, lsn.size()));
  // offset += lsn.size();
  // result.length = MoreBytes.getLong(bytes, offset);
  // offset += (Long.SIZE / Byte.SIZE);
  // result.body = MoreBytes.tail(bytes, bytes.length - offset);
  //
  // return result;
  // }
  //
  // @Override
  // public int size() {
  // return LSN.NULL.size() * 3 //
  // + (Long.SIZE / Byte.SIZE) * 2 //
  // + (Integer.SIZE / Byte.SIZE) * 2//
  // + body.length;
  // }

}
