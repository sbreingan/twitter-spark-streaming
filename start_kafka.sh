#!/bin/sh

docker run -d -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=localhost --env ADVERTISED_PORT=9092 --name kafka spotify/kafka
mvn -f kafkatwitter clean

mvn -f kafkatwitter package

java -jar kafkatwitter/target/kafka-twitter-1.0-SNAPSHOT-jar-with-dependencies.jar
