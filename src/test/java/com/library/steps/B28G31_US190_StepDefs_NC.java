package com.library.steps;

import com.library.utility.ConfigurationReader;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class B28G31_US190_StepDefs_NC {
    String userType;
    String headerType;
    String endPoint;
    int int1;
    String contentType;
    RequestSpecification request;
    @Given("I logged Library api as a {string}-NC")
    public void i_logged_library_api_as_a_nc(String userType) {
    request = given().log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken(userType));

             this.userType = userType;

    }

    @Given("Accept header is {string}-NC")
    public void accept_header_is_nc(String headerType) {

        this.headerType = headerType;

        request.accept(headerType);

    }
    @When("I send GET request to {string} endpoint-NC")
    public void i_send_get_request_to_endpoint_nc(String endPoint) {

                this.endPoint = endPoint;

                      request.accept(headerType)
                       .when().get(ConfigurationReader.getProperty("library.baseUri") + endPoint);

    }
    @Then("status code should be {int}-NC")
    public void status_code_should_be_nc(Integer int1) {

        this.int1 = int1;

         request.accept(headerType)
                 .when().get(ConfigurationReader.getProperty("library.baseUri") + endPoint)
                .then().statusCode(int1);


    }
    @Then("Response Content type is {string}-NC")
    public void response_content_type_is_nc(String contentType ) {

        this.contentType = contentType;

        request.accept(headerType)
                .when().get(ConfigurationReader.getProperty("library.baseUri") + endPoint)
                .then().statusCode(int1).contentType(contentType);


    }
    @Then("{string} field should not be null-NC")
    public void field_should_not_be_null_nc(String field) {

        if (field.equalsIgnoreCase("id")) {

            request.accept(contentType)
                    .when().get(ConfigurationReader.getProperty("library.baseUri") + endPoint)
                    .then().statusCode(int1)
                    .contentType(contentType)
                    .body("id", is(notNullValue()));

        } else if (field.equalsIgnoreCase("name")){

            request.accept(contentType)
                    .when().get(ConfigurationReader.getProperty("library.baseUri") + endPoint)
                    .then().statusCode(int1)
                    .contentType(contentType)
                    .body("name", is(notNullValue()));
        }

    }

}
