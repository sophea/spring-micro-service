#!/usr/bin/env bash

basedir=$(dirname $0)
cat $basedir/../../gradle.properties | grep version | cut -d\= -f2
