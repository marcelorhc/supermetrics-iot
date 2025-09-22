package com.supermetrics.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class SimulatorControllerTest extends BaseControllerTest {

    @Test
    void turnOn_shouldStartSimulator() {
        givenUserAuth()
            .when()
                .post("/simulator/on")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Simulator is ON"));
    }

    @Test
    void turnOff_shouldStopSimulator() {
        givenUserAuth()
            .when()
                .post("/simulator/on");

        givenUserAuth()
            .when()
                .post("/simulator/off")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Simulator is OFF"));
    }

    @Test
    void endpoints_shouldRequireAuthentication() {
        given()
            .when()
                .post("/simulator/on")
            .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());

        given()
            .when()
                .post("/simulator/off")
            .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
