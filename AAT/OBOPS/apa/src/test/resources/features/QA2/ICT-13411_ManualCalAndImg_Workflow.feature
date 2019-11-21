@MultiBrowserTesting @qa2
Feature: QA2: re-ingestion and more flexible workflow for manual processing - https://ictjira.alma.cl/browse/ICT-13411

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

  @regression @2018oct @sanity @regression-phab
  Scenario: ManualCalAndImg Do QA2 'Next Step' options
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    And the user inserts a value into a specific field
      | sb_name_qa2 | NGC |
    And the user clicks on a specific field
      | ous_state |
    And the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state_flag | reviewing |
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
      | processing_flags | manual_cal_and_img |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific button
      | do_qa2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    Then the specific list item is available
      | manually_calibrate |
      | manually_image     |
      | manually_process   |

  @regression @2018oct @sanity @regression-phab
  Scenario: Processing + ManualImaging pop-up warning message content
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state_flag | processing |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | manual_imaging |
    And the user inserts a value into a specific field
      | project_code | 2017.1.008 |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | manual_imaging |
    Then the specific button is displayed
      | finish_manual_imaging |
    When the user clicks on a specific button without wait
      | finish_manual_imaging |
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
    And the user clicks on a specific button
      | no |

  @2018oct @sanity @regression-phab
  Scenario: Next Step not displayed in the DR in the 'Finish' pop-up for Processing+ManualCalibration
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state_flag | processing |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | manual_calibration |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | manual_calibration |
    Then the specific button is displayed
      | finish_manual_calibration |
    When the user clicks on a specific button without wait
      | finish_manual_calibration |
    Then the element is no longer displayed
      | label | next_step |

  @2018oct @sanity @regression-phab
  Scenario Outline: Do QA2 button disabled for state <State>
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state_flag | <State> |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | manual_imaging |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific button is disabled
      | do_qa2 |

    Examples:
      | State      |
      | processing |
#      | fully_observed       |
#      | partially_observed   |
#      | ready_for_processing |
#      | verified             |

