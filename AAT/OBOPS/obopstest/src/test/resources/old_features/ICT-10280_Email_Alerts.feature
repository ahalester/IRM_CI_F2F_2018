@MultiBrowserTesting @qa2
Feature: Improvements to e-mail alerts - https://ictjira.alma.cl/browse/ICT-10280, https://ictjira.alma.cl/browse/ICT-10787

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
    When the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |

# https://ictjira.alma.cl/browse/ICT-10787
  @all @regression @email
  Scenario Outline: Email notification sent to DRM after set the 'Next step' "Fail (re-observe)"
  in the 'Do QA2' for MOUS in state <MOUSstate>
    When the user selects a specific option
      | ous_state | <MOUSstate> |
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
      | final_qa2_comment | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    Then the OUS Summary details page is displayed
    And the user clicks on the refresh State button
    And the specific data is collected for validation
      | project_code |
      | sb_names     |
    When the user navigates to a specific URL
      | mail_hog_url |
    Then the specific email notification is available
      | [AQUA]       |
      | Project code |
      | SB name(s)   |

    Examples:
      | MOUSstate |
      | reviewing |

# https://ictjira.alma.cl/browse/ICT-10787
  @all @regression @email
  Scenario Outline: Email notification sent to DRM after set the 'Next step' "Pass"
  in the 'Do QA2' for MOUS in state <MOUSstate>
    When the user selects a specific option
      | ous_state | <MOUSstate> |
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
      | pass |
    And the user adds a QA2 comment
      | final_qa2_comment | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    Then the OUS Summary details page is displayed
    And the user clicks on the refresh State button
    And the specific data is collected for validation
      | project_code |
      | sb_names     |
    When the user navigates to a specific URL
      | mail_hog_url |
    Then the specific email notification is available
      | [AQUA]       |
      | Project code |
      | SB name(s)   |

    Examples:
      | MOUSstate        |
      | reviewing        |
      | ready_for_review |

# https://ictjira.alma.cl/browse/ICT-10787
  @all @regression @email
  Scenario Outline: Email notification sent to DRM after set the 'Next step' "Semi-Pass"
  in the 'Do QA2' for MOUS in state <MOUSstate>
    When the user selects a specific option
      | ous_state | <MOUSstate> |
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
      | semi_pass_qa2 |
    And the user clicks on QA2 Status Reason
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
    Then the OUS Summary details page is displayed
    And the user clicks on the refresh State button
    And the specific data is collected for validation
      | project_code |
      | sb_names     |
    When the user navigates to a specific URL
      | mail_hog_url |
    Then the specific email notification is available
      | [AQUA]       |
      | Project code |
      | SB name(s)   |

    Examples:
      | MOUSstate        |
      | reviewing        |
      | ready_for_review |

# https://ictjira.alma.cl/browse/ICT-10787
  @all @regression @email
  Scenario Outline: In the PL Calibration Review dialog if we use PL Image option and press save a new
  notification is sent
    When the user selects a specific option
      | ous_state | <MOUSstate> |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | <ProcessingFlag> |
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
      | pl_image |
    Then the confirmation pop-up is displayed
    And the user clicks one of the popup's buttons
      | yes |
    And the user adds a QA2 comment
      | qa2_comment | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    Then the specific button is displayed
      | ok |
    When the user clicks on a specific button
      | ok |
    Then the OUS Summary details page is displayed
    And the specific data was changed
      | state | <NewState> |
    And the specific data is collected for validation
      | project_code |
      | sb_names     |
    When the user navigates to a specific URL
      | mail_hog_url |
    Then the specific email notification is available
      | [AQUA]       |
      | Project code |
      | SB name(s)   |

    Examples:
      | MOUSstate        | ProcessingFlag   | NewState                 |
      | reviewing        | pipeline_imaging | ready_for_processing_pli |
      | ready_for_review | pipeline_imaging | ready_for_processing_pli |

# https://ictjira.alma.cl/browse/ICT-10787
  @all @regression @email
  Scenario: Email notification sent when the state changes from Reviewing (Calibration) to
  ReadyForProcessing(Imaging)
    When the user selects a specific option
      | ous_state | reviewing |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_calibration |
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
      | pl_process_cal_img |
    Then the confirmation pop-up is displayed
    And the user clicks one of the popup's buttons
      | yes |
    And the user adds a QA2 comment
      | qa2_comment | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    Then the specific button is displayed
      | ok |
    When the user clicks on a specific button
      | ok |
    Then the OUS Summary details page is displayed
    And the specific data was changed
      | state | ready_for_processing_plci |
    And the specific data is collected for validation
      | project_code |
      | sb_names     |
    When the user navigates to a specific URL
      | mail_hog_url |
    Then the specific email notification is available
      | [AQUA]       |
      | Project code |
      | SB name(s)   |

# https://ictjira.alma.cl/browse/ICT-10787
  @all @regression @email
  Scenario: During reviewing process if you re-open Finish dialog or open it after "DRM review"
  option was chosen but no action was performed you will see the last comment text and when you
  press "Save" it updates the last comment
    And the user selects a specific option
      | ous_state_flag | reviewing |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_imaging |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | pipeline_imaging |
    Then the specific button is displayed
      | finish_pl_imaging_review |
    When the user clicks on a specific button without wait
      | finish_pl_imaging_review |
    And the user clicks on a specific combobox
      | next_step |
    Then the user selects a specific combobox item
      | drm_review |
    And the user inserts a value into a specific doublebox field
      | representative_target | 123 |
      | continuum             | 12  |
      | major_axis            | 3   |
      | minor_axis            | 2   |
      | position_angle        | 45  |
    And the user adds a QA2 comment
      | imaging_comment | Test comment |
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
    When the user clicks on a specific button
      | ok |
    And the user clicks on a specific tab
      | ous_summary |
    Then the OUS Summary details page is displayed
    And the specific data is collected for validation
      | project_code |
      | sb_names     |
    And the specific label is displayed
      | comment |
    When the user clicks on a specific tab
      | pipeline_imaging |
    Then the specific button is displayed
      | finish_pl_imaging_review |
    When the user clicks on a specific button without wait
      | finish_pl_imaging_review |
    Then the previously set comment is displayed within the comment textbox
      | Imaging comment | comment |
    When the user navigates to a specific URL
      | mail_hog_url |
    Then the specific email notification is available
      | [AQUA]       |
      | Project code |
      | SB name(s)   |

# https://ictjira.alma.cl/browse/ICT-10787
  @all @regression @email
  Scenario Outline: Text of comments added to MOUS are present in notification e-mails
    When the user selects a specific option
      | ous_state | <MOUSstate> |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | <ProcessingFlag> |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user inserts a value into a specific caption field
      | comments | Test comment |
    And the specific button is displayed
      | add_comment |
    Then the user clicks on a specific button
      | add_comment |
    And the specific data is collected for validation
      | project_code |
      | sb_names     |
    When the user navigates to a specific URL
      | mail_hog_url |
    Then the specific email notification is available
      | [AQUA] New QA2 comment for |
      | Project code               |
      | SB name(s)                 |
    When the user opens a specific email message
      | Project code |
      | SB name(s)   |
    Then the email body contains the specific comment text
      | comment |
    And the email body contains the specific text
      | DRM:          |
      | Data reducer: |

    Examples:
      | MOUSstate        | ProcessingFlag       |
      | reviewing        | pipeline_imaging     |
      | ready_for_review | pipeline_imaging     |
      | reviewing        | pipeline_calibration |
      | ready_for_review | pipeline_calibration |
      | reviewing        | manual_imaging       |
      | ready_for_review | manual_imaging       |

# https://ictjira.alma.cl/browse/ICT-10787
  @all @regression @email
  Scenario: Being DRM, assign someone as a data reducer to MOUS. Under account of data reducer
  start review - Cancel review - A new e-mail about cancellation will be sent
    When the user selects a specific option
      | ous_state | ready_for_review |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_calibration |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user assigns a specific data reducer
      | data_reducer | data_reduction_methods | Bogdan |
    And the specific data is written into the tmp file
      | project_code |
#    When the user clicks on a specific toolbar button
#      | Log out |
#    Then the specific button is displayed
#      | Yes |
#    When the user clicks on a specific button without wait
#      | Yes |
#    And the user navigates to a specific URL
#      | url |
#    Then QA2 test environment is available
#    And AQUA login page is displayed
#    When the user fills the credentials
#      | username |
#      | password |
#    And the user clicks the LOGIN button
#    And the user is authenticated
#      | username |
#    When the user clicks on QA2 tab
#    Then the QA2 view is displayed
#    And the specific search tab is selected
#      | advanced_ous_search |
#    When the user clicks on a specific field
#      | ous_state |
#    Then the available options are displayed
#      | ous_state |
#    And the user selects a specific option
#      | ous_state | ready_for_review |
#    And the user clicks on a specific field
#      | processing_flags |
#    And the available options are displayed
#      | processing_flags |
#    And the user selects a specific option
#      | processing_flags | pipeline_calibration |
#    And the user inserts a value into a specific field
#      | project_code | project_code |
#    Then the user checks if the OUS are displayed
#    When the user selects the first available OUS
#    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | pipeline_calibration |
    Then the specific button is displayed
      | start_pl_cal_review |
    When the user clicks on a specific button without wait
      | start_pl_cal_review |
    Then the specific button is displayed
      | ok |
    When the user clicks on a specific button
      | ok |
    And the user clicks on a specific tab
      | ous_summary |
    Then the OUS Summary details page is displayed
    When the user clicks on the refresh State button
    Then the OUS state is changed
      | state | reviewing |
    When the user clicks on a specific tab
      | pipeline_calibration |
    Then the specific button is displayed
      | cancel_pl_cal_review |
    When the user clicks on a specific button without wait
      | cancel_pl_cal_review |
    Then the specific button is displayed
      | ok |
    When the user clicks on a specific button
      | ok |
    Then the specific button is displayed
      | ok |
    When the user clicks on a specific button
      | ok |
    When the user navigates to a specific URL
      | mail_hog_url |
    Then the specific cancellation email notification was sent
      | Data reducer             |
      | seconds ago              |
      | a minute ago             |
      | has cancelled assignment |

# https://ictjira.alma.cl/browse/ICT-10787
  @all @regression @email
  Scenario: The user assigns himself as a data reducer, start review and cancel review. No
  notification will be sent
    When the user clicks on a specific toolbar button
      | logout |
    Then the specific button is displayed
      | yes |
    When the user clicks on a specific button without wait
      | yes |
    And the user navigates to a specific URL
      | url |
    Then QA2 test environment is available
    And AQUA login page is displayed
    When the user fills the credentials
      | username_obops |
      | password_obops |
    And the user clicks the LOGIN button
    And the user is authenticated
      | username_obops |
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    When the user selects a specific option
      | ous_state | ready_for_review |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_calibration |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user assigns a specific data reducer
      | data_reducer | data_reduction_methods | Bogdan |
    And the specific data is collected for validation
      | project_code |
      | sb_names     |
    When the user clicks on a specific toolbar button
      | logout |
    Then the specific button is displayed
      | yes |
    When the user clicks on a specific button without wait
      | yes |
    And the user navigates to a specific URL
      | url |
    Then QA2 test environment is available
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    And the user is authenticated
      | username |
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    When the user selects a specific option
      | ous_state | ready_for_review |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_calibration |
    And the user inserts a value into a specific field
      | project_code | project_code |
    And the user inserts a value into a specific field
      | sb_name_qa2 | sb_names |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | pipeline_calibration |
    Then the specific button is displayed
      | start_pl_cal_review |
    When the user clicks on a specific button without wait
      | start_pl_cal_review |
    Then the specific button is displayed
      | ok |
    When the user clicks on a specific button
      | ok |
    And the user clicks on a specific tab
      | ous_summary |
    Then the OUS Summary details page is displayed
    When the user clicks on the refresh State button
    Then the OUS state is changed
      | state | reviewing |
    When the user clicks on a specific tab
      | pipeline_calibration |
    Then the specific button is displayed
      | cancel_pl_cal_review |
    When the user clicks on a specific button without wait
      | cancel_pl_cal_review |
    Then the specific button is displayed
      | ok |
    When the user clicks on a specific button
      | ok |
    Then the specific button is displayed
      | ok |
    When the user clicks on a specific button
      | ok |
    When the user navigates to a specific URL
      | mail_hog_url |
    Then the specific cancellation email notification was not sent
      | Data reducer             |
      | seconds ago              |
      | a minute ago             |
      | has cancelled assignment |

# https://ictjira.alma.cl/browse/ICT-10787
  @all @regression @email
  Scenario Outline: Edit any comment in the QA2 Summary page.
  A new e-mail that comment was edited will be sent
    When the user selects a specific option
      | ous_state | <MOUSstate> |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | <ProcessingFlag> |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    When the user inserts a value into a specific caption field
      | comments | Test comment |
    And the specific button is displayed
      | add_comment |
    Then the user clicks on a specific button
      | add_comment |
    And the user edits the comment
      | comment |
    And the user clicks on a specific button
      | update |
    And the specific data is collected for validation
      | project_code |
      | sb_names     |
    When the user navigates to a specific URL
      | mail_hog_url |
    Then the specific email notification is available
      | [AQUA] QA2 comment edited for |
      | Project code                  |
      | SB name(s)                    |
    When the user opens a specific email message
      | Project code |
      | SB name(s)   |
    Then the email body contains the specific comment text
      | updated comment |
    And the email body contains the specific dynamic text
      | Data reducer |

    Examples:
      | MOUSstate        | ProcessingFlag       |
      | reviewing        | pipeline_imaging     |
      | ready_for_review | pipeline_imaging     |
      | reviewing        | pipeline_calibration |
      | ready_for_review | pipeline_calibration |
      | reviewing        | manual_imaging       |
      | ready_for_review | manual_imaging       |

# https://ictjira.alma.cl/browse/ICT-10787
  @all @regression @email
  Scenario Outline: New email notification sent on data reducer assignment
    And the user selects a specific option
      | ous_state_flag | <MOUSstate> |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
#    And collect the status of the Awaiting decision by DRM label
    And the user assigns a specific data reducer
      | data_reducer | data_reduction_methods | Arielle |
    And the specific data is collected for validation
      | project_code |
      | sb_names     |
      | data_reducer |
    When the user navigates to a specific URL
      | mail_hog_url |
    Then the specific email notification is available
      | [AQUA] ALMA data reduction assignment for SB |
      | Project code                                 |
      | SB name(s)                                   |
    When the user opens a specific email message
      | Project code |
      | SB name(s)   |
    Then the email body contains the specific dynamic text
      | Data reducer |

    Examples:
      | MOUSstate |
      | verified  |
#      | reviewing      |
#      | ready_for_review |

#    TODO - to be continued