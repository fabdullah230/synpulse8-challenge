package com.synpulse8.transactionconsumer.kafka;


import com.synpulse8.transactionconsumer.model.Transaction;
import lombok.extern.java.Log;

import org.apache.kafka.streams.processor.ProcessorContext;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@Log
public class KStreamsProcessor implements Processor<String, Transaction> {

    private KeyValueStore<String, Transaction> stateStore;

    @Value(value = "${kafka.streams.stateStoreName}")
    private String stateStoreName;

    @Override
    public void init(ProcessorContext context) {
        stateStore =  context.getStateStore(stateStoreName);
        Objects.requireNonNull(stateStore, "State store cannot be null");
    }

    @Override
    public void process(String transactionId, Transaction transactions) {
        log.info("Incoming request to save transaction -> " + transactions);
        stateStore.put(transactionId, transactions);
    }

    @Override
    public void close() {}
}
