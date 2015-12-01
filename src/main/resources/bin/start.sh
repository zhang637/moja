#!/bin/bash

date=`date +'%Y-%m-%d'`
LIB=./lib/*.jar
CLASSPATH=$CLASSPATH
for i in $LIB
do
	CLASSPATH="$i:$CLASSPATH"
done
echo $CLASSPATH
java -cp $CLASSPATH./classes com.aioute.server.Server 8801 8421 > log/xiaole_$date.log  2>&1