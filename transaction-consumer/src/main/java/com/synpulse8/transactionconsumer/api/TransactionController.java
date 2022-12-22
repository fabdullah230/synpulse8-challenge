package com.synpulse8.transactionconsumer.api;


import com.synpulse8.transactionconsumer.model.ErrorRes;
import com.synpulse8.transactionconsumer.model.Query;
import com.synpulse8.transactionconsumer.model.Transaction;
import com.synpulse8.transactionconsumer.service.TransactionService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Log
public class TransactionController {
	@Autowired
	TransactionService transactionService;

	@PostMapping
	@ResponseBody
	public ResponseEntity<Object> createSettlementMessage(@RequestBody Query query){
		log.info("Accessing Transaction Controller");
		try {
			List<Transaction> transactionList =  transactionService.fetchCustomerTransactions(query);
			return new ResponseEntity<>(transactionList, HttpStatus.OK);
		}
		catch (Exception e) {
			ErrorRes errorRes = new ErrorRes();
			errorRes.setMessage(e.getMessage());
			return new ResponseEntity<>(errorRes, HttpStatus.BAD_REQUEST);
		}
	} 
}
