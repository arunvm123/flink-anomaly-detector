# Flink-Anomaly-Detector

The Flink application detects anomaly based on the following rule - A user making more than 5 requests in 15 seconds is considered an anomaly.

## Pre-requisites

- Kafka 2.7.0
- Flink 1.16.0
- Golang (To run the script only)

## Steps to run locally

- Start Flink cluster ```./bin/start-cluster.sh```
- Flink UI available at http://localhost:8081/#/overview
- Build project with maven ```mvn clean package```
- Submit jar to local cluster ```./bin/flink run /PATH/TO/JAR/sample-1.0-SNAPSHOT.jar```

At this point you will see a message saying the job has been successfully submitted, if there are no issues. You will be 
able to view more information on the Flink UI.

## Script

TODO