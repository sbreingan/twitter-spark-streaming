package spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.OutputMode;
import org.apache.spark.sql.streaming.Trigger;
import twitter4j.Status;
import twitter4j.TwitterObjectFactory;

import static org.apache.spark.sql.functions.col;

public class StructuredStreamRunner {

    public static void main(String [] args) throws Exception{

        SparkSession spark = SparkSession
                .builder()
                .master("local")
                .appName("SparkTwitterApp")
                .getOrCreate();

        Logger.getRootLogger().setLevel(Level.WARN);

        // Create DataFrame representing the stream of input lines from connection to host:port
        Dataset<Row> dataset = spark.readStream().format("kafka")
                .option("kafka.bootstrap.servers", "localhost:9092")
                .option("subscribe", "twitter-topic")
                .load()
                .select(col("value").cast("string"));

        Dataset<String> statusString = dataset.as(Encoders.STRING());
        Dataset<Status> status = statusString.map(x -> TwitterObjectFactory.createStatus(x), Encoders.javaSerialization(Status.class))
                .filter(x -> x.getText() != null);

        Dataset<TwitterBean> tweets = status.map(x -> TwitterBean.createTwitterBean(x), Encoders.bean(TwitterBean.class))
                .filter(x -> x.getLanguage().equals("en"));


        status.printSchema();
        tweets.writeStream().format("console").option("truncate", false)
                .outputMode(OutputMode.Append())
                .trigger(Trigger.ProcessingTime("10 seconds"))
                .start()
                .awaitTermination();

    }

}
