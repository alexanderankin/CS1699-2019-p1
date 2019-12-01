#!/usr/bin/env bash
source variables.sh

source_dir="inputDataWC"

hadoop fs -rm -r -f -skipTrash $source_dir outputWC
hadoop fs -put $source_dir/ .
hadoop jar WordCount2.jar WordCount2 $source_dir outputWC

hadoop fs -getmerge -nl outputWC collectedResultsWC 
#You can add -nl to enable adding newline char after the end of each file
echo cat collectedResults

