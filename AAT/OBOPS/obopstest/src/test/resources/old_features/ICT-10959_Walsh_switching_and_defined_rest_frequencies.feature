@MultiBrowserTesting @qa0
Feature: QA0/QA2: improvements for Walsh switching and defined rest frequencies - https://ictjira.alma.cl/browse/ICT-10959

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

  @regression @all @smallregression
  Scenario: In Phase tab plot legend mentions the existence of mirror BBs
    When the specific search tab is selected
      | advanced_ous_search |
    And the user inserts a value into a specific field
      | ous_status_id | A002/Xc24c3f/X239 |
#    And the user clicks on a specific button
#      | search |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | manual_calibration |
    Then the specific toolbar button is displayed
      | uid://A002/Xc2ff76/X96b |
    When the user clicks on a specific toolbar button
      | uid://A002/Xc2ff76/X96b |
    Then the new browser tab is available
      | qa0_aqua |
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the user is authenticated
      | username |
    And wait for the loading progress to be completed
    And the EB Summary details page is displayed
    When the user clicks on a specific tab
      | phase |
    Then the phase RMS coverage plot is available
    And scroll to specific label
      | bb1_central |
    And the specific label is displayed
      | bb1_central |
      | bb2_central |
      | bb3_central |
      | bb4_central |
      | mirror      |