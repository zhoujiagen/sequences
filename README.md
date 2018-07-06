
Sequences
---------

# Main References

    @book{Gray:1992:TPC:573304,
     author = {Gray, Jim and Reuter, Andreas},
     title = {Transaction Processing: Concepts and Techniques},
     year = {1992},
     isbn = {1558601902},
     edition = {1st},
     publisher = {Morgan Kaufmann Publishers Inc.},
     address = {San Francisco, CA, USA},
    }

# API

[ISequencer](sequences-api/src/main/java/com/spike/giantdataanalysis/sequences/api/ISequencer.java)

	  long START_VALUE = 1L;

	  void init(String name, long value);

	  Sequence current(String name);

	  Sequence next(String name);

	  SequenceGroup next(String name, int n);


# Strike 1: built on existing databases

+ MySQL: [MySQLSequencer](sequences-api-mysql/src/main/java/com/spike/giantdataanalysis/sequences/api/mysql/MySQLSequencer.java)

+ HBase: [HBaseSequencer](sequences-api-hbase/src/main/java/com/spike/giantdataanalysis/sequences/api/hbase/HBaseSequencer.java)

+ Cassandra: [CassandraSequencer](sequences-api-cassandra/src/main/java/com/spike/giantdataanalysis/sequences/api/cassandra/CassandraSequencer.java)

# Strike 2: built from ground

## TODOs

+ DSL: SQL?
+ storage: format, partition and replica
+ daemon with active-standby: gossip and coordination
+ anagement utilities: system status and metric
