@B28G31-208
Feature: Default

	#As a user, I want to *search for a specific user* by their name or email address using the API so that I can quickly find the information I need.
	@B28G31-207
	Scenario: Retrieve single user - BG
		Given I logged Library api as a "librarian" - BG
		    And Accept header is "application/json" - BG
		    And Path param is "1" - BG
		    When I send GET request to "/get_user_by_id/{id}" endpoint - BG
		    Then status code should be 200 - BG
		    And Response Content type is "application/json; charset=utf-8" - BG
		    And "id" field should be same with path param - BG
		    And following fields should not be null - BG
		      | full_name |
		      | email     |
		      | password  |