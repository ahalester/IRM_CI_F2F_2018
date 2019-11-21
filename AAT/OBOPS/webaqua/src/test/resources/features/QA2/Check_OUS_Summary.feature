@MultiBrowserTesting @qa2
Feature: Display the aqua login page and authenticate

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
#    Given QA2 test environment is available
    Given the test environment is available
      | QA2 |

  @all @smoke
  Scenario: Display the QA2 page and and check if the OUS summary page is displayed
    Given AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user checks the warn checkbox
    And the user unchecks the warn checkbox
    And the user clicks the LOGIN button
    And the user is authenticated
      | username |
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
#    And the user checks/un-checks all checkboxes
#      | check |
#    And the user checks/un-checks all checkboxes
#      | uncheck |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed