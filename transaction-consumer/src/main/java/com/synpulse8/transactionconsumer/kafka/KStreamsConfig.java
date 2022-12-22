package com.synpulse8.transactionconsumer.kafka;


import com.synpulse8.transactionconsumer.model.Transaction;
import lombok.extern.java.Log;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;

import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.io.File;
import java.util.Properties;

import static org.apache.kafka.streams.StreamsConfig.*;

@Configuration
@Log
public class KStreamsConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.streams.applicationId}")
    private String appId;

    @Value(value = "${kafka.topics.topic.name}")
    private String topicName;

    @Value(value = "${kafka.streams.stateStoreName}")
    private String stateStoreName;

    private final ObjectFactory<KStreamsProcessor> TransactionsStreamsProcessorObjectFactory;

    private final Deserializer<String> keyDes = new StringDeserializer();

    private final Deserializer<Transaction> valueDes =
            new JsonDeserializer<>(Transaction.class).ignoreTypeHeaders();

    private final Serde<String> keySer = Serdes.String();

    private final Serde<Transaction> valueSer = new JsonSerde<>(Transaction.class).ignoreTypeHeaders();

    public KStreamsConfig(ObjectFactory<KStreamsProcessor> TransactionsStreamsProcessorObjectFactory) {
        this.TransactionsStreamsProcessorObjectFactory = TransactionsStreamsProcessorObjectFactory;
    }

    public KStreamsProcessor getTransactionsStreamsProcessor() {
        return TransactionsStreamsProcessorObjectFactory.getObject();
    }

    private Properties createConfigProps() {


        //was facing problems with file write permissions
        File file = new File(".\\temp");
        file.setWritable(true);
        file.setReadable(true);
        file.setExecutable(true);

        final Properties props = new Properties();
        props.put(StreamsConfig.STATE_DIR_CONFIG, ".\\temp");
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(APPLICATION_ID_CONFIG, appId);
        props.put(DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);

        return props;
    }


    @Bean
    @Primary
    public KafkaStreams kafkaStreams() {
        log.info("Defining topology for kafka stream");
        Topology topology = this.buildTopology(new StreamsBuilder());
        final KafkaStreams kafkaStreams = new KafkaStreams(topology, createConfigProps());
        kafkaStreams.start();
        return kafkaStreams;
    }

    public Topology buildTopology(StreamsBuilder streamsBuilder) {
        Topology topology = streamsBuilder.build();

        StoreBuilder<KeyValueStore<String, Transaction>> stateStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(stateStoreName), keySer, valueSer);

        topology.addSource("Source", keyDes, valueDes, topicName)
                .addProcessor("Process", this::getTransactionsStreamsProcessor, "Source")
                .addStateStore(stateStoreBuilder, "Process");
        return topology;
    }



}

