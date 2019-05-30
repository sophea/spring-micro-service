#!/usr/bin/env bash

if [ "$1" = "" ]; then
    echo "ERROR. Must pass the version to set, for example: $0 1.0.0-SNAPSHOT"
    exit 1;
fi

basedir=$(dirname $0)

sed -i '/version=/d' $basedir/../../gradle.properties
echo "version=$1" >> $basedir/../../gradle.properties
