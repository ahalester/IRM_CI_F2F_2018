@MultiBrowserTesting @qa2
Feature: Data Processing Regression Tests, TC07

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

  @regression @2018oct @sanity @regression-phab @apa
  Scenario Outline: TC07: On 12M <Start_Process> and then <Next_Step>
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    And the user inserts a value into a specific field
      | ous_status_id | <MOUS_UID> |
    And the user clicks on a specific button
      | search |
    And the user selects the first available OUS
    And the user assigns a specific data reducer
      | data_reducer | data_reduction_methods | Bogdan |
#    And the user assigns a specific drm
#      | drm | Bogdan |
    And the user clicks on a specific tab
      | <OUS_Tab> |
    Then the specific button is displayed
      | <Start_Process> |
    And the user clicks on a specific button
      | <Start_Process> |
    And the user clicks on a specific button
      | ok |
    Then the specific button is displayed
      | <Finish_Process> |
    And the user clicks on a specific button
      | <Finish_Process> |
    And the user clicks on a specific combobox
      | next_step |
    And the user selects a specific combobox item
      | <Next_Step> |
    And the user clicks on a specific button
      | save |
    Then the specific button is displayed
      | yes |
    And the user clicks on a specific button
      | yes |
    And wait for the loading progress to be completed
    And the user clicks on a specific tab
      | ous_summary |
    When the user clicks on a specific button
      | do_qa2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | <Set_QA2_Status> |
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
      | 0 |
    And the user adds a QA2 comment
      | final_qa2_comment | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |

    Examples:
      | MOUS_UID        | OUS_Tab        | Start_Process       | Finish_Process       | Next_Step  | Set_QA2_Status  |
      | A001/X1284/Xb91 | pl_calibration | start_pl_cal_review | finish_pl_cal_review | drm_review | fail_re_observe |
