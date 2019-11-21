@MultiBrowserTesting @qa2
Feature: QA2: unset QA2 state when OUS goes to ReadyForProcessing - https://ictjira.alma
  .cl/browse/ICT-11483

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  @regression @all @smallregression
  Scenario Outline: QA2 state marked as unset when OUS in status <OUS_State> goes to ReadyForProcessing
    Given QA2 test environment is available
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the user is authenticated
      | username |
    And the specific search tab is selected
      | advanced_ous_search |
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state | <OUS_State> |
    When the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | <Processing_Flag> |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the user checks if data reducer is assigned
      | data_reducer | data_reduction_methods |
    When the user clicks on a specific button
      | do_qa2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | <Status> |
    Then the confirmation pop-up is displayed
    And the confirmation dialog contains the specific message
      | Should the data reducer stay assigned to this OUS? |
    When the user clicks one of the popup's buttons
      | yes |
    Then the user adds a QA2 comment
      | final_qa2_comment | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    Then the specific label and data are displayed and contain the required details
      | qa2_status | unset |

    Examples:
      | OUS_State            | Processing_Flag      | Status             |
      | reviewing            | pipeline_calibration | pl_calibrate       |
      | processing           | pipeline_calibration | pl_image           |
      | ready_for_processing | pipeline_singledish  | sd_pl_process      |
      | processing_problem   | pipeline_cal_and_img | manually_calibrate |