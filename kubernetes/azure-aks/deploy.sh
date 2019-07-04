#!/usr/bin/env bash

##################################################################
#  Execution starts here.
##################################################################


#create configMap
#kubectl create configmap config-map --from-file=app.properties

##set project Id
#gcloud config set project $PROJECT_ID

#gcloud config set compute/zone $COMPUTE_ZONE

#select custer :  xxx-cluster-dev for dev2  ,  test-cluster for test
#gcloud container clusters get-credentials $CLUSTER

##deploy services : PODs
#source demo-backend-deployment.sh

##create service as load
kubectl apply -f demo-backend-deployment.yml
