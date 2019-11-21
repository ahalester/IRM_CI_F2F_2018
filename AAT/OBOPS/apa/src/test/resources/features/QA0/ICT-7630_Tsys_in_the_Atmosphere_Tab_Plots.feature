@MultiBrowserTesting @qa0
Feature: QA0: Use the Tsys of the representative frequency (not the average) - https://ictjira.alma.cl/browse/ICT-7630

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

  @all @2018apr @regression-phab-deprecated
  Scenario: In the Atmosphere tab new plots with title "Tsys of the representative frequency" are visible
    And the QA0 view is displayed
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