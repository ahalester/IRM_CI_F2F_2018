@MultiBrowserTesting @qa2
Feature: QA2: create SD Manual Processing tab - https://ictjira.alma.cl/browse/ICT-10960

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
      | ous_state |
    Then the available options are displayed
      | ous_state |

  @all @smoke @regression-phab
  Scenario: ICT-10960 SD Manual Processing tab contains specific information
    When the user selects a specific option
      | ous_state | ready_for_processing |
    And the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    When the user selects a specific option
      | processing_flags | manual_single_dish |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | manual_single_dish |
    Then the specific button is displayed
      | start_sd_manual_processing |
    And the specific label and data are displayed
      | data_reducer                     |
      | sd_manual_process_status         |
      | execution_count_colon            |
      | average_expected_time_per_source |
      | processing_comment               |
      | eb_overview                      |
    And the specific toolbar button is displayed
      | prtspr_tickets |
    And the specific grid header contains data
      | spectral_windows | Bandwidth [GHz]  |
      | spectral_windows | Bandwidth [km/s] |

  @all @smoke
  Scenario: ICT-10960 Start SD Manual Review then Cancel
    When the user selects a specific option
      | ous_state | ready_for_processing |
    And the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    When the user selects a specific option
      | processing_flags | manual_single_dish |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific data is collected for validation
      | state |
    When the user clicks on a specific tab
      | manual_single_dish |
    Then the specific button is displayed
      | start_sd_manual_processing |
    When the user clicks on a specific button
      | start_sd_manual_processing |
    Then the confirmation pop-up is displayed
    And the confirmation dialog contains the specific message
      | has been automatically changed to <br>\nProcessing [ManualSingleDish] |
    When the user clicks on a specific button on the the information dialog box
      | ok |
    Then the specific button is displayed
      | finish_sd_manual_processing |
    And the specific button is displayed
      | cancel_sd_manual_processing |
    When the user clicks on a specific button without wait
      | cancel_sd_manual_processing |
    Then the confirmation pop-up is displayed
    And the confirmation dialog contains the specific message
      | Are you sure you want to cancel? This will delete any information entered by you. |
    When the user clicks on a specific button on the the information dialog box
      | ok |
    Then the confirmation pop-up is displayed
    And the confirmation dialog contains the specific message
      | has been automatically changed to <br>\nReadyForProcessing [ManualSingleDish] |
    When the user clicks on a specific button on the the information dialog box
      | ok |
    Then the OUS state is not changed
      | state |

  @all @smoke
  Scenario: ICT-10960 Start SD Manual Review then Finish
    When the user selects a specific option
      | ous_state | processing |
    And the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    When the user selects a specific option
      | processing_flags | manual_single_dish |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific data is collected for validation
      | state |
    When the user clicks on a specific tab
      | manual_single_dish |
    Then the specific button is displayed
      | finish_sd_manual_processing |
    And the specific button is displayed
      | cancel_sd_manual_processing |
    When the user clicks on a specific button without wait
      | finish_sd_manual_processing |
    Then the user inserts a value into a specific doublebox field
      | representative_target | 123 |
      | continuum             | 12  |
      | major_axis            | 3   |
      | minor_axis            | 2   |
      | position_angle        | 45  |
    And the user inserts a value into a specific field
      | casa_version | casa_version_val |
    And the user adds a QA2 comment
      | processing_comment | Test comment |
    When the user clicks on a specific button without wait
      | submit_for_drm_review |
    Then the information dialog box message is consistent
      | The DRMs of your executive will be alerted. |
    And the information dialog box message is consistent
      | Please make sure the final data package is made available to the DRM. |
    And the information dialog box message is consistent
      | Do you want to proceed ? |
    When the user clicks on a specific button without wait
      | yes |
    And the user clicks on a specific tab
      | ous_summary |
    Then the OUS Summary details page is displayed
    And the specific elements are available
      | awaiting_decision_label | label |  |

  @all @smoke
  Scenario: ICT-11400 'SD Manually process' MOUS in 'Processing Problem' state
    When the user selects a specific option
      | ous_state | processing_problem |
    And the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    When the user selects a specific option
      | processing_flags | pipeline_singledish |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific data is collected for validation
      | state |
    When the user clicks on a specific tab
      | pipeline_single_dish |
    Then the specific button is displayed
      | start_sd_pl_process_review |
    When the user clicks on a specific button without wait
      | start_sd_pl_process_review |
    Then the confirmation pop-up is displayed
    When the user clicks on a specific button on the the information dialog box
      | ok |