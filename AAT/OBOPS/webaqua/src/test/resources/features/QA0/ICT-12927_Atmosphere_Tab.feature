@MultiBrowserTesting @qa0
Feature: QA0: updates for Atmosphere tab - https://ictjira.alma.cl/browse/ICT-12927

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
    And the QA0 view is displayed

  @all @regression-phaa @2018jun
  Scenario: Tsys of representative frequency markers are coloured based on time
    When the specific search tab is selected
      | eb_search |
    And the user checks/un-checks a specific checkbox
      | unset | uncheck |
      | pass  | check   |
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    When the user clicks on a specific tab
      | atmosphere |
    Then the phase Tsys of the representative frequency plot is available
      | tsys_freq_req |
    And the markers colour is set differently
      | 0 |

  @all @regression-phaa @2018jun
  Scenario: Source intent alongside source name
    When the specific search tab is selected
      | eb_search |
    And the user checks/un-checks a specific checkbox
      | unset | uncheck |
      | pass  | check   |
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    When the user clicks on a specific tab
      | atmosphere |
    Then the phase Tsys of the representative frequency plot is available
      | tsys_freq_req |
    And the specific label is displayed
      | source_name |
    And the source intent is alongside the source name
      | source_name |
