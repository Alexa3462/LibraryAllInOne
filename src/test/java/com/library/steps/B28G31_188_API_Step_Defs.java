package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.ConfigurationReader;
import com.library.utility.DB_Util;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;


import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


public class B28G31_188_API_Step_Defs {
    RequestSpecification givenPart;
    Response response;
    ValidatableResponse thenPart;
    Map<String, Object> randomUserMap;
    String userID, expectedName, expectedPassword, expectedEmail, expectedStatus, expectedAddress;
    int expectedUserGroupId;


    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {
        givenPart = given().log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken(userType));
    }

    @Given("Accept header is {string}")
    public void accept_header_is(String acceptType) {
        givenPart.accept(acceptType);
    }

    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String contentType) {
        givenPart.contentType(contentType);
    }

    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String user) {
        randomUserMap = LibraryAPI_Util.getRandomUserMap();
        givenPart.formParams(randomUserMap);
        expectedName = (String) randomUserMap.get("full_name");
        expectedPassword = (String) randomUserMap.get("password");
        expectedEmail = (String) randomUserMap.get("email");
        expectedUserGroupId = (Integer) randomUserMap.get("user_group_id");
        expectedStatus = (String) randomUserMap.get("status");
        expectedAddress = (String) randomUserMap.get("address");
    }

    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endpoint) {
        response =
                givenPart.when().post(ConfigurationReader.getProperty("library.baseUri") + endpoint);
        thenPart = response.then();
    }

    @Then("status code should be {int}")
    public void status_code_should_be(Integer statusCode) {
        thenPart.statusCode(statusCode);
    }

    @Then("Response Content type is {string}")
    public void response_content_type_is(String contentType) {
        thenPart.contentType(contentType);
    }

    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String path, String expected) {
        thenPart.body(path, is(equalTo(expected)));
    }

    @Then("{string} field should not be null")
    public void field_should_not_be_null(String path) {
        thenPart.body(path, is(notNullValue()));
        userID = response.jsonPath().getString(path);
    }

    @Then("created user information should match with Database")
    public void created_user_information_should_match_with_database() {
        String query = "select * from users where id = " + userID;
        DB_Util.runQuery(query);
        Map<String, Object> rowMap = DB_Util.getRowMap(1);


        String actualNameDB = (String) rowMap.get("full_name");
        String actualEmailDB = (String) rowMap.get("email");
        String actualUserGroupIdDB = (String) rowMap.get("user_group_id");
        String actualStatusDB = (String) rowMap.get("status");
        String actualAddressDB = (String) rowMap.get("address");

        assertEquals(expectedName,actualNameDB);
        assertEquals(expectedEmail,actualEmailDB);
        assertEquals(expectedUserGroupId,Integer.parseInt(actualUserGroupIdDB));
        assertEquals(expectedStatus,actualStatusDB);
        assertEquals(expectedAddress,actualAddressDB);

    }

    @Then("created user should be able to login Library UI")
    public void created_user_should_be_able_to_login_library_ui() {
        new LoginPage().login(expectedEmail,expectedPassword);
    }

    @Then("created user name should appear in Dashboard Page")
    public void created_user_name_should_appear_in_dashboard_page() {
        BrowserUtil.waitForVisibility(new BookPage().accountHolderName,5);
        String actualNameUI = new BookPage().accountHolderName.getText();
        assertEquals(expectedName,actualNameUI);

    }
}
