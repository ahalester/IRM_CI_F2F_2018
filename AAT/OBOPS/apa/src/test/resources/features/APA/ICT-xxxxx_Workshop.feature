@MultiBrowserTesting @apa
Feature: F2F Workshop

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/APA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  @workshop @apa
  Scenario: Open APA and collect all available pipeline runs
    Given the test environment is available
      | APA |
    And APA header title is displayed
    When the user clicks on a specific header tab
      | pipeline_runs |
    Then the MOUS is in a specific state
      | uid://A001/X1320/X97 | PartiallyObserved |
    When the user clicks the info icon for a specific MOUS
      | uid://A001/X129e/X1c2 |
    Then the info pipeline run pop-up is displayed
    When the user clicks on Open Weblog
    Then the new browser tab is available
      | 2017.1.00698.S - Home |
    And the Weblog content is displayed