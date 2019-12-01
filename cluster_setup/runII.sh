#!/usr/bin/env bash
source variables.sh

source_dir="inputDataII"

hadoop fs -rm -r -f -skipTrash $source_dir outputII
hadoop fs -put $source_dir/ .
hadoop jar InvertedIndex.jar InvertedIndex $source_dir outputII

hadoop fs -getmerge -nl outputII collectedResultsII 

