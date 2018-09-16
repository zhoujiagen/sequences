package com.spike.giantdataanalysis.db.sparql.filesystem.core;


public final class RDFNodeId {
  private String node;
  private byte[] id;

  private RDFNodeId() {
  }

  public String getNode() {
    return node;
  }

  public byte[] getId() {
    return id;
  }

}
