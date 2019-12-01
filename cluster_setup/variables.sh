alias ll='ls -alF'
alias la='ls -A'
alias l='ls -CF'

if [ `hostname` = 'ric-edge-01.sci.pitt.edu' ]; then
        unset -f which
fi

export JAVA_HOME=/usr/local/jdk1.8.0_101
export PATH=${JAVA_HOME}/bin:${PATH}
export HADOOP_CLASSPATH=/opt/cloudera/parcels/CDH/lib/hadoop/hadoop-common.jar:/opt/cloudera/parcels/CDH/lib/hadoop-mapreduce/hadoop-mapreduce-client-core.jar
