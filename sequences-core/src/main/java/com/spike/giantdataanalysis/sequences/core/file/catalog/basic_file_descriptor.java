package com.spike.giantdataanalysis.sequences.core.file.catalog;

import static com.spike.giantdataanalysis.db.commons.serialize.MoreSerializable.BEGIN;
import static com.spike.giantdataanalysis.db.commons.serialize.MoreSerializable.END;
import static com.spike.giantdataanalysis.db.commons.serialize.MoreSerializable.INTEGER_MAX_STRING_LEN;
import static com.spike.giantdataanalysis.db.commons.serialize.MoreSerializable.SEP;
import static com.spike.giantdataanalysis.db.commons.serialize.MoreSerializable.SEP_LIST;

import java.util.LinkedList;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.spike.giantdataanalysis.db.commons.serialize.Stringable;
import com.spike.giantdataanalysis.sequences.core.file.allocparmp;

/**
 * catalog of a file: including <ui>
 * <li>current space allocation,
 * <li>address mapping: through <code>FILEID</code>
 * <p>
 * should be stored persistently.
 */
public class basic_file_descriptor implements Stringable<basic_file_descriptor> {
  private static final long serialVersionUID = 1L;

  public static basic_file_descriptor NULL = new basic_file_descriptor();

  public String filename = "unkown";
  public int fileno = 0; // FILEID
  public int partition_no = 0; // for partitioned file
  public int version_no = 0; // version
  public allocparmp spacerequest = allocparmp.NULL; // describe primary and secondary

  public int curr_no_extends = 0; // number of currently allocated extents
  public LinkedList<EXTENT> curr_alloc = Lists.newLinkedList(); // allocated extents

  // ---------------------------------------------------------------------------
  // Stringable
  // ---------------------------------------------------------------------------
  public static final String PREFIX = "FD";

  @Override
  public String asString() {
    return PREFIX + BEGIN//
        + filename//
        + SEP//
        + Strings.padStart(String.valueOf(fileno), INTEGER_MAX_STRING_LEN, '0') //
        + SEP //
        + Strings.padStart(String.valueOf(partition_no), INTEGER_MAX_STRING_LEN, '0') //
        + SEP //
        + Strings.padStart(String.valueOf(version_no), INTEGER_MAX_STRING_LEN, '0') //
        + SEP //
        + spacerequest.asString()//
        + SEP_LIST //
        + Strings.padStart(String.valueOf(curr_no_extends), INTEGER_MAX_STRING_LEN, '0') //
        + SEP //
        + Joiner.on(",").join(curr_alloc) //
        + END;
  }

  @Override
  public basic_file_descriptor fromString(String raw) {
    basic_file_descriptor result = new basic_file_descriptor();

    int start = PREFIX.length() + BEGIN.length();
    int end = raw.indexOf(SEP, start);
    result.filename = raw.substring(start, end);
    start = end + SEP.length();
    end = start + INTEGER_MAX_STRING_LEN;
    result.fileno = Integer.valueOf(raw.substring(start, end));
    start = end + SEP.length();
    end = start + INTEGER_MAX_STRING_LEN;
    result.partition_no = Integer.valueOf(raw.substring(start, end));
    start = end + SEP.length();
    end = start + INTEGER_MAX_STRING_LEN;
    result.version_no = Integer.valueOf(raw.substring(start, end));

    start = end + SEP.length();
    end = raw.indexOf(SEP_LIST, start);
    result.spacerequest = allocparmp.NULL.fromString(raw.substring(start, end));

    start = end + SEP_LIST.length();
    end = start + INTEGER_MAX_STRING_LEN;
    result.curr_no_extends = Integer.valueOf(raw.substring(start, end));

    start = end + SEP.length();
    end = raw.indexOf(END, start);
    String curr_allocString = raw.substring(start, end);
    if (!"".equals(curr_allocString)) {
      for (String s : Splitter.on(",").splitToList(curr_allocString)) {
        result.curr_alloc.add(EXTENT.NULL.fromString(s));
      }
    }

    return result;
  }

  @Override
  public int size() {
    return PREFIX.length() + BEGIN.length()//
        + filename.length() //
        + SEP.length() //
        + INTEGER_MAX_STRING_LEN//
        + SEP.length() //
        + INTEGER_MAX_STRING_LEN//
        + SEP.length() //
        + INTEGER_MAX_STRING_LEN//
        + SEP.length() //
        + allocparmp.NULL.size()//
        + SEP_LIST.length() //
        + INTEGER_MAX_STRING_LEN//
        + SEP.length() //
        + curr_alloc.size() * EXTENT.NULL.size()//
        + END.length();
  }

}
