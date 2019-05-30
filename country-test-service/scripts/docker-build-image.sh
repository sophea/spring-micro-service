#!/usr/bin/env bash

basedir=$(dirname $0)
version=$(${basedir}/version/getVersion.sh)
instance_name=$(cat ${basedir}/instance_name)
nexusName=$(${basedir}/version/getNexusImageName.sh)

echo Building version ${version} of ${instance_name}

docker build -t ${instance_name}:${version} ${basedir}/..
docker tag ${instance_name}:${version} ${nexusName}
