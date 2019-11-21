@MultiBrowserTesting @qa2
Feature: QA2: support QA0 semi-pass only workflow - https://ictjira.alma.cl/browse/ICT-11942

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
    And the user is authenticated
      | username |
    And the user clicks on QA2 tab
    Then the QA2 view is displayed

  @all @smoke @2018apr @regression-phab-deprecated
  Scenario: QA0 Semi-pass workflow
    When the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state | delivery_in_progress |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | ingestion_triggered |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
#    And the specific button is displayed
#      | do_qa2 |
#    When the user clicks on a specific button
#      | do_qa2 |
#    Then the Do QA2 pop-up form is displayed
#    When the user clicks on the Set QA2 Status
#    And the user selects specific status
#      | semi_pass_qa2 |
#    And the user clicks on QA2 Status Reason
#    Then the QA2 Status Reason options are displayed
#    And the user selects one QA2 Reason status by position
##    If the value is 0, empty or non-numerical, the first element on the list will be selected
#      |  |
#    And the user adds a QA2 comment
#      | final_qa2_comment | Test comment |
#    When the user clicks on a specific button on Do QA2 page
#      | save |
#    Then the confirmation pop-up is displayed
#    When the user clicks one of the popup's buttons
#      | yes |
#    And the user clicks on a specific button
#      | ok |
#    Then the OUS Summary details page is displayed
    And the specific button is displayed
      | qa2_status_history |
    When the user clicks on a specific button
      | qa2_status_history |
    Then the information dialog box is displayed