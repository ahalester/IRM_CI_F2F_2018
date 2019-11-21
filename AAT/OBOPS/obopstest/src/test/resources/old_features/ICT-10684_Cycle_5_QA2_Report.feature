@MultiBrowserTesting @qa2
Feature: QA2: improvements to the QA2 report for Cycle 5 - https://ictjira.alma.cl/browse/ICT-10684

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Scenario: User authenticated in QA2
    Given QA2 test environment is available
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the QA2 view is displayed
    And the user is authenticated
      | username |

#    TODO - to be continued