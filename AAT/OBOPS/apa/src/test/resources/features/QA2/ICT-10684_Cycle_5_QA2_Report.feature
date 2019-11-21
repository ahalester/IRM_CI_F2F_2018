@MultiBrowserTesting @qa2
Feature: QA2: improvements to the QA2 report for Cycle 5 - https://ictjira.alma.cl/browse/ICT-10684

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Scenario: User authenticated in QA2
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

#    TODO - to be continued