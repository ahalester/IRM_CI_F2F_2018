@MultiBrowserTesting @qa2
Feature: Support manual processing workflow - https://ictjira.alma.cl/browse/ICT-10621

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

  @all
  Scenario Outline: No "Attach" buttons available within the <ProcessingFlag> tab
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
      | processing_flags | <ProcessingFlag> |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | <ProcessingFlag> |
    Then the specific button is displayed
      | <ButtonName> |
    And the specific button is not displayed
      | attach |
      | add    |
      | append |
      | upload |

    Examples:
      | ProcessingFlag     | ButtonName               |
      | manual_calibration | start_manual_calibration |
      | manual_imaging     | start_manual_imaging     |

  @all @email
  Scenario: The state of a MOUS with ManualCalibration flag changed to ReadyForReview after
  clicking on "Finish..."
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
    And the information dialog box message is consistent
      | The state will be changed to |
    When the user clicks on a specific button without wait
      | yes |
    Then the specific button is displayed
      | ok |
    When the user clicks on a specific button
      | ok |
#    And the user clicks on a specific tab
#      | ObsUnitSet Summary |
    Then the OUS Summary details page is displayed
#    And the user clicks on the refresh State button
    And the specific data was changed
      | state | ready_for_processing_mc |
    And the specific elements are available
      | awaiting_decision_label | label |  |
    And the specific data is collected for validation
      | project_code |
      | sb_names     |
    When the user navigates to a specific URL
      | mail_hog_url |
    Then the specific email notification is available
      | [AQUA]       |
      | Project code |
      | SB name(s)   |

  @all @email
  Scenario: The state of a MOUS with ManualImaging flag changed to Awaiting decision by DRM after
  clicking on "Finish..."
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
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | manual_imaging |
    Then the specific button is displayed
      | finish_manual_imaging |
    When the user clicks on a specific button without wait
      | finish_manual_imaging |
    And the user inserts a value into a specific doublebox field
      | representative_target | 123 |
      | continuum             | 12  |
      | major_axis            | 3   |
      | minor_axis            | 2   |
      | position_angle        | 45  |
    And the user inserts a value into a specific field
      | casa_version | casa_version_val |
    And the user adds a QA2 comment
      | imaging_comment | Test comment |
    When the user clicks on a specific button
      | submit_for_drm_review |
    Then the information dialog box message is consistent
      | The DRMs of your executive will be alerted |
    When the user clicks on a specific button without wait
      | yes |
    And the user clicks on a specific tab
      | ous_summary |
    Then the OUS Summary details page is displayed
    And the user clicks on the refresh State button
    And the specific data was changed
      | state | processing_mi |
    And the specific elements are available
      | awaiting_decision_label | label |  |
    And the specific data is collected for validation
      | project_code |
      | sb_names     |
    When the user navigates to a specific URL
      | mail_hog_url |
    Then the specific email notification is available
      | [AQUA]       |
      | Project code |
      | SB name(s)   |

  @all
  Scenario Outline: In the "Manual Imaging" tab the 'Finish' pop-up button stays active for the OUS state <OUSstate>
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state_flag | <OUSstate> |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | <ProcessingFlag> |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | <ProcessingFlag> |
    Then the specific button is displayed
      | <ButtonName> |
    And the specific button is enabled
      | <ButtonName> |

    Examples:
      | OUSstate         | ProcessingFlag | ButtonName            |
      | processing       | manual_imaging | finish_manual_imaging |
      | ready_for_review | manual_imaging | finish_manual_imaging |
      | reviewing        | manual_imaging | finish_manual_imaging |