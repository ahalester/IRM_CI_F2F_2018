@MultiBrowserTesting @qa0
Feature: QA0: remove some UI elements - https://ictjira.alma.cl/browse/ICT-12926

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

  @all @regression-phaa @2018jun @regression-phab-deprecated
  Scenario: Outliers not available in the Phase tab
    When the specific search tab is selected
      | advanced_eb_search |
    And the user inserts a value into a specific field
      | project | 2017 |
    Then the EBs are displayed
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    When the user clicks on a specific tab
      | phase |
    And the specific element is no longer displayed
      | outliers |

  @all @regression-phaa @2018jun @regression-phab
  Scenario: Focus tab not available
    When the specific search tab is selected
      | advanced_eb_search |
    And the user inserts a value into a specific field
      | project | 2017.1.00001.S |
    Then the EBs are displayed
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the "focus" tab is not displayed