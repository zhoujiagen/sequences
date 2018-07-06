package com.spike.giantdataanalysis.sequences.faultmodel.process;

class MessageProtocol {

  static final byte MP_IS_PRIMARY = 0;
  static final byte MP_CHECK_POINT = 1;

  static final byte MP_CLIENT_KICKOFF = 2;

  static final byte MP_OTHER = 3;

  static final byte MP_TICKET_REQUEST = 126;
  static final byte MP_TICKET_RESPONSE = 127;
}

class StateProtocol {
  static final int SP_P_START_REGUALR = 0;
  static final int SP_P_START_PAIR = 1;
}
