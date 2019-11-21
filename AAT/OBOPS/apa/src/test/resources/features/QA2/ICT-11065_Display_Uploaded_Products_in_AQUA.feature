@MultiBrowserTesting @qa2
Feature: QA2: bring up list all product files uploaded to APA from button in AQUA - https://ictjira.alma.cl/browse/ICT-11065

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  @all @regression-phab-deprecated
  Scenario: ICT-11065 Button displaying uploaded products is available in the 'Pipeline executions' table
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
    When the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    When the user selects a specific option
      | ous_state | ready_for_review |
    And the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    When the user selects a specific option
      | ous_state | reviewing |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_calibration |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_imaging |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_cal_and_img |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And a specific element is displayed
      | aux_header | pipeline_executions |
    When the user clicks on a specific button
      | show_products |
    Then the information dialog box is displayed