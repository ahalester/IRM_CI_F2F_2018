@MultiBrowserTesting @qa0
Feature: QA0: info from aoscheck in AQUA - changes to summary and phase tabs - https://ictjira.alma.cl/browse/ICT-12199

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

  @all @regression-phaa @2018apr @regression-phab
  Scenario: Extra output parameters from aoscheck are available in Summary tab
    And the QA0 view is displayed
    When the specific search tab is selected
      | advanced_eb_search |
    And the user inserts a value into a specific field
      | project | 2017 |
    And the user inserts a value into a specific field
      | sb_name_qa0 | NGC5253_a_08_TM1 |
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the specific label is displayed
      | eb_status |
    And the particular row contains specific text
      | eb_status |
      | antennas  |
      | total     |
    And the specific label is displayed
      | qa0_status          |
      | achieved_ang_res    |
      | antennas            |
      | effective           |
      | usable              |
      | unflagged           |
      | total               |
      | exp_for_cycle5      |
      | minimum_acceptable  |
      | band_observed       |
      | highest_recommended |

  @all @regression-phaa @2018apr @regression-phab
  Scenario: Extra output parameters from aoscheck are available in Phase tab
    And the QA0 view is displayed
    When the specific search tab is selected
      | advanced_eb_search |
    And the user inserts a value into a specific field
      | project | 2017 |
    And the user inserts a value into a specific field
      | sb_name_qa0 | NGC5253_a_08_TM1 |
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    When the user clicks on a specific tab
      | phase |
    Then the phase RMS coverage plot is available
    And the user checks/un-checks a specific checkbox
      | corrected   | uncheck |
      | uncorrected | check   |
      | corrected   | check   |
      | uncorrected | uncheck |
    And the specific label is displayed
      | source_name                        |
      | phase_cal_science_completed_cycles |
    And scroll to specific label
      | phase_cal_science_completed_cycles |
    And the data grid was successfully retrieved
      | aos_check_phase | phase_cal_science_completed_cycles |