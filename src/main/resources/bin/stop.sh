#!/bin/bash

cd `dirname $0`
cd ..
BASE=`pwd`
MAIN_CLASS=

ps axfww | grep "$MAIN_CLASS" | grep " $BASE" | grep -v grep | awk '{print $1}' | xargs kill -9