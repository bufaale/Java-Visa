package com.petstore.tests;

import io.restassured.RestAssured;
import com.petstore.utils.ConfigReader;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import io.restassured.specification.RequestSpecification;


/**
 * Abstract base test class that sets up and cleans up the RestAssured configuration.
 */
public abstract class TestBase {

    protected RequestSpecification requestSpec;

    @BeforeClass
    public void setup() {
        // Set the base URI and enable logging of request and response
        RestAssured.baseURI = ConfigReader.get("BASE_URL");
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // Initialize the request specification with default headers
        requestSpec = RestAssured.given()
                .contentType("application/json")
                .header("Accept", "application/json");

    }

    @AfterMethod
    public void cleanup(ITestResult result) {
        // Reset the request specification to its initial state after each test method
        requestSpec = RestAssured.given()
                .contentType("application/json")
                .header("Accept", "application/json");

    }
}
