@MultiBrowserTesting @qa2
Feature: QA0/QA2: indicate long-baseline EBs/OUSes - https://ictjira.alma.cl/browse/ICT-11219

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
#    Given QA2 test environment is available
    Given the test environment is available
      | QA2 |
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the user is authenticated
      | username |
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    When the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | observing_modes |
    Then the available options are displayed
      | observing_modes |
    When the user selects a specific option
      | observing_modes | long_baseline |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS

  @all @qa0 @qa2
  Scenario: Long-baseline indicated for EBs/OUSes
    Then the OUS Summary details page is displayed
    And the specific label and data are displayed and contain the required details
      | mode | long_baseline |
    And the specific data is collected for validation
      | sb_names |
#    When QA0 test environment is available
    When the test environment is available
      | PHA |
      | QA0 |
    And AQUA login page is displayed
    And the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the user is authenticated
      | username |
    When the specific search tab is selected
      | advanced_eb_search |
    And the user inserts a value into a specific field
      | sb_name_qa0 | sb_names |
    And the user clicks on a specific button
      | search |
    Then the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the specific label and data are displayed and contain the required details
      | special_modes | long_baseline |