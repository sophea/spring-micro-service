#!/usr/bin/env bash

basedir=$(dirname $0)
nexusName=$(${basedir}/version/getNexusImageName.sh)

echo Pushing ${nexusName}
docker push ${nexusName}
