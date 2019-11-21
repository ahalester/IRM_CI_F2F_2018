@MultiBrowserTesting @qa0
Feature: QA0/QA2: support for Cycle 5 new SB features - https://ictjira.alma.cl/browse/ICT-10462

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
#    Given QA0 test environment is available
    Given the test environment is available
      | QA0 |
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the user is authenticated
      | username |

  @all
  Scenario: The Baseline Coverage tab 'Expected Angular Resolution' is displayed as a range
    When the specific search tab is selected
      | eb_search |
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    When the user clicks on a specific tab
      | baseline_coverage |
    And the specific element matches specific pattern
      | QA0 | expected_angular_resolution_qa0 | ^\d[\w.+-]*\d$ |