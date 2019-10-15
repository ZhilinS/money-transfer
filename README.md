# Simple money transfer system


To run app from cli you'll need `java` at least 1.8, `maven` 3.5.0.

To start server, put this in cli from the project root: 
```
mvn clean package && java -jar target/target/transfer-1.0-jar-with-dependencies.jar
```
It'll start on `localhost:8080`.

It creates DB file in folder, from which server was started.
Don't miss it.


Concurrent bank imitation with REST endpoints such as
```
GET /api/account/           - returns all accounts
GET /api/account/:id        - returns particular account
POST /api/account/          - creates new account
body: {"name": "String", "balance": int}

POST /api/account/withdraw  - withdraws money from account
body: {"account": int, "amount": float}

POST /api/account/deposit   - deposits money to account
body: {"account": int, "amount": float}
```

Uses in-memory SQLite db separated for testing and production purposes. 

Floats used widely because there is a problem mapping
from FLOAT SQLite type to java double.


### Testing
Tests are running on top of their own web server and db instance.

To run tests simply put this in console (from project root):
```
mvn clean test
```
