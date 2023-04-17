@vs
Feature: As a librarian, I want to create a new book - VS



  Scenario: Create a new book API - VS
    Given I logged Library api as a "librarian" - VS
    And Accept header is "application/json" - VS
    And Request Content Type header is "application/x-www-form-urlencoded" - VS
    And I create a random "book" as request body - VS
    When I send POST request to "/add_book" endpoint - VS
    Then status code should be 200 - VS
    And Response Content type is "application/json; charset=utf-8" - VS
    And the field value for "message" path should be equal to "The book has been created." - VS
    And "book_id" field should not be null - VS


@ui @db
  Scenario: Create a new book all layers - VS
    Given I logged Library api as a "librarian" - VS
    And Accept header is "application/json" - VS
    And Request Content Type header is "application/x-www-form-urlencoded" - VS
    And I create a random "book" as request body - VS
    And I logged in Library UI as "librarian" - VS
    And I navigate to "Books" page - VS
    When I send POST request to "/add_book" endpoint - VS
    Then status code should be 200 - VS
    And Response Content type is "application/json; charset=utf-8" - VS
    And the field value for "message" path should be equal to "The book has been created." - VS
    And "book_id" field should not be null - VS
    And UI, Database and API created book information must match - VS