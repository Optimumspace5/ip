#!/usr/bin/env bash

mkdir -p ../bin
find ../src/main/java -name "*.java" -print0 | xargs -0 javac -d ../bin
if [ $? -ne 0 ]; then
  echo "********** BUILD FAILURE **********"
  exit 1
fi

java -classpath ../bin ace.Ace < input.txt > ACTUAL.TXT
diff -u EXPECTED.TXT ACTUAL.TXT
