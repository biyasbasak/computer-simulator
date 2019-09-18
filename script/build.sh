#!/bin/bash

DIR=`dirname ${BASH_SOURCE[0]}`
  
pushd "$DIR/../"
if [ "$1" == "test" ]; then   # test
  ./gradlew test --info
elif [ "$1" == "build" ]; then # build
  ./gradlew build -x test   # skip tests
else
  ./gradlew run
fi 
popd

