package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.ConfigurationReader;
import com.library.utility.DB_Util;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

public class API_Step_Definitions {
    RequestSpecification givenPart;
    ValidatableResponse thenPart;
    String pathParam, token;
    Map<String, Object> randomDataMap;

    LoginPage loginPage;
    BookPage bookPage;


    //US1
    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String userType) {
        token = LibraryAPI_Util.getToken(userType);
        givenPart = given().header("x-library-token", token);
    }

    @Given("Accept header is {string}")
    public void accept_header_is(String acceptType) {
        givenPart.accept(acceptType);
    }

    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String getEndpoint) {
        thenPart =
                givenPart.when().get(ConfigurationReader.getProperty("library.baseUri") + getEndpoint).then();
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
        thenPart.body(field, is(notNullValue()));
    }

    //US2
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

    //US3
    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String contentTypeHeader) {
        givenPart.contentType(contentTypeHeader);
    }

    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String randomData) {
        Map<String, Object> requestBody = new LinkedHashMap<>();

        switch (randomData) {
            case "user":
                requestBody = LibraryAPI_Util.getRandomUserMap();
                break;
            case "book":
                requestBody = LibraryAPI_Util.getRandomBookMap();
                break;
            default:
                throw new RuntimeException("Unexpected value: " + randomData);
        }

        System.out.println("requestBody = " + requestBody);
        randomDataMap = requestBody;
        givenPart.formParams(requestBody);
    }

    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endpoint) {

        thenPart =
                givenPart.when().post(ConfigurationReader.getProperty("library.baseUri") + endpoint).then();

    }

    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String field, String expectedField) {
        thenPart.body(field, is(equalTo(expectedField)));
    }

    @Given("I logged in Library UI as {string}")
    public void i_logged_in_library_ui_as(String role) {
        loginPage = new LoginPage();
        loginPage.login(role);
        BrowserUtil.waitFor(1);
    }

    @Given("I navigate to {string} page")
    public void i_navigate_to_page(String moduleName) {
        bookPage = new BookPage();
        bookPage.navigateModule(moduleName);
        BrowserUtil.waitFor(1);
    }

    @Then("UI, Database and API created book information must match")
    public void ui_database_and_api_created_book_information_must_match() {
        BrowserUtil.waitFor(3);

        // API DATA --> Expected --> Since we added data from API
        Response apiData = given().log().uri().header("x-library-token", LibraryAPI_Util.getToken("librarian"))
                .pathParam("id", thenPart.extract().response().path("book_id"))
                .when().get(ConfigurationReader.getProperty("library.baseUri") + "/get_book_by_id/{id}").prettyPeek();

        JsonPath jp = apiData.jsonPath();
        System.out.println("--------- API DATA -------------");
        Map<String, Object> APIBook = new LinkedHashMap<>();
        APIBook.put("name", jp.getString("name"));
        APIBook.put("isbn", jp.getString("isbn"));
        APIBook.put("year", jp.getString("year"));
        APIBook.put("author", jp.getString("author"));
        APIBook.put("book_category_id", jp.getString("book_category_id"));
        APIBook.put("description", jp.getString("description"));
        System.out.println("APIBook = " + APIBook);

        // To find book in database we need ID information
        String bookID = jp.getString("id");


        // DB DATA  --> Actual --> DB needs to show data that we add through API

        DB_Util.runQuery("select * from books where id='" + bookID + "'");
        Map<String, Object> DBBook = DB_Util.getRowMap(1);
        System.out.println("--------- DB DATA -------------");
        // These fields are auto-generated so we need to remove
        DBBook.remove("added_date");
        DBBook.remove("id");

        System.out.println(DBBook);


        // UI DATA  --> Actual --> needs to show data that we add through API
        BookPage bookPage = new BookPage();
        // we need bookName to find in UI.Make sure book name is unique.
        // Normally ISBN should be unique for each book
        String bookName = (String) randomDataMap.get("name");
        System.out.println("bookName = " + bookName);
        // Find book in UI
        bookPage.search.sendKeys(bookName);
        BrowserUtil.waitFor(3);

        bookPage.editBook(bookName).click();
        BrowserUtil.waitFor(3);


        // Get book info
        String UIBookName = bookPage.bookName.getAttribute("value");
        String UIAuthorName = bookPage.author.getAttribute("value");
        String UIYear = bookPage.year.getAttribute("value");
        String UIIsbn = bookPage.isbn.getAttribute("value");
        String UIDesc = bookPage.description.getAttribute("value");

        // We don't have category name information in book page.
        // We only have id of category
        // with the help of category id we will find category name by running query
        // Find category as category_id
        String UIBookCategory = BrowserUtil.getSelectedOption(bookPage.categoryDropdown);
        DB_Util.runQuery("select id from book_categories where name='" + UIBookCategory + "'");
        String UICategoryId = DB_Util.getFirstRowFirstColumn();

        System.out.println("--------- UI DATA -------------");
        Map<String, Object> UIBook = new LinkedHashMap<>();
        UIBook.put("name", UIBookName);
        UIBook.put("isbn", UIIsbn);
        UIBook.put("year", UIYear);
        UIBook.put("author", UIAuthorName);
        UIBook.put("book_category_id", UICategoryId);
        UIBook.put("description", UIDesc);

        System.out.println("UIBook = " + UIBook);


        // ASSERTIONS
        Assert.assertEquals(APIBook, UIBook);
        Assert.assertEquals(APIBook, DBBook);

    }

    //US4
    @Then("created user information should match with Database")
    public void created_user_information_should_match_with_database() {
        String user_id = thenPart.extract().response().path("user_id");
        DB_Util.runQuery("select * from users where id=" + user_id);
        Map<String, Object> DBUser = DB_Util.getRowMap(1);

        // DB DATA --> Remove not needed part from DBUser map


        Response apiData = given().log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken("librarian"))
                .pathParam("id", user_id)
                .when().get(ConfigurationReader.getProperty("library.baseUri") + "/get_user_by_id/{id}").prettyPeek();
        JsonPath jp = apiData.jsonPath();

        Map<String, Object> APIUser = new LinkedHashMap<>();
        APIUser.put("id", jp.getString("id"));
        APIUser.put("full_name", jp.getString("full_name"));
        APIUser.put("email", jp.getString("email"));
        APIUser.put("password", jp.getString("password"));
        APIUser.put("user_group_id", jp.getString("user_group_id"));
        APIUser.put("image", jp.getString("image"));
        APIUser.put("extra_data", jp.getString("extra_data"));
        APIUser.put("status", jp.getString("status"));
        APIUser.put("is_admin", jp.getString("is_admin"));
        APIUser.put("start_date", jp.getString("start_date"));
        APIUser.put("end_date", jp.getString("end_date"));
        APIUser.put("address", jp.getString("address"));


        //Assertion
        Assert.assertEquals(APIUser, DBUser);
    }

    @Then("created user should be able to login Library UI")
    public void created_user_should_be_able_to_login_library_ui() {
        String email = (String) randomDataMap.get("email");
        String password = (String) randomDataMap.get("password");
        loginPage = new LoginPage();
        loginPage.login(email, password);
        BrowserUtil.waitFor(3);
    }

    @Then("created user name should appear in Dashboard Page")
    public void created_user_name_should_appear_in_dashboard_page() {

        bookPage = new BookPage();
        BrowserUtil.waitFor(2);
        String UIFullName = bookPage.accountHolderName.getText();
        String APIFullName = (String) randomDataMap.get("full_name");

        Assert.assertEquals(APIFullName, UIFullName);
    }

    //US5
    @Given("I logged Library api with credentials {string} and {string}")
    public void i_logged_library_api_with_credentials_and(String email, String password) {
        token = LibraryAPI_Util.getToken(email, password);
        givenPart = given().log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken(email, password));
    }

    @Given("I send token information as request body")
    public void i_send_token_information_as_request_body() {
        givenPart.formParams("token", token);
    }


}
