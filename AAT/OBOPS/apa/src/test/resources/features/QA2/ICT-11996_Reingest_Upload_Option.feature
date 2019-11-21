@MultiBrowserTesting @qa2
Feature: QA2: Re-ingest/upload option following ingestion error - https://ictjira.alma.cl/browse/ICT-11996

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
  Scenario: Package buttons are available
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
      | processing_flags | ingest_error |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific button is displayed
      | reingest_package   |
      | upload_new_package |

  @all @smoke @2018apr @regression-phab-deprecated
  Scenario: Re-ingest package action
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
      | processing_flags | ingest_error |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific button is displayed
      | reingest_package |
    When the user clicks on a specific button
      | reingest_package |
    Then the confirmation pop-up is displayed
    And the confirmation dialog contains the specific message
      | Do you want to re-ingest package ? |
    And the user clicks one of the popup's buttons
      | ok |

  @all @smoke @2018apr @regression-phab-deprecated
  Scenario: Upload new package action
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
      | processing_flags | ingest_error |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific button is displayed
      | upload_new_package |
    When the user clicks on a specific button
      | upload_new_package |
    Then the confirmation pop-up is displayed
    And the confirmation dialog contains the specific message
      | Do you want to upload a new package to APA ? |
    And the user clicks one of the popup's buttons
      | ok |
    And the user clicks one of the popup's buttons
      | ok |