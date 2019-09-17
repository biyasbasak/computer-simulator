#!/bin/bash

DIR=`dirname ${BASH_SOURCE[0]}`
  
pushd "$DIR/../"
if [ "$1" == "test" ]; then   # test
  ./gradlew test
else  # build
  ./gradlew build -x test   # skip tests
fi 
popd

