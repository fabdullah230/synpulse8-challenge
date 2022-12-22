package com.synpulse8.transactionproducer.kafka;


import com.synpulse8.transactionproducer.payload.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProducer.class);

    private KafkaTemplate<String, Transaction> kafkaTemplate;

    public TransactionProducer(KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Transaction transaction){

        LOGGER.info(String.format("Message sent -> %s", transaction.toString()));

        //Creating unique transaction id
        String uniqueId = UUID.randomUUID().toString();
        System.out.println(uniqueId);

        transaction.setTransactionIdentifier(uniqueId);

        Message<Transaction> message = MessageBuilder
                .withPayload(transaction)
                .setHeader(KafkaHeaders.KEY, uniqueId)
                .setHeader(KafkaHeaders.TOPIC, "transactiontopic")
                .build();

        kafkaTemplate.send(message);
    }
}
