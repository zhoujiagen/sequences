# A Dummy SPARQL RI

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
prev | next | parent | ptr0 | key1 | ptr1 | ... | keyn-1 | ptrn )*
---------------------------------------------------------------------------------------------------

estimation:
prev,next,parent: block id(16+48) + record id(12)
ptr1,..., ptrn: block id + record id + offset
key1,...,keyn-1: fixed-length string(255*8)

# 2 Query Processing

  TBD
