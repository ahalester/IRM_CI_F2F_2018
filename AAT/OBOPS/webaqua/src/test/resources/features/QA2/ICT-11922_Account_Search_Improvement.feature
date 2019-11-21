@MultiBrowserTesting @qa2
Feature: Improve the performance of the account search dialogs in AQUA - https://ictjira.alma.cl/browse/ICT-11922

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
#    Given QA2 test environment is available
    Given the test environment is available
      | QA2 |
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the user is authenticated
      | username |
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    When the specific search tab is selected
      | advanced_ous_search |

  @all @qa2
  Scenario: Account search performance improvement
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state | reviewing |
    When the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_calibration |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user adds a specific comment recipient
      | comment_recipients | Bogdan |