# 1 TPOS

Transaction Processing Operating System


5 basic system RM:

+ TP Monitor
+ TM
+ LogM
+ LockM
+ CM

Scope:

## Processor

+ TRIDS
+ Server Class
+ Request Scheduling
+ Resource Manager(RM)(with Authentication)

## Memory/Repository

+ Transaction Manager(TM)
+ Log, Context
+ Durable Queue
+ Transactional File

## Network

+ Transactional RPC
+ Transactional Session


# 2 TP Monitor

## Components

+ Presentation service
+ Queue Management
+ Server class management: Application program perspective
+ Request scheduling
+ Request authorization
+ Context management
+ Description of TP environment: meta-data dictionary/global repository/configuration database/catalog 

## Transaction Services

cooperating components based on RMs:

+ administration system RM: catalog, representation services
+ implement transaction RM: TM, LogM, CM
+ open: FileSystem, SQL DB System, Queue System, Mail System, Complex Txn Handling Manager

# 3 TRPC

transparent with respect to the location of services: 

+ local procedure call
+ remote procedure call
