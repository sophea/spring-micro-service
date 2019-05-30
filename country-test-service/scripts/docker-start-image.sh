#!/usr/bin/env bash

basedir=$(dirname $0)
export version=$(${basedir}/version/getVersion.sh)
export instance_name=$(cat ${basedir}/instance_name)

echo "Running ${instance_name}:${version} instance using docker-compose.yml"
docker-compose -f ${basedir}/docker-compose.yml run -d --service-ports --name ${instance_name} ${instance_name}


