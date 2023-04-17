package com.library.steps;

import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.ConfigurationReader;
import com.library.utility.DB_Util;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
public class B28G31_191_StepDef_VS {

    RequestSpecification givenPart;
    Response response;
    ValidatableResponse thenPart;

    @Given("I logged Library api as a {string} - VS")
    public void i_logged_library_api_as_a_vs(String userType) {
        givenPart=given().log().uri()
                .header("x-library-token", LibraryAPI_Util.getToken(userType));

    }

    @Given("Accept header is {string} - VS")
    public void accept_header_is_vs(String contentType) {
        givenPart.accept(contentType);

    }
    @Given("Request Content Type header is {string} - VS")
    public void request_content_type_header_is_vs(String contentType) {
        givenPart.contentType(contentType);

    }
    Map<String,Object> randomDataMap;
    @Given("I create a random {string} as request body - VS")
    public void i_create_a_random_as_request_body_vs(String randomData) {
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

    @When("I send POST request to {string} endpoint - VS")
    public void i_send_post_request_to_endpoint_vs(String endpoint) {

            response = givenPart.when().post(ConfigurationReader.getProperty("library.baseUri") + endpoint)
                    .prettyPeek();

            thenPart=response.then();

    }
    @Then("status code should be {int} - VS")
    public void status_code_should_be_vs(Integer statusCode) {
        thenPart.statusCode(statusCode);

    }
    @Then("Response Content type is {string} - VS")
    public void response_content_type_is_vs(String contentType) {
        thenPart.contentType(contentType);

    }
    @Then("the field value for {string} path should be equal to {string} - VS")
    public void the_field_value_for_path_should_be_equal_to_vs(String path, String value) {
        thenPart.body(path,is(value));

    }
  /*  @Then("{string} field should not be null - VS")
    public void following_fields_should_not_be_null(List<String> paths) {
        thenPart.body(paths.get(0),is(notNullValue()))
                .body(paths.get(1),is(notNullValue()))
                .body(paths.get(2),is(notNullValue()));

    }*/
  @Then("{string} field should not be null - VS")
  public void field_should_not_be_null_vs(String string) {
      thenPart.body(string,is(notNullValue()));
  }

    @And("UI, Database and API created book information must match - VS")
    public void uiDatabaseAndAPICreatedBookInformationMustMatchVS() {

        BrowserUtil.waitFor(3);

        // API DATA --> Expected --> Since we added data from API
        Response apiData = given().log().uri().header("x-library-token", LibraryAPI_Util.getToken("librarian"))
                .pathParam("id", response.path("book_id"))
                .when().get(ConfigurationReader.getProperty("library.baseUri") + "/get_book_by_id/{id}").prettyPeek();

        JsonPath jp = apiData.jsonPath();
        System.out.println("--------- API DATA -------------");
        Map<String,Object> APIBook=new LinkedHashMap<>();
        APIBook.put("name",jp.getString("name"));
        APIBook.put("isbn",jp.getString("isbn"));
        APIBook.put("year",jp.getString("year"));
        APIBook.put("author",jp.getString("author"));
        APIBook.put("book_category_id",jp.getString("book_category_id"));
        APIBook.put("description",jp.getString("description"));
        System.out.println("APIBook = " + APIBook);

        // To find book in database we need ID information
        String bookID = jp.getString("id");


        // DB DATA  --> Actual --> DB needs to show data that we add through API

        DB_Util.runQuery("select * from books where id='"+bookID+"'");
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
        DB_Util.runQuery("select id from book_categories where name='"+UIBookCategory+"'");
        String UICategoryId=DB_Util.getFirstRowFirstColumn();

        System.out.println("--------- UI DATA -------------");
        Map<String,Object> UIBook=new LinkedHashMap<>();
        UIBook.put("name",UIBookName);
        UIBook.put("isbn",UIIsbn);
        UIBook.put("year",UIYear);
        UIBook.put("author",UIAuthorName);
        UIBook.put("book_category_id",UICategoryId);
        UIBook.put("description",UIDesc);

        System.out.println("UIBook = " + UIBook);


        // ASSERTIONS
        Assert.assertEquals(APIBook,UIBook);
        Assert.assertEquals(APIBook,DBBook);



    }
}
