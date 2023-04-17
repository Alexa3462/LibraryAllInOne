@B28G31-206
Feature: Default

	@B28G31-204
	Scenario: As a librarian, I want to retrieve all users from the library2.cydeo.com API endpoint so that I can display them in my application.
		Given I logged Library api as a "librarian"-NC
		    And Accept header is "application/json"-NC
		    When I send GET request to "/get_all_users" endpoint-NC
		    Then status code should be 200-NC
		    And Response Content type is "application/json; charset=utf-8"-NC
		    And "id" field should not be null-NC
		    And "name" field should not be null-NC