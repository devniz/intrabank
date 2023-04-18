package com.mastercard.intrabank.application.controller;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mastercard.intrabank.domain.model.TransactionRequest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AccountControllerTest {

    @AfterEach
    public void tearDown() {
        RestAssured.reset();
    }

    @Test
    public void givenAValidAccountId_WhenRequestAccountBalance_ThenItShouldReturnAccountResponseBalanceObject() {
        given().
                baseUri("http://localhost:8080")
                .contentType(ContentType.JSON)
                .when()
                .get("/accounts/111/balance")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body(
                        "accountId", equalTo("111"),
                        "balance", equalTo(100f),
                        "currency", equalTo("GBP")
                );
    }

    @Test
    public void givenValidAccountId_Sending100_ThenItShouldShowCorrectNewBalance() {
        given().
                baseUri("http://localhost:8080")
                .contentType(ContentType.JSON)
                .with().body(TransactionRequest.builder()
                        .senderAccountId("111")
                        .receiverAccountId("112")
                        .amount(BigDecimal.valueOf(5.00f))
                        .build())
                .when()
                .request("POST", "/transaction/send")
                .then()
                .statusCode(200)
                .extract().response();

        given().
                baseUri("http://localhost:8080")
                .contentType(ContentType.JSON)
                .when()
                .get("/accounts/111/balance")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body(
                        "accountId", equalTo("111"),
                        "balance", equalTo(90.0f),
                        "currency", equalTo("GBP")
                );
    }

    @Test
    public void givenValidAccountId_Sending100_ThenItShouldGetMiniStatement() {
        given().
                baseUri("http://localhost:8080")
                .contentType(ContentType.JSON)
                .with().body(TransactionRequest.builder()
                        .senderAccountId("111")
                        .receiverAccountId("112")
                        .amount(BigDecimal.valueOf(5.00f))
                        .build())
                .when()
                .request("POST", "/transaction/send")
                .then()
                .statusCode(200)
                .extract().response();

        given().
                baseUri("http://localhost:8080")
                .contentType(ContentType.JSON)
                .when()
                .get("/accounts/111/statements/mini")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body(
                        "accountId", hasItem("111"),
                        "amount", hasItem(5.00f),
                        "currency", hasItem("GBP"),
                        "transactionType", hasItem("DEBIT")
                );
    }

    @Test
    public void givenInvalidValidAccountId_WhenRequestAccountBalance_ThenItShouldReturn404() {
        given().
                baseUri("http://localhost:8080")
                .contentType(ContentType.JSON)
                .when()
                .get("/accounts/999/balance")
                .then()
                .log().all()
                .assertThat()
                .statusCode(404);
    }

}