# A Dummy SPARQL RI

# 0 tech debts

## 0.1 access path

+ read block content: block organization (*)
+ validate block by checksum
+ scan records in one block
+ read record content: record organization (*)
+ B+ tree data with index file organization

+ free space management in block and in file (*)
+ insert record
+ update record
+ delete record

+ record buffer: load, update, store


## 0.2 query processing

+ analysis sample query statements
+ determine record retrieval patterns (*)
+ transform AST to algebra IR (*)
+ implement algebra IR semantic equal transformations
+ outer sorting: ORDER, GROUP BY etc


## 0.3 transaction support

+ read ARIES paper

+ WAL log: log record organization(*)
+ WAL log: when/where to write
+ WAL log: insert tag into data record and index record(*)
+ WAL log: when/how offen to archive
+ WAL log: system recovery process detailed specification

+ locking: lock representation: semaphore, monitor lock, RW lock
+ locking: lock representation: locked entity: data record, index record (*)
+ locking: lock schedule: history: who is own/wait which lock(in what frequency) (*)
+ locking: lock schedule: acquire: check wether requested lock is compatible in current history
+ locking: lock schedule: grant: fail fast/request backlog

# 1 File Organization

## 1.1 File Block

---------------------------------------------------------------------------------------------------
block size type(2) | block type(3) | next block id(16+48) | record count(12) | free offset(16) |
---------------------------------------------------------------------------------------------------
records(x) ...
---------------------------------------------------------------------------------------------------
                                                     descending record ptr[](n*R) | checksum(32)
---------------------------------------------------------------------------------------------------

note: may change due to txn.

estimation: 
file: 32GB
block: 4KB
block count per file: 2^23

## 1.2 Data Record

RDF Term id assignment record: denotation name(x) <=> MD5(name)(128)

RDF Literal: id and value

---------------------------------------------------------------------------------------------------
record type(3) | delete flag(1) | node name ptr(16) | node id ptr(16) | node name(x) | node id(16) 
---------------------------------------------------------------------------------------------------

## 1.3 Index Record

Quad(graph, subject, predicate, object)

---------------------------------------------------------------------------------------------------
prev | next | parent | ptr0 | key1 | ptr1 | ... | keyn-1 | ptrn
---------------------------------------------------------------------------------------------------

estimation:
prev,next,parent: block id(16+48) + record id(12)
ptr1,..., ptrn: block id + record id + offset
key1,...,keyn-1: fixed-length string(255*8)

# 2 Query Processing

  TBD
