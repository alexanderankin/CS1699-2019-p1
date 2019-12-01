#!/usr/bin/env bash
source variables.sh

./runII.sh 2>&1 > log.log1 &
IIpid=$!
./runWC.sh 2>&1 > log.log2 &
WCpid=$!

wait $IIpid $WCpid

