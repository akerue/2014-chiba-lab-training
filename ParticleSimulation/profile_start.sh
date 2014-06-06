#!/bin/sh

if [ $# -ne 2 ]; then
	echo "You should set 2 argument "
	echo "which are class path and java byte code file"
	exit 1
fi

sudo opcontrol --deinit
sudo opcontrol --reset
sudo opcontrol --start --no-vmlinux --event INST_RETIRED:100000 --event=l1d:30000000 --separate=cpu
taskset 02 java -agentlib:jvmti_oprofile -classpath $1 $2
exit 0

