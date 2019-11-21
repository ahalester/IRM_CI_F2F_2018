@MultiBrowserTesting @qa0
Feature: Display and allow editing of AOS check output in AQUA - https://ictjira.alma.cl/browse/ICT-10247

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

#  Use "Re-run AOS check" button. A progress icon will be displayed.
  @all @smoke @regression-phab-deprecated
  Scenario: The AOS check json data button appears after user clicks on Re-run AOS check
    When the specific search tab is selected
      | eb_search |
    Then the specific elements are available
      | bl          | checkbox |      |
      | aca         | checkbox |      |
      | science     | checkbox |      |
      | calibration | checkbox |      |
      | unset       | checkbox |      |
      | pending     | checkbox |      |
      | pass        | checkbox |      |
      | fail        | checkbox |      |
      | semi_pass   | checkbox |      |
      | from        | datebox  | 2011 |
      | to,datebox  | today    |      |
      | search      | button   |      |
      | reset       | button   |      |
    And the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the user selects the next EB if the Re-run AOS check was already triggered
      | aos_check_json |
    When the user clicks on a specific button
      | rerun_aos_check |
    Then the specific button is displayed
      | aos_check_json |

#  Use "Re-run AOS check" button. A progress icon will be displayed.
  @all @smoke
  Scenario: The new Weather information is displayed after user clicks on Re-run AOS check
    When the specific search tab is selected
      | eb_search |
#    And the user checks/un-checks all checkboxes
#      | uncheck |
    And the user checks/un-checks a specific checkbox
      | bl      | uncheck |
      | aca     | uncheck |
      | science | uncheck |
      | unset   | uncheck |
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the user selects the next EB if the Re-run AOS check was already triggered
      | aos_check_json |
    And the specific data is collected for validation
      | weather |
    When the user clicks on a specific button
      | rerun_aos_check |
    Then the specific button is displayed
      | aos_check_json |
    And the data was successfully retrieved
      | weather | qa0_status |
    And the user checks if the specific data is different
      | weather |

#  Use "Re-run AOS check" button. A progress icon will be displayed.
  @all @smoke
  Scenario: The Final QA0 Comment is displayed after user clicks on Re-run AOS check
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the user selects the next EB if the Re-run AOS check was already triggered
      | aos_check_json |
    And the specific data is collected for validation
      | final_qa0_comment |
    When the user clicks on a specific button
      | rerun_aos_check |
    Then the specific button is displayed
      | aos_check_json |
    And the data was successfully retrieved
      | final_qa0_comment | total_duration |
    And the user checks if the specific data is different
      | final_qa0_comment |


#  new information in the "Weather" section will be added;
#  new information will be appended to the "Final QA0 Comment".
#  new numerical columns (if there is data) can be added to the "Source summary" table.
#  below "Source summary" table "Bandpass calibrator maximum number of channels" and "Phase cal/science completed cycles" will have values (if the data available).
  @all @smoke
  Scenario: The ExecBlock Summary data was successfully retrieved after the user clicks on Re-run AOS check
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the user selects the next EB if the Re-run AOS check was not triggered already
      | aos_check_json |
    And the specific button is displayed
      | aos_check_json |
#    TODO - replace the content from the second column with data that should appear after the re-run AOS check
#    Or leave it like this if you want to validate that the next available label after the one in the first column is
#    not the one from the second column
#    If the test should fail when the condition is not met, the Assume should be replaced with an Assert and the LOG
#    as well
    And the data was successfully retrieved
      | weather                            | qa0_status                         |
      | final_qa0_comment                  | total_duration                     |
      | bandpass_calib_max_ch_no           | phase_cal_science_completed_cycles |
      | phase_cal_science_completed_cycles | comments                           |
    And the data grid was successfully retrieved
#    TODO - replace "Total duration" with specific values that have to be validated
      | source_summary | total_duration |

#  There is a new QA0 Flags tab. It has a text area with QA0 Pipeline flags information.
  @all @smoke
  Scenario: The QA0 Flags view has a text area with QA0 Pipeline flags info after the user clicks on Re-run AOS check
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the user selects the next EB if the Re-run AOS check was not triggered already
      | aos_check_json |
    And the specific data is collected for validation
      | exec_block |
    When the user clicks on a specific tab
      | qa0_flags |
    Then the QA0 Flags view is displayed
      | exec_block |

#  Phase tab at the very end will have the "Aoscheck Phase" table with some values.
  @all @smoke
  Scenario: The Phase view has the Aoscheck Phase table with some values after the user clicks on Re-run AOS check
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the user selects the next EB if the Re-run AOS check was not triggered already
      | aos_check_json |
    When the user clicks on a specific tab
      | phase |
    Then the data grid was successfully retrieved
#    TODO - replace "Antenna-based phase differences on PhaseCal [deg]:" with specific values that have to be validated
      | aos_check_phase | antenna_based_phase_diff_on_phase_cal |

#  Baseline Coverage tab: "Array summary" table can have one new row "Antennas with issues (aoscheck):".
  @all @smoke
  Scenario: The Baseline Coverage view can have a new row "Antennas with issues (aoscheck)" into the Array summary
  table after the user clicks on Re-run AOS check
    When the user clicks on a specific button
      | search |
    Then the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the user selects the next EB if the Re-run AOS check was not triggered already
      | aos_check_json |
    When the user clicks on a specific tab
      | baseline_coverage |
    Then the data grid was successfully retrieved
#    TODO - replace "Antennas with issues (aoscheck):" with specific values that have to be validated
      | array_summary | antennas_with_issues |

#  Baseline Coverage tab:  Antenna positions plot have new type of points: "Antennas with issues", marked with red color.
  @all @smoke
  Scenario: The Antenna positions plot can have new type of points: "Antennas with issues", marked with red color after
  the user clicks on Re-run AOS check
    When the user clicks on a specific button
      | search |
    Then the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the user selects the next EB if the Re-run AOS check was not triggered already
      | aos_check_json |
    When the user clicks on a specific tab
      | baseline_coverage |
    Then the legend item is displayed
      | #ff00 | ant_with_issues |

#   Aa an user with DRM role, you can edit, save, reload, and reload from initial aos-check data the list of flags from QA0 Pipeline flags
  @all @smoke
  Scenario: The QA0 Pipeline flags area info can be edited by an user with DRM role after they click on Re-run AOS
  check
    When the user clicks on a specific button
      | search |
    Then the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the user selects the next EB if the Re-run AOS check was not triggered already
      | aos_check_json |
    And the specific data is collected for validation
      | exec_block |
    When the user clicks on a specific tab
      | qa0_flags |
    Then the QA0 Flags view is displayed
      | exec_block |
    And the specific button is displayed
      | save                  |
      | reload                |
      | reload_from_aos_check |