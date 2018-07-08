#!/bin/bash

# This enables us to set foreground process
set -m

java -Djava.library.path=. -jar DynamoDBLocal.jar -inMemory --sharedDb -port 8000 &

aws dynamodb create-table \
    --table-name CarAdvert \
    --attribute-definitions \
        AttributeName=id,AttributeType=N \
        AttributeName=title,AttributeType=S \
	--key-schema AttributeName=id,KeyType=HASH \
	             AttributeName=title,KeyType=RANGE \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --region=eu-west-1 \
    --endpoint=http://localhost:8000

# Set to foreground, because the container exits after creating the table
fg