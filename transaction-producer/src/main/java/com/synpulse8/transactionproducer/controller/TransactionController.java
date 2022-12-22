package com.synpulse8.transactionproducer.controller;

import com.synpulse8.transactionproducer.kafka.TransactionProducer;
import com.synpulse8.transactionproducer.payload.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private TransactionProducer kafkaProducer;

    public TransactionController(TransactionProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> publish(@RequestBody Transaction transaction){
        kafkaProducer.sendMessage(transaction);
        return ResponseEntity.ok("Json transaction sent to kafka topic");
    }
}
