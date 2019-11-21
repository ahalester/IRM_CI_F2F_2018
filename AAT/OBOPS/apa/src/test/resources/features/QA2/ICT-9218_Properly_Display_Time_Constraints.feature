@MultiBrowserTesting @qa2
Feature: QA2: properly display time constraints - https://ictjira.alma.cl/browse/ICT-9218

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
    When the user clicks on QA2 tab
    And the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |

  @all @regression-phab @2018jul @sanity
  Scenario: Fixed time windows
    And the user inserts a value into a specific field
      | ous_status_id | A001/X129e/X6a |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | pipeline_calibration |
    Then a specific element is displayed
      | z-caption-content | time_constraints |
      | z-column-content  | start_time       |
      | z-column-content  | end_time         |
    And the specific label is displayed
      | dependency_list       |
      | time_constraint_msg_1 |

  @all @regression-phab @2018jul @sanity
  Scenario: Relative time windows - EBs observed outside the specified time range(s)
    And the user inserts a value into a specific field
      | ous_status_id | A002/Xc24c3f/X29d |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific label is displayed
      | eb_outside_range      |
#      | time_dependency_msg_1 |

  @all @regression-phab @2018jul @sanity
  Scenario: Relative time windows
    And the user inserts a value into a specific field
      | ous_status_id | A001/X129e/X6a |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | pipeline_calibration |
    Then a specific element is displayed
      | z-caption-content | time_constraints |