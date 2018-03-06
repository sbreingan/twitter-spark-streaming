import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.twitter.hbc.core.endpoint.StatusesSampleEndpoint;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaTwitterProducer {


    private final static String TOPIC = "twitter-topic";
    private final static String BOOTSTRAP_SERVERS = "localhost:9092";

    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;


    private Producer<String, String> createProducer() {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<String,String>(props);

    }

    public void loadTwitterAuthProperties() throws IOException{

        Properties prop = new Properties();
        InputStream in = this.getClass().getResourceAsStream("twitterauth.properties");
        prop.load(in);
        in.close();

        consumerKey = prop.getProperty("consumerKey");
        consumerSecret = prop.getProperty("consumerSecret");
        accessToken = prop.getProperty("accessToken");
        accessTokenSecret = prop.getProperty("accessTokenSecret");
    }

    public void run(String[] args) throws InterruptedException {

        Producer<String, String> producer = createProducer();

        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);

        StatusesSampleEndpoint endpointSample = new StatusesSampleEndpoint();
        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        // add some track terms
        endpoint.trackTerms(Lists.newArrayList(args));

        Authentication auth = new OAuth1(consumerKey, consumerSecret, accessToken, accessTokenSecret);

        // Create a new BasicClient. By default gzip is enabled.
        Client client = new ClientBuilder().hosts(Constants.STREAM_HOST)
                .endpoint(endpoint)
                .authentication(auth)
                .processor(new StringDelimitedProcessor(queue)).build();

        // Establish a connection
        client.connect();

        // Do whatever needs to be done with messages
        while(true){
            ProducerRecord<String, String> message = null;
            try {
                message = new ProducerRecord<String, String>(TOPIC, queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
                producer.close();
                client.stop();
            }
            producer.send(message);
        }
    }

    public static void main(String[] args) {

        try {
            KafkaTwitterProducer ke = new KafkaTwitterProducer();
            ke.loadTwitterAuthProperties();
            ke.run(args);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
