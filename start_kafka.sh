#!/bin/sh

if [ ! "$(docker ps -q -f name=kafka_twitter)" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=kafka_twitter)" ]; then
        # cleanup
        docker rm kafka_twitter
    fi
    # run your container
    docker run -d -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=localhost --env ADVERTISED_PORT=9092 --name kafka_twitter spotify/kafka
fi

mvn -f kafka_twitter clean

mvn -f kafka_twitter package

echo Twitter search terms: "$@" 

java -jar kafka_twitter/target/kafka-twitter-1.0-SNAPSHOT-jar-with-dependencies.jar "$@"
