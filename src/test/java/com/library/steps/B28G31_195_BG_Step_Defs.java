package com.library.steps;

import com.library.utility.*;
import io.cucumber.java.en.*;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class B28G31_195_BG_Step_Defs {

    RequestSpecification givenPart;
    ValidatableResponse thenPart;
    String pathParamValue;

    @Given("I logged Library api as a {string} - BG")
    public void i_logged_library_api_as_a_bg(String userType) {
        givenPart = given().log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken(userType));}
    @Given("Accept header is {string} - BG")
    public void accept_header_is_bg(String contentType) {
        givenPart.accept(contentType);}
    @Given("Path param is {string} - BG")
    public void path_param_is_bg(String pathParamValue) {
        this.pathParamValue=pathParamValue;
        givenPart.pathParam("id", pathParamValue);}
    @When("I send GET request to {string} endpoint - BG")
    public void i_send_get_request_to_endpoint_bg(String endPoint) {
        thenPart = givenPart.when().get(ConfigurationReader.getProperty("library.baseUri") + endPoint).prettyPeek().then();}
    @Then("status code should be {int} - BG")
    public void status_code_should_be_bg(Integer statusCode) {
        thenPart.statusCode(statusCode);}
    @Then("Response Content type is {string} - BG")
    public void response_content_type_is_bg(String contentType) {
        thenPart.contentType(contentType);}
    @Then("{string} field should be same with path param - BG")
    public void field_should_be_same_with_path_param_bg(String string) {
        thenPart.body(string,is(pathParamValue));}
    @Then("following fields should not be null - BG")
    public void following_fields_should_not_be_null_bg(List<String> list) {
        for (String each : list) {
            thenPart.body(each,notNullValue());}
    }
}
