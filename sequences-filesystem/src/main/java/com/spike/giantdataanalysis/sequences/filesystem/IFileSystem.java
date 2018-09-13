package com.spike.giantdataanalysis.sequences.filesystem;

import java.util.List;

import com.spike.giantdataanalysis.sequences.filesystem.core.FileBlockEntity;
import com.spike.giantdataanalysis.sequences.filesystem.core.FileEntity;

/**
 * File System Abstraction.
 */
public interface IFileSystem {

  /**
   * create and allocate file.
   * @param filename should be unique in file system
   * @param allocationParameter
   * @return
   */
  void create(String filename, FileAllocationParameter allocationParameter);

  /**
   * delete and deallocate file.
   * @param filename
   */
  void delete(String filename);

  /**
   * open a file with access mode.
   * @param filename
   * @param accessMode
   * @return
   */
  FileEntity open(String filename, FileAccessModeEnum accessMode);

  /**
   * close a file.
   * @param fileEntity
   */
  void close(FileEntity fileEntity);

  /**
   * extend an existed file with give parameter.
   * @param fileEntity
   * @param allocationParameter
   */
  void extend(FileEntity fileEntity, FileAllocationParameter allocationParameter);

  /**
   * read entire block in disk to buffer in memeory.
   * @param fileEntity
   * @param blockIndex
   * @return
   */
  FileBlockEntity read(FileEntity fileEntity, int blockIndex);

  /**
   * read a line
   * @param fileEntity
   * @param blockIndex
   * @return
   */
  FileBlockEntity readLine(FileEntity fileEntity, int blockIndex);

  /**
   * read <code>blockCount</code> blocks in disk to buffer in memory.
   * @param fileEntity
   * @param blockIndex
   * @param blockCount
   * @return
   */
  List<FileBlockEntity> readc(FileEntity fileEntity, int blockIndex, int blockCount);

  /**
   * write from buffer in memeory to block in disk.
   * @param fileEntity
   * @param blockIndex
   * @param fileBlockEntity
   */
  void write(FileEntity fileEntity, int blockIndex, FileBlockEntity fileBlockEntity);

  /**
   * write continuously from buffer in memeory to block in disk.
   * @param fileEntity
   * @param blockIndex
   * @param fileBlockEntity
   */
  void writec(FileEntity fileEntity, int blockIndex, FileBlockEntity fileBlockEntity);

  /**
   * flush to disk.
   * @param fileEntity
   */
  void flush(FileEntity fileEntity);

}
