@MultiBrowserTesting
Feature: The Search option is available within the QA0 page

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties

  Background:
    Given that the test environment is available
    And that the aqua login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the QA0 view is displayed
    And the user is authenticated
      | username |

  @smoke
  Scenario: The EB Search and Advanced EB Search tabs are available
    When the specific search tab is selected
      | EB Search |
    Then the specific elements are available
      | BL,checkbox          |
      | ACA,checkbox         |
      | Science,checkbox     |
      | Calibration,checkbox |
      | Unset,checkbox       |
      | Pending,checkbox     |
      | Pass,checkbox        |
      | Fail,checkbox        |
      | SemiPass,checkbox    |
      | From,datebox,2011    |
      | To,datebox,today     |
      | Other,combobox       |
      | Search,button        |
      | Reset,button         |
    When the specific search tab is selected
      | Advanced EB Search |
    Then the specific elements are available
      | Project,textbox |
      | EB UID,textbox  |
      | SB Name,textbox |
