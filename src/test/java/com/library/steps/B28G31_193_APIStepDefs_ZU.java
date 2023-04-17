package com.library.steps;

import com.library.utility.ConfigurationReader;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class B28G31_193_APIStepDefs_ZU {

    RequestSpecification givenPart;

    Response response;
    ValidatableResponse thenPart;
    String token;
    @Given("I logged Library api with credentials {string} and {string} - ZU")
    public void i_logged_library_api_with_credentials_and_zu(String email, String password) {
        token = LibraryAPI_Util.getToken(email, password);
        givenPart = given().log().uri()
                .header("x-library-token", token);
    }
    @Given("Accept header is {string} - ZU")
    public void accept_header_is_zu(String acceptType) {
        givenPart.accept(acceptType);

    }
    @Given("Request Content Type header is {string} - ZU")
    public void request_content_type_header_is_zu(String contentType) {
        givenPart.contentType(contentType);
    }
    @Given("I send token information as request body - ZU")
    public void i_send_token_information_as_request_body_zu() {
        givenPart.formParam("token",token);
    }
    @When("I send POST request to {string} endpoint - ZU")
    public void i_send_post_request_to_endpoint_zu(String endpoint) {
        response =
                givenPart.when().post(ConfigurationReader.getProperty("library.baseUri") + endpoint);
        thenPart = response.then();

    }
    @Then("status code should be {int} - ZU")
    public void status_code_should_be_zu(Integer statusCode) {
        thenPart.statusCode(statusCode);
    }
    @Then("Response Content type is {string} - ZU")
    public void response_content_type_is_zu(String contentType) {
        thenPart.contentType(contentType);
    }
    @Then("the field value for {string} path should be equal to {string} - ZU")
    public void the_field_value_for_path_should_be_equal_to_zu(String path, String expected) {
        thenPart.body(path, is(equalTo(expected)));
    }
    @Then("{string} field should not be null - ZU")
    public void field_should_not_be_null_zu(String path) {
        thenPart.body(path, (notNullValue()));
    }
}
