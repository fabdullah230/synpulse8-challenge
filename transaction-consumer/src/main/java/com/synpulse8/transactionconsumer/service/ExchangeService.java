package com.synpulse8.transactionconsumer.service;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.synpulse8.transactionconsumer.model.Transaction;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


@Service
public class ExchangeService {

    public JsonObject getRates() throws IOException {

        String url_str = "https://v6.exchangerate-api.com/v6/ebc42a324815d708ecae95d5/latest/USD";

        URL url = new URL(url_str);
        HttpURLConnection requestNew = (HttpURLConnection) url.openConnection();
        requestNew.connect();

        // Convert to JSON
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) requestNew.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        JsonObject rates = jsonobj.get("conversion_rates").getAsJsonObject();
        return rates;
    }

    public List<Transaction> convertTransactions(List<Transaction> transactions) throws IOException {

        JsonObject rates = getRates();

        for (Transaction t : transactions) {
            String cur = t.getAmount().substring(0, 3).toUpperCase();
            if (cur == "USD") {
                continue;
            }

            float amt = Float.parseFloat(t.getAmount().substring(4));
            float rate = rates.get(cur).getAsFloat();
            float convertedToUSD = amt/rate;

            String newAmt = "USD " + convertedToUSD;
            t.setAmount(newAmt);
        }
        return transactions;
    }








}
