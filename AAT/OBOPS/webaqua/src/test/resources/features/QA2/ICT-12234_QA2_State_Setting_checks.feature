@MultiBrowserTesting @qa2
Feature: QA2: checks when setting QA2 state - https://ictjira.alma.cl/browse/ICT-12234

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

  @all @qa2 @2018mar
  Scenario Outline: QA2 parameters required for 'Do QA2' if 'Pass' status has been selected
    When the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the user selects a specific option
      | ous_state | <State> |
    And the user clicks on a specific field
      | processing_flags |
    And the user selects a specific option
      | processing_flags | <ProcessingFlags> |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    When the user clicks on a specific button
      | do_qa2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | <QA2Status> |
    And the user adds a QA2 comment
      | final_qa2_comment | Test comment |
    And the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    And the confirmation dialog contains the specific message
      | <Message> |

    Examples:
      | State                | ProcessingFlags      | QA2Status | Message                                                                                               |
      | ready_for_review     | pipeline_singledish  | pass      | Please enter the required QA2 parameters (achieved sensitivity and beam)                              |
      | ready_for_review     | pipeline_cal_and_img | pass      | Please enter the required QA2 parameters (achieved sensitivity and beam)                              |
      | ready_for_review     | manual_imaging       | pass      | Please enter the required QA2 parameters (achieved sensitivity and beam)                              |
#      | ready_for_review | pipeline_imaging     | pass      | Please enter the required QA2 parameters (achieved sensitivity and beam) |
#      | ready_for_review | manual_single_dish   | pass      | Please enter the required QA2 parameters (achieved sensitivity and beam) |
      | reviewing            | pipeline_singledish  | pass      | Please enter the required QA2 parameters (achieved sensitivity and beam)                              |
      | reviewing            | pipeline_imaging     | pass      | Please enter the required QA2 parameters (achieved sensitivity and beam)                              |
      | reviewing            | pipeline_cal_and_img | pass      | Please enter the required QA2 parameters (achieved sensitivity and beam)                              |
#      | reviewing        | manual_imaging       | pass      | Please enter the required QA2 parameters (achieved sensitivity and beam) |
#      | reviewing        | manual_single_dish   | pass      | Please enter the required QA2 parameters (achieved sensitivity and beam) |
      | ready_for_review     | pipeline_calibration | pass      | Please make sure the latest reduction method contains imaging and set the processing flag accordingly |
      | ready_for_review     | manual_calibration   | pass      | Please make sure the latest reduction method contains imaging and set the processing flag accordingly |
      | reviewing            | pipeline_calibration | pass      | Please make sure the latest reduction method contains imaging and set the processing flag accordingly |
      | processing           | pipeline_cal_and_img | pass      | Please make sure that the OUS state is set correctly before setting the QA2 state                     |
      | processing           | manual_imaging       | pass      | Please make sure that the OUS state is set correctly before setting the QA2 state                     |
#      | processing           | pipeline_singledish  | pass      | Please make sure that the OUS state is set correctly before setting the QA2 state                     |
#      | processing           | manual_single_dish   | pass      | Please make sure that the OUS state is set correctly before setting the QA2 state                     |
      | verified             | manual_imaging       | pass      | Please make sure that the OUS state is set correctly before setting the QA2 state                     |
      | ready_for_processing | manual_single_dish   | pass      | Please make sure that the OUS state is set correctly before setting the QA2 state                     |
#      | ready_for_processing | pipeline_cal_and_img | pass      | Please make sure that the OUS state is set correctly before setting the QA2 state                     |
#      | processing_problem   | pipeline_singledish  | pass      | Please make sure that the OUS state is set correctly before setting the QA2 state                     |
#      | processing_problem   | pipeline_cal_and_img | pass      | Please make sure that the OUS state is set correctly before setting the QA2 state                     |

  @all @qa2 @2018mar
  Scenario Outline: QA2 parameters required for 'Do QA2' if 'Semi-pass' status has been selected
    When the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the user selects a specific option
      | ous_state | <State> |
    And the user clicks on a specific field
      | processing_flags |
    And the user selects a specific option
      | processing_flags | <ProcessingFlags> |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    When the user clicks on a specific button
      | do_qa2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | <QA2Status> |
    And the user clicks on QA2 Status Reason
    Then the QA2 Status Reason options are displayed
    And the user selects one QA2 Reason status by position
      |  |
    And the user adds a QA2 comment
      | final_qa2_comment | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    And the confirmation dialog contains the specific message
      | <Message> |

    Examples:
      | State                | ProcessingFlags      | QA2Status     | Message                                                                                               |
      | ready_for_review     | pipeline_singledish  | semi_pass_qa2 | Please enter the required QA2 parameters (achieved sensitivity and beam)                              |
      | ready_for_review     | pipeline_cal_and_img | semi_pass_qa2 | Please enter the required QA2 parameters (achieved sensitivity and beam)                              |
      | ready_for_review     | manual_imaging       | semi_pass_qa2 | Please enter the required QA2 parameters (achieved sensitivity and beam)                              |
#      | ready_for_review | pipeline_imaging     | semi_pass_qa2      | Please enter the required QA2 parameters (achieved sensitivity and beam) |
#      | ready_for_review | manual_single_dish   | semi_pass_qa2      | Please enter the required QA2 parameters (achieved sensitivity and beam) |
      | reviewing            | pipeline_singledish  | semi_pass_qa2 | Please enter the required QA2 parameters (achieved sensitivity and beam)                              |
      | reviewing            | pipeline_imaging     | semi_pass_qa2 | Please enter the required QA2 parameters (achieved sensitivity and beam)                              |
      | reviewing            | pipeline_cal_and_img | semi_pass_qa2 | Please enter the required QA2 parameters (achieved sensitivity and beam)                              |
#      | reviewing        | manual_imaging       | semi_pass_qa2      | Please enter the required QA2 parameters (achieved sensitivity and beam) |
#      | reviewing        | manual_single_dish   | semi_pass_qa2      | Please enter the required QA2 parameters (achieved sensitivity and beam) |
      | ready_for_review     | pipeline_calibration | semi_pass_qa2 | Please make sure the latest reduction method contains imaging and set the processing flag accordingly |
      | ready_for_review     | manual_calibration   | semi_pass_qa2 | Please make sure the latest reduction method contains imaging and set the processing flag accordingly |
      | reviewing            | pipeline_calibration | semi_pass_qa2 | Please make sure the latest reduction method contains imaging and set the processing flag accordingly |
      | processing           | pipeline_cal_and_img | semi_pass_qa2 | Please make sure that the OUS state is set correctly before setting the QA2 state                     |
      | processing           | manual_imaging       | semi_pass_qa2 | Please make sure that the OUS state is set correctly before setting the QA2 state                     |
#      | processing           | pipeline_singledish  | semi_pass_qa2      | Please make sure that the OUS state is set correctly before setting the QA2 state                     |
#      | processing           | manual_single_dish   | pass      | Please make sure that the OUS state is set correctly before setting the QA2 state                     |
      | verified             | manual_imaging       | semi_pass_qa2 | Please make sure that the OUS state is set correctly before setting the QA2 state                     |
      | ready_for_processing | manual_single_dish   | semi_pass_qa2 | Please make sure that the OUS state is set correctly before setting the QA2 state                     |
#      | ready_for_processing | pipeline_cal_and_img | semi_pass_qa2      | Please make sure that the OUS state is set correctly before setting the QA2 state                     |
#      | processing_problem   | pipeline_singledish  | semi_pass_qa2      | Please make sure that the OUS state is set correctly before setting the QA2 state                     |
#      | processing_problem   | pipeline_cal_and_img | semi_pass_qa2      | Please make sure that the OUS state is set correctly before setting the QA2 state                     |