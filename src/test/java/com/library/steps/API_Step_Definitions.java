package com.library.steps;

import com.library.utility.ConfigurationReader;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.*;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class API_Step_Definitions {
    RequestSpecification givenPart;
    ValidatableResponse thenPart;
    String pathParam;

    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {
        givenPart = given().header("x-library-token", LibraryAPI_Util.getToken(userType));
    }

    @Given("Accept header is {string}")
    public void accept_header_is(String acceptType) {
        givenPart.accept(acceptType);
    }

    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {

        thenPart =
                givenPart.when().get(ConfigurationReader.getProperty("library.baseUri") + endpoint).then();
    }

    @Then("status code should be {int}")
    public void status_code_should_be(Integer statusCode) {
        thenPart.statusCode(statusCode);
    }

    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        thenPart.contentType(contentType);
    }

    @Then("{string} field should not be null")
    public void field_should_not_be_null(String field) {
        thenPart.body(field, everyItem(is(notNullValue())));
    }

    @Given("Path param is {string}")
    public void path_param_is(String pathParam) {
        this.pathParam = pathParam;
        givenPart.pathParam("id", pathParam);
    }

    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String actualField) {
thenPart.body(actualField, is(equalTo(pathParam)));
    }

    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> fieldList) {
        for (String eachField : fieldList) {
            thenPart.body(eachField, is(notNullValue()));
        }
    }


}
