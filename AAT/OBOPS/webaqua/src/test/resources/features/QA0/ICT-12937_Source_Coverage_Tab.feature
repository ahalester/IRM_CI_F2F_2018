@MultiBrowserTesting @qa0
Feature: QA0: updates for source coverage plots - https://ictjira.alma.cl/browse/ICT-12937

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
    Then the user is authenticated
      | username |
    And the QA0 view is displayed

  @all @regression-phaa @2018jun @regression-phab
  Scenario: Show the phase calibrator position available in source coverage plot
    When the specific search tab is selected
      | advanced_eb_search |
    And the user inserts a value into a specific field
      | eb_uid | A002/Xc9b9ce/X4c9 |
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    When the user clicks on a specific tab
      | sources |
    Then the Source Coverage plot is available
    When the user checks/un-checks a specific checkbox
      | show_phase_calibrator | check |
#    Then a specific element is displayed
#      | jqplot-table-legend-label | phase_calibrator |
    And the user checks/un-checks a specific checkbox
      | show_phase_calibrator | uncheck |