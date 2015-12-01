#!/bin/bash

cd `dirname $0`
cd ..
BASE=`pwd`
MAIN_CLASS=

PROCESS=`ps axfww | grep "$MAIN_CLASS" | grep " $BASE" | grep -v grep`
if [ ! -z "$PROCESS" ]; then
echo started
else
echo stoped
fi