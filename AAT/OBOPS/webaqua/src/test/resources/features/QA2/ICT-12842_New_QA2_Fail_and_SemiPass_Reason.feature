@MultiBrowserTesting @qa2
Feature: QA2: New QA2 FAIL/SEMI-PASS reason - https://ictjira.alma.cl/browse/ICT-12842

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
    Given the test environment is available
      | QA2 |
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    And the user clicks on QA2 tab
    Then the user is authenticated
      | username |
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |

  @all @regression-phaa @2018jun @regression-phab
  Scenario: New QA2 reason available in the QA2 dialog for Semi-Pass flag
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
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
    And the QA2 Status Reason option is displayed
      | rms_dynamic_range_limited |

  @all @regression-phaa @2018jun @regression-phab
  Scenario: New QA2 reason available in the QA2 dialog for Fail flag
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
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
    When the user clicks on QA2 Status Reason
    Then the QA2 Status Reason options are displayed
    And the QA2 Status Reason option is displayed
      | rms_dynamic_range_limited |
