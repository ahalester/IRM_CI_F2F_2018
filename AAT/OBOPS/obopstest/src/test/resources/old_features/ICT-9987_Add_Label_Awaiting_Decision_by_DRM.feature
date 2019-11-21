@MultiBrowserTesting @qa2
Feature: Add label 'awaiting DRM decision' - https://ictjira.alma.cl/browse/ICT-9987

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
    Then the QA2 view is displayed
    And the user is authenticated
      | username |

  @all @regression
  Scenario: Start the Review process for an OUS in ReadyForReview state
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state_flag | ready_for_review |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_calibration |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific data is written into the tmp file
      | project_code |
    When the user clicks on a specific tab
      | pipeline_calibration |
    Then the specific button is displayed
      | start_pl_cal_review |
    When the user clicks on a specific button without wait
      | start_pl_cal_review |
    And the user clicks on a specific button without wait
      | ok |
    And the user clicks on a specific tab
      | ous_summary |
    Then the OUS Summary details page is displayed
    When the user clicks on the refresh State button
    Then the OUS state is changed
      | state | reviewing |

  @all @regression
  Scenario: Finish the Review process for an OUS in Reviewing state
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state_flag | reviewing |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_calibration |
    And the user inserts a value into a specific field
      | project_code | project_code |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | pipeline_calibration |
    Then the specific button is displayed
      | finish_pl_cal_review |
    When the user clicks on a specific button without wait
      | finish_pl_cal_review |
    And the user clicks on a specific combobox
      | next_step |
    Then the user selects a specific combobox item
      | drm_review |
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
    And the user clicks on a specific button without wait
      | ok |
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | pipeline_calibration |
    And the specific button is disabled
      | cancel_pl_cal_review_no_space |
    When the user clicks on a specific tab
      | ous_summary |
    Then the OUS Summary details page is displayed
    When the user clicks on the refresh State button
    Then the specific elements are available
      | awaiting_decision_label | label |  |
    When the user clicks on a specific button
      | self_assign |
    And the specific search tab is selected
      | ous_search |
    And the user clicks on a specific checkbox
      | awaiting_decision_ous_search |
#    TODO - to be continued

  @all @regression
  Scenario: Filter the available OUS by the Awaiting decision by me flag in the OUS Search
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | ous_search |
    And the user clicks on a specific checkbox
      | awaiting_decision_ous_search |
    When the user checks if the OUS are displayed
    And the user selects the first available OUS
    Then the specific elements are available
      | awaiting_decision_label | label |  |
#    Then the displayed items are filtered by
#      | QA2 status | Awaiting QA2 decision | OUS Search |

  @all @regression
  Scenario: Filter the available OUS by the Awaiting DRM decision flag in the Advanced OUS Search
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific checkbox
      | awaiting_decision_adv_ous_search |
    When the user checks if the OUS are displayed
    And the user selects the first available OUS
    Then the specific elements are available
      | awaiting_decision_label | label |  |
#    Then the displayed items are filtered by
#      | QA2 status | Awaiting QA2 decision | Advanced OUS Search |

  @all @regression
  Scenario: Do QA2 for any OUS in state Awaiting DRM decision and the "Awaiting decision by DRM"
  label disappears
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | ous_search |
    And the user clicks on a specific checkbox
      | awaiting_decision_ous_search |
    And the user checks if the OUS are displayed
    And the user selects the first available OUS
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
    And the specific element is no longer displayed
      | awaiting_decision_label |