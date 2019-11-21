@MultiBrowserTesting @qa2
Feature: Implement a QA3 workflow in AQUA - https://ictjira.alma.cl/browse/ICT-13154

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
    Then the user is authenticated
      | username |

  @regression @2018oct @regression-phab @sanity
  Scenario: 'Open QA3 investigation' button available OUSes in Delivered state
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state_flag | delivered |
    And the user inserts a value into a specific field
      | ous_status_id | A001/X12c4/X7a |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And scroll to specific element
      | button | open_qa3_investigation |
    And the specific button is displayed
      | open_qa3_investigation |
    When the user clicks on a specific button
      | open_qa3_investigation |
    Then the specific label is displayed
      | substate |
    And a specific element is displayed
      | z-radio-content | user_initiated        |
      | z-radio-content | observatory_initiated |

  @regression @2018oct @regression-phab @sanity
  Scenario: Search for QA3InProgress OUSes in the Advanced search
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state_flag | qa3_in_progress |
    And the user checks/un-checks a specific checkbox
      | qa3_ongoing | check   |
      | qa3_ongoing | uncheck |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And scroll to specific element
      | button | do_qa3 |
    And the specific button is displayed
      | do_qa3 |
    And the specific button is displayed
      | abort_qa3 |

  @regression @2018oct @regression-phab @sanity
  Scenario: 'Abort QA3' and 'Open QA3 investigation'
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state_flag | qa3_in_progress |
    And the user checks/un-checks a specific checkbox
      | qa3_ongoing | check   |
      | qa3_ongoing | uncheck |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific data is collected for validation
      | state |
    And scroll to specific element
      | button | abort_qa3 |
    And the specific button is displayed
      | abort_qa3 |
    When the user clicks on a specific button
      | abort_qa3 |
    Then the specific label is not displayed
      | qa3_ongoing |
    And scroll to specific element
      | button | open_qa3_investigation |
    When the user clicks on a specific button
      | open_qa3_investigation |
    Then a specific element is displayed
      | z-radio-content | user_initiated        |
      | z-radio-content | observatory_initiated |
    And the user sets a specific QA3 substate
      | state |
    When the user clicks on a specific button
      | ok |
    Then the specific label is displayed
      | qa3_ongoing |

  @regression @2018oct @regression-phab @sanity
  Scenario: 'Do QA3' set the next step then 'Cancel'
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state_flag | qa3_in_progress |
    And the user checks/un-checks a specific checkbox
      | qa3_ongoing | check   |
      | qa3_ongoing | uncheck |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific data is collected for validation
      | state |
    And scroll to specific element
      | button | do_qa3 |
    And the specific button is displayed
      | do_qa3 |
    When the user clicks on a specific button
      | do_qa3 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | fail_re_observe |
    And the user adds a QA2 comment
      | qa3_comment | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | cancel |
    Then the OUS state is not changed
      | state |

  @regression @2018oct @regression-phab @sanity
  Scenario: 'Reset proprietary time' available in the 'Do QA2' form
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state_flag | ready_for_review |
    And the user inserts a value into a specific field
      | ous_status_id | uid://A001/X12a3/X7b2 |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And scroll to specific element
      | button | do_qa2 |
    And the specific button is displayed
      | do_qa2 |
    When the user clicks on a specific button
      | do_qa2 |
    Then the Do QA2 pop-up form is displayed
    And a specific element is displayed
      | z-checkbox-content | proprietary_time |
    And the user checks/un-checks a specific checkbox
      | proprietary_time | check   |
      | proprietary_time | uncheck |