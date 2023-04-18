
Feature: As a librarian, I want to retrieve all users


  Scenario: Retrieve all users from the API endpoint

    Given I logged Library api as a "librarian"-AG
    And Accept header is "application/json"-AG
    When I send GET request to "/get_all_users" endpoint-AG
    Then status code should be 200-AG
    And Response Content type is "application/json; charset=utf-8"-AG
    And "id" field should not be null-AG
    And "name" field should not be null-AG