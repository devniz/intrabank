![mastercard image](https://www.investopedia.com/thmb/mwHW7XCk-VHMUdAIOPHvuOsMQAU=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/522421671-5bfc38d946e0fb00517f989a.jpg)

# Mastercard Backend Test

Design and implement a RESTful API (including data model and the backing implementation) for
money transfers between accounts.

### Dependencies:

- Java Version: v17
- Framework: Spring Boot.
- Mockito: Mock all my services.
- RestAssured: Testing REST endpoints.

### How to test?

- Inside Tests folder, you'll find interbank-collection.json file with all necessary endpoint calls to import into
  Postman.

The REST API is accepting those calls:

- /GET localhost:8080/accounts/111/balance: Return the balance of the account if found.
- /GET localhost:8080/accounts/112/statements/mini: Return last transaction from given account.
- /POST localhost:8080/transaction/send: Send money to another account.

#### Or simply run CURL requests from here:

```BASH
curl --location 'localhost:8080/accounts/111/balance' \
--header 'Content-Type: application/json'
```

```BASH
curl --location 'localhost:8080/accounts/112/statements/mini?=null' \
--header 'Content-Type: application/json'
```

```BASH
curl --location 'localhost:8080/transaction/send' \
--header 'Content-Type: application/json' \
--data '{
    "senderAccountId":"111",
    "receiverAccountId": "112",
    "amount": 10,
    "transactionType": "DEBIT"
}'
```

### Technical note.

- For a banking service, it is not acceptable to have unsafe and unpredictable transactions across multiple distributed
  systems.

- As a basic workaround, the method called ``dotransfer`` is being marked as **synchronised**, since we would like to
  perform one transfer at a time.

- I assume in production environment we would want to have some optimistic/pessimistic strategy on the database layer as
  well to decrease or increase accounts accordingly.

- Having OpenAPI documentation and perhaps contracting testing for a production grade API will be necessary.

### Contact

- Email: nizar.bousebsi@gmail.com
- Phone: 07 562 923 669