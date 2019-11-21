@MultiBrowserTesting @qa0
Feature: QA0: show spw central frequency and bandwidth in phase tab - https://ictjira.alma.cl/browse/ICT-12232

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

  @all @regression-phaa @2018apr @regression-phab-deprecated
  Scenario: Show spw central frequency and bandwidth in phase tab
    And the QA0 view is displayed
    When the specific search tab is selected
      | advanced_eb_search |
    And the user inserts a value into a specific field
      | eb_uid | A002/Xc9b9ce/X4c9 |
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    When the user clicks on a specific tab
      | phase |
    Then a specific element is displayed
      | z_column | cf_sky         |
      | z_column | cf_rest        |
      | z_column | bandwidth_ghz  |
      | z_column | bandwidth_kms  |
      | z_column | no_of_channels |