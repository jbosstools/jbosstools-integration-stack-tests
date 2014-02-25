#!/bin/bash
if [ $# -lt 2 ]; then
	echo "Usage: testlog.sh <TestName> <env>";
	exit;
fi

A=`date +"%d-%m--%Y--%H:%M"`
echo -e $1"\t"$2"\t"$A
echo -e $1"\t"$2"\t"$A >> testlog
