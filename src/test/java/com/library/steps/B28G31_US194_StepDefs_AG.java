package com.library.steps;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import com.library.pages.LoginPage;
import com.library.utility.ConfigurationReader;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class B28G31_US194_StepDefs_AG {
    RequestSpecification givenPart;
    Response response;
    ValidatableResponse thenPart;
    

    @Given("I logged Library api as a {string}-AG")
    public void i_logged_library_api_as_a_ag(String userType) {

        givenPart = given().log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken(userType));

    }

    @Given("Accept header is {string}-AG")
    public void accept_header_is_ag(String contentType) {

        givenPart.accept(contentType);

    }

    @When("I send GET request to {string} endpoint-AG")
    public void i_send_get_request_to_endpoint_ag(String endpoint) {

        response = givenPart.when().get(ConfigurationReader.getProperty("library.baseUri") + endpoint).prettyPeek();
        thenPart = response.then();

    }

    @Then("status code should be {int}-AG")
    public void status_code_should_be_ag(Integer statusCode) {
        thenPart.statusCode(statusCode);

    }

    @Then("Response Content type is {string}-AG")
    public void response_content_type_is_ag(String contentType) {
        thenPart.contentType(contentType);
    }

    @Then("{string} field should not be null-AG")
    public void field_should_not_be_null_ag(String path) {

        thenPart.body(path, everyItem(notNullValue()));

    }

}
