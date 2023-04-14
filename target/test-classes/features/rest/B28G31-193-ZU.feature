Feature: : As a user, I want to view my own user information using the API
  so that I can see what information is stored about me

@ZU
  Scenario Outline: Decode User
    Given I logged Library api with credentials "<email>" and "<password>" - ZU
    And Accept header is "application/json" - ZU
    And Request Content Type header is "application/x-www-form-urlencoded" - ZU
    And I send token information as request body - ZU
    When I send POST request to "/decode" endpoint - ZU
    Then status code should be 200 - ZU
    And Response Content type is "application/json; charset=utf-8" - ZU
    And the field value for "user_group_id" path should be equal to "<user_group_id>" - ZU
    And the field value for "email" path should be equal to "<email>" - ZU
    And "full_name" field should not be null - ZU
    And "id" field should not be null - ZU


    Examples:
      | email                | password    | user_group_id |
      | student5@library     | libraryUser | 3             |
      | librarian10@library  | libraryUser | 2             |
      | student10@library    | libraryUser | 3             |
      | librarian13@library  | libraryUser | 2             |
