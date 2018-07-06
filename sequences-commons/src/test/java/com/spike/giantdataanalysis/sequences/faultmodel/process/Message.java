package com.spike.giantdataanalysis.sequences.faultmodel.process;

import com.spike.giantdataanalysis.sequences.faultmodel.support.MoreBytes;

/**
 * The message abstration.
 * 
 * <pre>
 * Message format
 * 
 * firt byte
 * 0      you are primary          
 * 1      checkpoint message
 * 2      regular kick off: retry times, total send times
 * ...      
 * 126    ticket request: request process id
 * 127    ticket request response: sequence
 * </pre>
 */
class Message {

  static int PROTOCOL_BYTE_SIZE = 1;

  static int protocol(byte[] b) {
    return b[0];
  }

  boolean status;
  Message next; // for message queue
  byte[] value = new byte[ProcessConfiguration.MESSAGE_DATA_SIZE];

  Message() {
  }

  Message(boolean status) {
    this.status = status;
  }

  Message(boolean status, byte[] value) {
    this.status = status;
    this.value = value;
  }

  Message(boolean status, byte[] value, Message next) {
    this.status = status;
    this.value = value;
    this.next = next;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("[").append(status);
    sb.append(", ").append(MoreBytes.toHex(value));
    sb.append("]");

    if (next != null) {
      sb.append("\n\t").append(next.toString());
    }

    return sb.toString();
  }
}
