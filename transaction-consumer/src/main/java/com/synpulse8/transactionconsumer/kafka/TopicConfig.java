package com.synpulse8.transactionconsumer.kafka;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TopicConfig {

    private String topicName = "transactionstopic";

    @Bean
    public NewTopic transactionsTopic(){
        return TopicBuilder.name(topicName).build();
    }

}
