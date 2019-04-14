package com.holidu.assignment.interview.controllers;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.Matchers.containsString;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.holidu.interview.assignment.App;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = App.class)
public class TreeControllerTest {

    @LocalServerPort
    protected int port;

    @Before
    public void beforeTest() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    public void getTreeCounts() {
        //@formatter:off
    	ValidatableResponse result = given()
            .contentType(ContentType.JSON)
            .param("x", "913949")
            .param("y", "120317")
            .param("radius", "50")
        .when()
            .get("trees")
        .then();
    	
        result.statusCode(HttpStatus.SC_OK);
        result.assertThat().body(containsString("trees"));

        //@formatter:on

    }
    
    @Test
    public void sendInvalidRadius() {
        //@formatter:off
    	ValidatableResponse result = given()
            .contentType(ContentType.JSON)
            .param("x", "913949")
            .param("y", "120317")
            .param("radius", "-50")
        .when()
            .get("trees")
        .then();
    	
        result.statusCode(HttpStatus.SC_BAD_REQUEST);
        result.assertThat().body("status", Matchers.equalTo("BAD_REQUEST"));
        result.assertThat().body("errors[0]", Matchers.equalTo("Radius value is invalid"));

        //@formatter:on

    }
}
