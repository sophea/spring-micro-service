#!/usr/bin/env bash

##################################################################
#  Execution starts here.
##################################################################

 PROJECT_ID=nifty-linker-484
 COMPUTE_ZONE=asia-south1-a
 CLUSTER=standard-cluster-1

environment=$1

if [[ $environment == "TEST" ]]; then
    PROJECT_ID=virgin-red-test2
    COMPUTE_ZONE=europe-west1-b
    CLUSTER=test-cluster
elif [[ $environment == "OPS" ]]; then
    PROJECT_ID=nifty-linker-484
    COMPUTE_ZONE=asia-south1-a
    CLUSTER=standard-cluster-1
fi

echo "====deploy into $environment environment=========="
echo "    PROJECT_ID : ${PROJECT_ID}"
echo "    COMPUTE_ZONE: ${COMPUTE_ZONE}"
echo "    CLUSTER : ${CLUSTER}"
echo "==================================================="
#PROJECT_ID=$1
#COMPUTE_ZONE=$2
#CLUSTER=$3

#create configMap
#kubectl create configmap config-map --from-file=app.properties

##set project Id
gcloud config set project $PROJECT_ID

gcloud config set compute/zone $COMPUTE_ZONE

#select custer :  virgin-cluster-dev for dev2  ,  test-cluster for test
gcloud container clusters get-credentials $CLUSTER

##deploy services : PODs
#source virgin-backend-deployment.sh

##create service as load
kubectl apply -f demo-backend-deployment.yml
