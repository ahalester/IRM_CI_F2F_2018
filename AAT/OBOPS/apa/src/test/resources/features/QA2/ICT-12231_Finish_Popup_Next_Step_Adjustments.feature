@MultiBrowserTesting @qa2
Feature: QA2: take re-observe option out of 'Next step' options in 'Finish' pop-up - https://ictjira.alma.cl/browse/ICT-12231

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
  Scenario: 'Re-observe' not available as a 'Next step' option in 'Finish' pop-up
    When the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the user selects a specific option
      | ous_state | reviewing |
    And the user clicks on a specific field
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_calibration |
    Then the user checks if the OUS are displayed
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
    Then the specific list item is not available
      | re_observe |

  @all @qa2 @2018mar @regression-phab
  Scenario: 'Re-observe' not available as an status option in 'DoQA2' pop-up
    When the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the user selects a specific option
      | ous_state | reviewing |
    And the user clicks on a specific field
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
    Then the specific list item is not available
      | re_observe |