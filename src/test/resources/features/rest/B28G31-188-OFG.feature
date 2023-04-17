@B28G31-199

Feature: Default

	#As a librarian, I want to *create a new user* using the library2.cydeo.com API so that I can add new users to the system.
	@B28G31-197
	Scenario: US1AC1 - Create a new user API - OFG
		Given I logged Library api as a "librarian" - OFG
		    And Accept header is "application/json" - OFG
		    And Request Content Type header is "application/x-www-form-urlencoded" - OFG
		    And I create a random "user" as request body - OFG
		    When I send POST request to "/add_user" endpoint - OFG
		    Then status code should be 200 - OFG
		    And Response Content type is "application/json; charset=utf-8" - OFG
		    And the field value for "message" path should be equal to "The user has been created." - OFG
		    And "user_id" field should not be null - OFG

	#As a librarian, I want to *create a new user* using the library2.cydeo.com API so that I can add new users to the system.
	@B28G31-198 @ui @db
	Scenario: US1AC2 - Create a new user all layers - OFG
		Given I logged Library api as a "librarian" - OFG
		    And Accept header is "application/json" - OFG
		    And Request Content Type header is "application/x-www-form-urlencoded" - OFG
		    And I create a random "user" as request body - OFG
		    When I send POST request to "/add_user" endpoint - OFG
		    Then status code should be 200 - OFG
		    And Response Content type is "application/json; charset=utf-8" - OFG
		    And the field value for "message" path should be equal to "The user has been created." - OFG
		    And "user_id" field should not be null - OFG
		    And created user information should match with Database - OFG
		    And created user should be able to login Library UI - OFG
		    And created user name should appear in Dashboard Page - OFG