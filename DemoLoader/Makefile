APPJARS=.:./libs/ibm-event.jar:${SCALA_HOME}/lib/scala-library.jar
CLASSPATH="${SPARK_HOME}/jars/*":${APPJARS}
ESIP=`ifconfig | grep -A 1 eth1 | grep "inet " | awk -F ' ' '{ print $$2;}'`
USER="admin"
PWD="password"

StartLoader.class: TaxiRecord.java EventStoreWriter.java StartLoader.java
	javac -classpath ${CLASSPATH} $?

TryConnect.class: TryConnect.java
	javac -classpath ${CLASSPATH} $?

TryConnect: TryConnect.class
	java -classpath ${CLASSPATH} TryConnect 169.48.209.252

run: StartLoader.class
	java -classpath ${CLASSPATH} StartLoader 9292 ${ESIP} ${USER} ${PWD}
