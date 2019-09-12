#!/bin/bash

DIR=`dirname ${BASH_SOURCE[0]}`

if [ "$1" == "test" ]; then   # test
  echo "Test not implemented."
  exit 1
else  # build
  pushd "$DIR/../"
  ./gradlew build -x test   # skip tests
  popd
fi 

