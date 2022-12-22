package com.synpulse8.transactionconsumer.service;


import com.synpulse8.transactionconsumer.model.Query;
import com.synpulse8.transactionconsumer.model.Transaction;
import lombok.extern.java.Log;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Log
public class TransactionService {

	@Autowired
	ExchangeService exchangeService;
	
	private final KafkaStreams kafkaStreams;

    @Value(value = "${kafka.streams.stateStoreName}")
    private String stateStoreName;

    @Autowired
    public TransactionService(KafkaStreams kafkaStreams) {
        this.kafkaStreams = kafkaStreams;
    }
    

	public List<Transaction> fetchCustomerTransactions(Query query) throws IOException {

		int month = query.getMonth();
		int year = query.getYear();


		log.info("Accessing TransactionService");
		List<Transaction> list =  new ArrayList<Transaction>();
		KeyValueIterator<String, Transaction> iterator = getStore().all();
		while(iterator.hasNext()) {
			Transaction temp = iterator.next().value;
			if(temp.getValueDate().contains(month +"-"+ year)){
				list.add(temp);
			}

		}
		iterator.close();
		list = exchangeService.convertTransactions(list);

		return list;
	}
	
	private ReadOnlyKeyValueStore<String, Transaction> getStore() {
		
		return kafkaStreams.store(StoreQueryParameters.fromNameAndType(stateStoreName, QueryableStoreTypes.keyValueStore()));
	}

}
