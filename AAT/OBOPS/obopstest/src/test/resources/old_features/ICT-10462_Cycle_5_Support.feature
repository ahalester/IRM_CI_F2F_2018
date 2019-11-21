@MultiBrowserTesting @qa2
Feature: QA0/QA2: support for Cycle 5 new SB features - https://ictjira.alma.cl/browse/ICT-10462

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
    Given QA2 test environment is available
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the QA2 view is displayed
    And the user is authenticated
      | username |

  @all @regression
  Scenario: Cycle 5 improvements values are properly displayed
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    When the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state_flag | processing |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific element matches specific pattern
      | QA2 | expected_angular_resolution_qa2 | ^\d[\w.+-]*\d$    |
      | QA2 | largest_angular_scale_goal      | ^[0-9]*\.?[0-9]*$ |
#      | QA2 | largest_angular_scale_goal      | ^[-+]?[0-9]*\.?[0-9]*$ |

