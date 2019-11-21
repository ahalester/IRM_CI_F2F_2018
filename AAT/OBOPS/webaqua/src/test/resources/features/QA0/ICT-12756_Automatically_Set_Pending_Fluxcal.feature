@MultiBrowserTesting @qa0
Feature: QA0: automatically set Pending+Fluxcal - https://ictjira.alma.cl/browse/ICT-12756

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
    Given the test environment is available
      | QA0 |
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    And the user clicks on "QA0" tab
    Then the user is authenticated
      | username |

  @all @regression-phaa @2018jun-disabled
  Scenario: Notification displayed and Pending flag set with reason FLUX_CAL for QA0 Pending
    When the QA0 view is displayed
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And collect all values from a specific column
      | sb_name_qa0 | eb_search |
    And check if the EB Summary view contains specific data
      | label | calibrate_flux | displayed |
    When the user clicks on a specific link
      | in_protrack |
    Then the new browser tab is available
      | protrack |
      | cas      |
    When change MOUS state in ProTrack
      | delivered_partially_observed |
    Then the user clicks on a specific button
      | ok |
    When the user switches back to the parent window
    And the specific data is collected for validation
      | qa0_status |
    And the user clicks on a specific button
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
    And the user sets an EC value
      | 0.01 |
    And the user adds a QA0 comment
      | final_qa0_comment | Test comment |
    When the user clicks on a specific button
      | save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    Then the user checks if the specific data is different
      | qa0_status |

  @all @regression-phaa @2018jun-disabled
  Scenario: Notification displayed and Pending flag set with reason FLUX_CAL for QA0 Pass
    When the QA0 view is displayed
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And collect all values from a specific column
      | sb_name_qa0 | eb_search |
    And check if the EB Summary view contains specific data
      | label | calibrate_flux | displayed |
    When the user clicks on a specific link
      | in_protrack |
    Then the new browser tab is available
      | protrack |
      | cas      |
    When change MOUS state in ProTrack
      | delivered_partially_observed |
    Then the user clicks on a specific button
      | ok |
    When the user switches back to the parent window
    And the specific data is collected for validation
      | qa0_status |
    And the user clicks on a specific button
      | do_qa0 |
    Then the Do QA0 pop-up form is displayed
    When the user clicks on the Set QA0 Status
    And the user selects specific status
      | pass |
    And the user sets an EC value
      | 0.01 |
    And the user adds a QA0 comment
      | final_qa0_comment | Test comment |
    Then the user clicks on a specific button
      | save |
    And the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | yes |
    Then the user checks if the specific data is different
      | qa0_status |