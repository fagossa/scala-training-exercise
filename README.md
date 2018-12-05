# scala-training-exercise

## Problem description

A log file contains lines having elements separated by a single space. The expected format is:

```
<timestamp unix> <hostname> <hostname>
```
 
For example:
```
1366815793 quark garak
1366815795 brunt quark
1366815811 lilac garak
```
 
Each line represents a connection between a host (left side) to another (right side) at a specific time.


## What is expected from the exercise

The program must produce each hour:

* The list of servers connected to a specific server during this hour
* The list of servers whose a specific server got connected
* The server that generated more connections during this period

The amount of lines in a file can be very important : consider a lazy implementation 
