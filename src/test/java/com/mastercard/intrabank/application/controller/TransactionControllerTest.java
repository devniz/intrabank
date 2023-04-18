package com.mastercard.intrabank.application.controller;


import static io.restassured.RestAssured.given;

import java.math.BigDecimal;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mastercard.intrabank.domain.model.TransactionRequest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class TransactionControllerTest {

    @AfterEach
    public void tearDown() {
        RestAssured.reset();
    }

    @Test
    public void givenInvalidValidAccountId_WhenRequestSend_ThenItShouldReturn404() {
        given().
                baseUri("http://localhost:8080")
                .contentType(ContentType.JSON)
                .when()
                .get("/transaction/999/send")
                .then()
                .log().all()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void givenValidAccountIdWithInsufficientFunds_WhenRequestSend_ThenItShouldReturn401() {
        given().
                baseUri("http://localhost:8080")
                .contentType(ContentType.JSON)
                .when()
                .with().body(TransactionRequest.builder()
                        .senderAccountId("111")
                        .receiverAccountId("112")
                        .amount(BigDecimal.valueOf(250f))
                        .build())
                .then()
                .log().all()
                .statusCode(401);
    }

}