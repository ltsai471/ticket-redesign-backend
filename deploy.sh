#!/bin/bash

# Configuration
APP_NAME="ticket-system"                          # Your application name
DOCKER_IMAGE="ltsai471/$APP_NAME:latest"  # Docker image path
K8S_DEPLOYMENT_YAML="./deployment.yaml" # Path to your Kubernetes YAML
DOCKER_REGISTRY="docker.io"                 # Docker registry (default is Docker Hub)

# Step 1: Clean and Package the Application
# echo ">> Cleaning and packaging the application..."
# mvn clean package -DskipTests
# if [ $? -ne 0 ]; then
  # echo ">> Maven build failed!"
  # exit 1
# fi

# Step 2: Build the Docker Image
echo ">> Building the Docker image..."
docker build -t $DOCKER_IMAGE .
if [ $? -ne 0 ]; then
  echo ">> Docker build failed!"
  exit 1
fi

# Step 3: Push the Docker Image to the Registry
echo ">> Pushing the Docker image to the registry..."
docker push $DOCKER_IMAGE
if [ $? -ne 0 ]; then
  echo ">> Docker push failed!"
  exit 1
fi

# Step 4: Apply the Kubernetes YAML Files
echo ">> Deploying to Kubernetes..."
#kubectl set image deployment.apps/ticket-system ticket-system=$DOCKER_IMAGE
## delete
kubectl delete pod -l app=$APP_NAME
kubectl apply -f $K8S_DEPLOYMENT_YAML
if [ $? -ne 0 ]; then
  echo ">> Kubernetes deployment failed!"
  exit 1
fi

# Completion Message
echo ">> Deployment successful! Your application is now live in Kubernetes."
