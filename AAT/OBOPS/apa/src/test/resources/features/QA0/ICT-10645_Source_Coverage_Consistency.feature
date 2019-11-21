@MultiBrowserTesting @qa0
Feature: QA0: discrepancy between source coverage and time on source information - https://ictjira.alma.cl/browse/ICT-10645

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
    Then the QA0 view is displayed
    And the user is authenticated
      | username |

  @all
  Scenario: Consistency between source coverage and time on source information
    When the specific search tab is selected
      | advanced_eb_search |
    And the user inserts a value into a specific field
      | eb_uid | A002/Xc32f8c/X1bec |
    And the user clicks on a specific button
      | search |
    Then the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
#  ...Ability:
    When the user clicks on a specific tab
      | source_coverage |

#    TODO - to be continued