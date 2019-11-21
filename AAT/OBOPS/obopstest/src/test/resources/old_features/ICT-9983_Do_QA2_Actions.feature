@MultiBrowserTesting @qa2
Feature: Ask whether data reducer should be unset - https://ictjira.alma.cl/browse/ICT-9983

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
    And the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |

#  @all @smoke @regression
#  Scenario: The DR should remain assigned to the MOUS in Reviewing state when selecting Fail (re-observe) status and
#  choose to keep the DR
#    When the user selects a specific option
#      | ous_state | reviewing |
#    Then the user checks if the OUS are displayed
#    When the user selects the first available OUS
#    Then the OUS Summary details page is displayed
#    And the user checks if data reducer is assigned
#      | data_reducer | data_reduction_methods |
#    And the specific data is collected for validation
#      | data_reducer |
#    When the user clicks on a specific button
#      | do_qa2 |
#    Then the Do QA2 pop-up form is displayed
#    When the user clicks on the Set QA2 Status
#    And the user selects specific status
#      | fail_re_observe |
#    Then the confirmation pop-up is displayed
#    And the confirmation dialog contains the specific message
#      | Should the data reducer stay assigned to this OUS? |
#    When the user clicks one of the popup's buttons
#      | yes |
#    Then the Do QA2 pop-up form is displayed
#    When the user clicks on QA2 Status Reason
#    Then the QA2 Status Reason options are displayed
#    And the user selects one QA2 Reason status by position
##    If the value is 0, empty or non-numerical, the first element on the list will be selected
#      |  |
#    And the user sets an EC value
#      | 0.01 |
#    When the user clicks on a specific button on Do QA2 page
#      | save |
#    Then the confirmation pop-up is displayed
#    When the user clicks one of the popup's buttons
#      | yes |
#    Then the OUS Summary details page is displayed
#    And the Data reducer is displayed
#      | data_reducer |

  @all @smoke @regression
  Scenario: The DR should remain assigned to the MOUS in ReadyForReview state when selecting Fail (re-observe) status and
  choose to keep the DR
    When the user selects a specific option
      | ous_state | ready_for_review |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    And the specific data is collected for validation
      | data_reducer |
    When the user clicks on a specific button
      | do_qa2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | fail_re_observe |
    Then the confirmation pop-up is displayed
    And the confirmation dialog contains the specific message
      | Should the data reducer stay assigned to this OUS? |
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
    When the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    And the user clicks on a specific button
      | ok |
    Then the OUS Summary details page is displayed
    And the Data reducer is displayed
      | data_reducer |

#  @all @smoke @regression
#  Scenario: The DR should be removed from the MOUS in Reviewing state when selecting Fail (re-observe) status and choose
#  to remove the DR
#    When the user selects a specific option
#      | ous_state | reviewing |
#    Then the user checks if the OUS are displayed
#    When the user selects the first available OUS
#    Then the OUS Summary details page is displayed
#    And the user checks if data reducer is assigned
#      | data_reducer | data_reduction_methods |
#    And the specific data is collected for validation
#      | data_reducer |
#    When the user clicks on a specific button
#      | do_qa2 |
#    Then the Do QA2 pop-up form is displayed
#    When the user clicks on the Set QA2 Status
#    And the user selects specific status
#      | fail_re_observe |
#    Then the confirmation pop-up is displayed
#    And the confirmation dialog contains the specific message
#      | Should the data reducer stay assigned to this OUS? |
#    When the user clicks one of the popup's buttons
#      | no |
#    Then the Do QA2 pop-up form is displayed
#    When the user clicks on QA2 Status Reason
#    Then the QA2 Status Reason options are displayed
#    And the user selects one QA2 Reason status by position
##    If the value is 0, empty or non-numerical, the first element on the list will be selected
#      |  |
#    And the user sets an EC value
#      | 0.01 |
#    When the user clicks on a specific button on Do QA2 page
#      | save |
#    Then the confirmation pop-up is displayed
#    When the user clicks one of the popup's buttons
#      | yes |
#    And the user clicks on a specific button
#      | ok |
#    And the Data reducer is no longer displayed
#      | data_reducer |

  @all @smoke @regression
  Scenario: The DR should be removed from the MOUS in ReadyForReview state when selecting Fail (re-observe) status and choose
  to remove the DR
    When the user selects a specific option
      | ous_state | ready_for_review |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    And the specific data is collected for validation
      | data_reducer |
    When the user clicks on a specific button
      | do_qa2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | fail_re_observe |
    Then the confirmation pop-up is displayed
    And the confirmation dialog contains the specific message
      | Should the data reducer stay assigned to this OUS? |
    When the user clicks one of the popup's buttons
      | no |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on QA2 Status Reason
    Then the QA2 Status Reason options are displayed
    And the user selects one QA2 Reason status by position
#    If the value is 0, empty or non-numerical, the first element on the list will be selected
      |  |
    And the user sets an EC value
      | 0.01 |
    When the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    And the user clicks on a specific button
      | ok |
    And the Data reducer is no longer displayed
      | data_reducer |

  @all @smoke @regression
  Scenario: The DR should remain assigned to the MOUS in Reviewing state when selecting Semi-Pass status
    When the user selects a specific option
      | ous_state | reviewing |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    And the specific data is collected for validation
      | data_reducer |
    When the user clicks on a specific button
      | do_qa2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | semi_pass_qa2 |
    When the user clicks on QA2 Status Reason
    Then the QA2 Status Reason options are displayed
    And the user selects one QA2 Reason status by position
#    If the value is 0, empty or non-numerical, the first element on the list will be selected
      |  |
    And the user adds a QA2 comment
      | final_qa2_comment | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    And the user clicks on a specific button
      | ok |
    And the Data reducer is displayed
      | data_reducer |

  @all @smoke @regression
  Scenario: The DR should remain assigned to the MOUS in ReadyForReview state when selecting Semi-Pass status
    When the user selects a specific option
      | ous_state | ready_for_review |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    And the specific data is collected for validation
      | data_reducer |
    When the user clicks on a specific button
      | do_qa2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | semi_pass_qa2 |
    When the user clicks on QA2 Status Reason
    Then the QA2 Status Reason options are displayed
    And the user selects one QA2 Reason status by position
#    If the value is 0, empty or non-numerical, the first element on the list will be selected
      |  |
    And the user adds a QA2 comment
      | final_qa2_comment | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    And the user clicks on a specific button
      | ok |
    And the Data reducer is displayed
      | data_reducer |

  @all @smoke @regression
  Scenario: The DR should remain assigned to the MOUS in Reviewing state when selecting Pass status
    When the user selects a specific option
      | ous_state | reviewing |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    And the specific data is collected for validation
      | data_reducer |
    When the user clicks on a specific button
      | do_qa2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | pass |
    And the user adds a QA2 comment
      | final_qa2_comment | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    And the user clicks on a specific button
      | ok |
    And the Data reducer is displayed
      | data_reducer |

  @all @smoke @regression
  Scenario: The DR should remain assigned to the MOUS in ReadyForReview state when selecting Pass status
    When the user selects a specific option
      | ous_state | ready_for_review |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    And the specific data is collected for validation
      | data_reducer |
    When the user clicks on a specific button
      | do_qa2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | pass |
    And the user adds a QA2 comment
      | final_qa2_comment | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    And the user clicks on a specific button
      | ok |
    And the Data reducer is displayed
      | data_reducer |

  @all @smoke @regression
  Scenario: ICT-11458 Check the error message while state transition is performed correctly
    When the user selects a specific option
      | ous_state | reviewing |
    And the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    When the user selects a specific option
      | processing_flags | pipeline_imaging |
    And the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    When the user selects a specific option
      | processing_flags | pipeline_cal_and_img |
    And the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    When the user selects a specific option
      | processing_flags | manual_imaging |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    And the specific data is collected for validation
      | data_reducer |
    When the user clicks on a specific button
      | do_qa2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | pass |
    And the user adds a QA2 comment
      | final_qa2_comment | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    And the Data reducer is displayed
      | data_reducer |