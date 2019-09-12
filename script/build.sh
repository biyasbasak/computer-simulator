#!/bin/bash

DIR=`dirname ${BASH_SOURCE[0]}`
pushd "$DIR/../"
./gradlew build -x test   # skip tests
popd

