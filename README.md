# Reading Twitter with Kafka and Spark Structured Streaming

The aim of this project is to experiment with the Apache Spark Structure Streaming API using Twitter as the source and Kafka as an intermediatary. 

It is composed of three parts:

- Kafka Docker Image
A [docker image](https://hub.docker.com/r/spotify/kafka/) is used to start Kafka and Zookeeper in a container on localhost and running at port 9092.
- Kafka Producer using Twitter4j 
This connects to a Kafka broker with the topic `twitter-topic`. Using the Twitter4j library and [Hosebird](https://github.com/twitter/hbc), it connects to a Twitter stream, either on a set of provided keyword or on a sample of all tweets.
- Consumer using Apache Spark Structured Streaming
This uses Apache Spark's [Structured Streaming API](https://spark.apache.org/docs/latest/structured-streaming-programming-guide.html) to connect to the Kafka stream 

## Getting Started

### Create Twitter App Authentication

- Go to [Twitter Application Management](https://apps.twitter.com/) - (you will require a Twitter account). 
- Create a new application with any given name.
- Go to _Keys and Access Tokens_.
- Take a note of:
  - Consumer Key (API Key)
  - Consumer Secret (API Secret)
  - Access Token
  - Access Token Secret
- You may wish to set access level to _Read Only_.
- Update the file `twitterauth_template.properties` under `twitter-spark-streaming\kafkatwitter\src\main\resources` with the values noted, and rename to `twitterauth.properties`. 

### Start the Twitter Kafka Stream

From the main directory the Kafka Twitter stream can be start using the `start_kafka.sh` script. This will start a docker container (if it is not already running) on port `localhost:9092`. 

Without any arguments this will start reading a sample of all tweets from Twitter. You can include any number of terms as arguments after for the shell script and they will be used to filter tweets which match them. For example:

```
./start_kafka.sh brexit trump
```

### Consuming the Kafka Stream 

In the `spark-streaming` directory the `StructredStreamRunner.java` class is used for connecting to the running Kafka stream and reading it into a Spark Dataset. 

Additionally in this directory is `KafkaTestConsumer` which uses the standard Kafka Java library without Spark and is useful for debugging purposes to ensure that connection to the stream is valid. 



