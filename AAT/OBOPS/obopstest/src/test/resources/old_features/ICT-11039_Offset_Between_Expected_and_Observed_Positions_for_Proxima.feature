@MultiBrowserTesting @qa0
Feature: QA0: offset between expected and observed positions for Proxima - https://ictjira.alma.cl/browse/ICT-11039

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
    Given QA0 test environment is available
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the user is authenticated
      | username |

  @regression @all @smallregression
  Scenario: Plots coverage for Proxima Centaury observations is 100%
    When the specific search tab is selected
      | advanced_eb_search |
    And the user inserts a value into a specific field
      | sb_name_qa0 | Proxima |
    And the user inserts a value into a specific field
      | eb_uid | A002/Xbe18f4/Xfa2 |
    And the user clicks on a specific button
      | search |
    Then the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    When the user clicks on a specific tab
      | source_coverage |
    Then the mosaic coverage plot is available
    When the user clicks on a specific tab
      | phase |
    Then the phase RMS coverage plot is available