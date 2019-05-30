#!/usr/bin/env bash

basedir=$(dirname $0)
currentVersion=$(${basedir}/getVersion.sh)
instanceName=$(${basedir}/getInstanceName.sh)

fileName="$basedir/../../buildProps.properties"

echo > $fileName
echo "env.artifactName=\"${instanceName}\"" >> $fileName
echo "env.artifactVersion=\"${currentVersion}\"" >> $fileName
