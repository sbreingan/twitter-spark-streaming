package spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.OutputMode;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.streaming.Trigger;
import twitter4j.Status;
import twitter4j.TwitterObjectFactory;

import static org.apache.spark.sql.functions.col;

public class StructuredStreamRunner {

    public static void main(String [] args) throws Exception{
        
        Logger.getRootLogger().setLevel(Level.WARN);
        startReadingTweets();
    }

    private static void startReadingTweets() throws StreamingQueryException {
        
        Dataset<Row> dataset = createKafkaDataset().select(col("data").cast("string"));

        Dataset<String> statusJSON = dataset.as(Encoders.STRING());

        Dataset<Status> statusDS = statusJSON
                .map(json -> TwitterObjectFactory.createStatus(json), Encoders.javaSerialization(Status.class))
                .filter(status -> status.getText() != null);

        Dataset<TwitterBean> tweetDS = statusDS
                .map(status -> TwitterBean.createTwitterBean(status), Encoders.bean(TwitterBean.class))
                .filter(x -> x.getLanguage().equals("en"));

        tweetDS.printSchema();

        tweetDS.writeStream().format("console")
                .option("truncate", false)
                .outputMode(OutputMode.Append())
                .trigger(Trigger.ProcessingTime("10 seconds"))
                .start()
                .awaitTermination();
    }

    private static Dataset<Row> createKafkaDataset() {

        SparkSession spark = SparkSession
                .builder()
                .master("local")
                .appName("SparkTwitterApp")
                .getOrCreate();
        
        return spark.readStream().format("kafka")
                .option("kafka.bootstrap.servers", "localhost:9092")
                .option("subscribe", "twitter-topic")
                .load();
    }

}
