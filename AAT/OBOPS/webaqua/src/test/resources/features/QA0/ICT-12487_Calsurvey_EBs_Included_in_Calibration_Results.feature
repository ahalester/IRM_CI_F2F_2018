@MultiBrowserTesting @qa0
Feature: QA0: calsurvey EBs not included in 'Calibration' results - https://ictjira.alma.cl/browse/ICT-12487

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
    Given the test environment is available
      | QA0 |
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the user is authenticated
      | username |

  @all @regression-phaa @2018apr @regression-phab
  Scenario: Calsurvey EBs included in 'Calibration' results
    And the QA0 view is displayed
    When the specific search tab is selected
      | eb_search |
    And the user checks/un-checks a specific checkbox
      | bl          | uncheck |
      | aca         | uncheck |
      | science     | uncheck |
      | unset       | uncheck |
      | calibration | check   |
    Then the user checks if the EBs are displayed
    And the list of filtered items contain
      | project | 0000.0.00219.CSV | eb_search |