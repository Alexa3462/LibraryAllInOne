Feature: As a librarian, I want to retrieve all users


  Scenario: Retrieve all users from the API endpoint

    Given I logged Library api as a "librarian"-NC
    And Accept header is "application/json"-NC
    When I send GET request to "/get_all_users" endpoint-NC
    Then status code should be 200-NC
    And Response Content type is "application/json; charset=utf-8"-NC
    And "id" field should not be null-NC
    And "name" field should not be null-NC


