@MultiBrowserTesting @qa0
Feature: QA0: allow multiple flags for PENDING state - https://ictjira.alma.cl/browse/ICT-10170

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
    Given QA0 test environment is available
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the QA0 view is displayed
    And the user is authenticated
      | username |

  @all @regression
  Scenario: Multiple flags can be added simultaneously for PENDING state
    When the specific search tab is selected
      | eb_search |
    And the user clicks on a specific checkbox
      | pass |
    And the user clicks on a specific checkbox
      | fail |
    And the user clicks on a specific checkbox
      | semi_pass |
    And the user clicks on a specific button
      | search |
    Then the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the specific data is collected for validation
      | qa0_status |
    When the user clicks on a specific button
      | do_qa0 |
    Then the Do QA0 pop-up form is displayed
    When the user clicks on the Set QA0 Status
    And the user selects specific status
      | pending |
    Then the specific label is displayed
      | pending_reasons |
    When the user clicks on QA0 Status Reason
    Then the QA0 Status Reason options are displayed
    And the user selects one QA0 Reason status by position
#    If the value is 0, empty or non-numerical, the first element on the list will be selected
      | 1 |
    And the user clicks on QA0 Status Reason
    And the user selects one QA0 Reason status by position
#    If the value is 0, empty or non-numerical, the first element on the list will be selected
      |  |
    And the user sets an EC value
      | 0.01 |
    And the user adds a QA2 comment
      | final_qa0_comment | Test comment |
    When the user clicks on a specific button
      | save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    Then the user checks if the specific data is different
      | qa0_status |