@MultiBrowserTesting @qa2
Feature: Show release info in QA2 GUI - https://ictjira.alma.cl/browse/ICT-11407

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
    Then the user is authenticated
      | username |

  @regression @all @sanity @smallregression
  Scenario: AQUA release info available in the QA2 GUI
    And the specific toolbar button is displayed
      | release_info |
    When the user clicks on a specific toolbar button
      | release_info |
    Then the release Notes page is displayed