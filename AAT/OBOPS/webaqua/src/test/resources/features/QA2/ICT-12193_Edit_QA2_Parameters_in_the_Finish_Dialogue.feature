@MultiBrowserTesting @qa2
Feature: QA2: editing of QA2 parameters entered in the 'Finish' dialogue - https://ictjira.alma.cl/browse/ICT-12193

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
  Scenario: 'Finish' button disabled after 'Awaiting DRM review' flag has been set
    When the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the user selects a specific option
      | ous_state | reviewing |
    And the user clicks on a specific field
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_cal_and_img |
    And the user checks/un-checks a specific checkbox
      | awaiting_decision_adv_ous_search | check |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | pl_process |
    Then the specific button is displayed
      | finish_pl_process_review |
    And the specific button is disabled
      | finish_pl_process_review |
