@MultiBrowserTesting @qa2 @regression
Feature: Inconsistent achieved rms values between AQUA and PT - https://ictjira.alma.cl/browse/ICT-10280, https://ictjira.alma.cl/browse/ICT-12028

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
    And the user clicks on QA2 tab
    And the QA2 view is displayed
    When the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |

# https://ictjira.alma.cl/browse/ICT-12028
  @2018may @regression-phaa @regression-phab-deprecated
  Scenario Outline: Do QA2 Review for MOUS in state <MOUSstate> and Processing Flag <ProcessingFlag>
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
      | pass |
    And the user adds a QA2 comment
      | qa2_comment | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    #    TODO - ugly workaround | to be removed
    And find the right OUS ugly - workaround
    When the user clicks one of the popup's buttons
      | yes |
    Then the specific button is displayed
      | ok |
    When the user clicks on a specific button
      | ok |
    Then the OUS Summary details page is displayed
    And the specific data was changed
      | state | <NewState> |

    Examples:
      | MOUSstate        | ProcessingFlag      | NewState |
      | ready_for_review | pipeline_imaging    | verified |
      | ready_for_review | manual_imaging      | verified |
      | ready_for_review | manual_single_dish  | verified |
      | ready_for_review | pipeline_singledish | verified |