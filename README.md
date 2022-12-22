Synpulse Backend Engineer Challenge

How to run:
1.  Clone the repo from your IDE (preferably IntelliJ IDEA)
2. For both the consumer and producer applications, right click their respective pom.xml and click “add as Maven project”
3. Make sure that Zookeeper is running on port 2181 and Kafka on port 9092
4. Run the ProducerApplication, then run the ChallengeApplication

Project Architecture

Blue arrows represent program flow for creating transactions. Red arrows represent flow for retrieving transactions.
![alt text](https://github.com/fabdullah230/synpulse8-challenge/blob/master/flow.png?raw=true)

Creating Transactions

Send a POST request to the route “/api/publish” of the ProducerApplication app. The payload should be a raw JSON body with the following fields.

{  
&emsp;“transactionIdentifier” : String,    //Can be anything, since the application will auto generate a UUID for this field  
&emsp;“accountIBAN” : String,  
&emsp;“amount” : String,   //First 3 letters should be a valid currency code, followed by a space, then a positive (no sign) or a negative (-) number  
&emsp;“description” : String,  
&emsp;“valueDate” : String    //dd-MM-yyyy format only  
}

Expected Response

200 OK, “Json transaction sent to kafka topic”

Retrieving Transactions

Authentication:

Send a POST request to the route “/api/auth” of the ChallengeApplication app. The payload should be a raw JSON body with following credentials.

{  
&emsp;“username” : “testUser”,  
&emsp;“password” : “password”  
}

The expected api response should be 200 OK in the following format.

{  
&emsp;“token” : “ey…..” //  JWT token  
}

Query:

Send a POST request to the route “/api/transactions” of the ChallengeApplication app. The payload should be a raw JSON body with the following fields.

{  
&emsp;“month” : int  //MM  
&emsp;“year” : int  //yyyy  
}

The expected api response should be 200 OK in the following format.

[  
{  
&emsp;"transactionIdentifier": "5d1a2004-d5af-48a7-8f84-0443042e5b47",
&emsp;"accountIBAN": "MYR3-0000-1111-2222-3333",
&emsp;"amount": "USD -22534.197",
&emsp;"description": "ATM MYR WITHDRAWAL",
&emsp;"valueDate": "22-09-2020"  
},  
{  
&emsp;"transactionIdentifier": "ea4d5f72-a767-434b-bcac-2a2f84981ac0",
&emsp;"accountIBAN": "THB3-0000-1111-2222-3333",
&emsp;"amount": "USD -2882.808",
&emsp;"description": "ATM THB WITHDRAWAL",
&emsp;"valueDate": "22-09-2020"  
}  
]

