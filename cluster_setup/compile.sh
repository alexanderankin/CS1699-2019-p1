#!/usr/bin/env bash
source variables.sh
set -e

echo "mkdir -p $1/"
mkdir -p $1/

echo "javac -cp $HADOOP_CLASSPATH:acommons-cli-1.2.jar $1.java -d $1"
javac -cp $HADOOP_CLASSPATH:acommons-cli-1.2.jar $1.java -d $1

echo "jar cvf $1.jar -C $1/ ."
jar cvf $1.jar -C $1/ .
