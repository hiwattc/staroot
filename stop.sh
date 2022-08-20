#!/bin/sh
#Find the Process ID for syncapp running instance

#(case1)
#PID=`ps -ef | grep com.staroot.StarootApplication 'awk {print $2}'`
#if [[ -z "$PID" ]] then
#kill -9 PID
#fi

#(case2)
kill -9 $(ps aux | grep com.staroot.StarootApplication | awk '{print $2}')
