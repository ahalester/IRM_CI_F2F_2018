@MultiBrowserTesting @qa2
Feature: Consistent EC between AQUA, PT and Scheduler - https://ictjira.alma.cl/browse/ICT-10230

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
    And the specific search tab is selected
      | advanced_ous_search |

  @all @smoke
  Scenario: Cancel the Review process for the available PL calibration
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    When the user selects a specific option
      | ous_state_flag | reviewing |
    When the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    When the user selects a specific option
      | processing_flags | pipeline_calibration |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific data is written into the tmp file
      | project_code |
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    When the user clicks on a specific tab
      | pipeline_calibration |
    Then the specific button is displayed
      | cancel_pl_cal_review |
    When the user clicks on a specific button without wait
      | cancel_pl_cal_review |
    Then the specific button is displayed
      | ok |
    And the user clicks on a specific button without wait
      | ok |
    And the user clicks on a specific button without wait
      | ok |

  @smoke
  Scenario: Start the Review process for the available PL calibration
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    When the user selects a specific option
      | ous_state_flag | ready_for_review |
    When the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    When the user selects a specific option
      | processing_flags | pipeline_calibration |
    And the user inserts a value into a specific field
      | project_code | project_code |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    When the user clicks on a specific tab
      | pipeline_calibration |
    Then the specific button is displayed
      | start_pl_cal_review |
    When the user clicks on a specific button without wait
      | start_pl_cal_review |
    Then the specific button is displayed
      | ok |
    When the user clicks on a specific button without wait
      | ok |
    Then the specific button is displayed
      | finish_pl_cal_review |

  @smoke
  Scenario: Finish the Review process for the available PL calibration
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    When the user selects a specific option
      | ous_state_flag | reviewing |
    When the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    When the user selects a specific option
      | processing_flags | pipeline_calibration |
    And the user inserts a value into a specific field
      | project_code | project_code |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    When the user clicks on a specific tab
      | pipeline_calibration |
    Then the specific button is displayed
      | finish_pl_cal_review |
    When the user clicks on a specific button without wait
      | finish_pl_cal_review |
    And the user clicks on a specific combobox
      | next_step |
    Then the user selects a specific combobox item
      | re_observe |
    When the user clicks on QA2 Status Reason
    Then the QA2 Status Reason options are displayed
    And the user selects one QA2 Reason status by position
#    If the value is 0, empty or non-numerical, the first element on the list will be selected
      |  |
    And the user sets an EC value
      | 0.01 |
    And the user adds a QA2 comment
      | calibration_comment | Test comment |
    And the specific button is displayed
      | save |
    When the user clicks on a specific button without wait
      | save |
    Then the specific button is displayed
      | yes |
    When the user clicks on a specific button without wait
      | yes |
    Then the specific button is displayed
      | ok |
    When the user clicks on a specific button without wait
      | ok |
    Then the OUS Summary details page is displayed

  @smoke
  Scenario: Start the Review process for the available SD PL processing
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    When the user selects a specific option
      | ous_state_flag | ready_for_review |
    When the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    When the user selects a specific option
      | processing_flags | pipeline_singledish |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific data is written into the tmp file
      | project_code |
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    When the user clicks on a specific tab
      | pipeline_calibration |
    Then the specific button is displayed
      | start_sd_pl_process_review |
    When the user clicks on a specific button without wait
      | start_sd_pl_process_review |
    Then the specific button is displayed
      | ok |
    When the user clicks on a specific button without wait
      | ok |
    Then the specific button is displayed
      | finish_sd_pl_process_review |

  @smoke
  Scenario: Cancel the Review process for the available SD PL processing
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    When the user selects a specific option
      | ous_state_flag | reviewing |
    When the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    When the user selects a specific option
      | processing_flags | pipeline_singledish |
    And the user inserts a value into a specific field
      | project_code | project_code |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    When the user clicks on a specific tab
      | pipeline_singledish |
    Then the specific button is displayed
      | cancel_sd_pl_process_review |
    When the user clicks on a specific button without wait
      | cancel_sd_pl_process_review |
    Then the specific button is displayed
      | ok |
    And the user clicks on a specific button without wait
      | ok |
    And the user clicks on a specific button without wait
      | ok |

  @smoke
  Scenario: The EC is consistent between AQUA, PT and Scheduler
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    When the user selects a specific option
      | ous_state_flag | reviewing |
    When the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    When the user selects a specific option
      | processing_flags | pipeline_calibration |
#    And the user inserts a value into a specific field
#      | Project code | 2016 |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific data is collected for validation
      | sb_names        |
      | execution_count |
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    When the user clicks on a specific button
      | do_qa2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | fail_re_observe |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on QA2 Status Reason
    Then the QA2 Status Reason options are displayed
    And the user selects one QA2 Reason status by position
#    If the value is 0, empty or non-numerical, the first element on the list will be selected
      |  |
    And the user sets an EC value
      | 0.01 |
    And the user adds a QA2 comment
      | qa2_comment | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    Then the OUS Summary details page is displayed
    And the specific data is collected for validation
      | execution_count |
    When the user clicks on a specific toolbar button
      | html_qa2_report |
    Then the QA2 Report html page is displayed
    And the QA2 Report html page contains the specific data
      | execution_count |
    When the user switches back to the main page
      | aqua |
    And the user clicks on a specific tab
      | pl_calibration |
    Then the specific elements are available
      | eb_overview | label |  |
    And the user clicks on a specific tab
      | ous_summary |
    When the user clicks on a specific link
      | in_protrack |
    Then the new browser tab is available
      | protrack |
      | cas      |
    When the user clicks on a specific tree cell
      | sb_names |
    Then the specific data is displayed within the page
      | execution_count | execution_count |
    And the Protrack EF sum equals the Execution Count value
      | EF | Execution Count |
    And the EF values are reduced according to the formula
      | EF_old - [(EC_old - EC_new)]/N_EB |
      | EF_old,EC_old,EC_new,N_EB         |
#    FIXME to be replaced with: -  https://ictjira.alma.cl/browse/ICT-11036
#      | EF_old * (EC_new / EC_old)        |
#      | EF_old,EC_old,EC_new              |
