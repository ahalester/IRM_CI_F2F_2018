@MultiBrowserTesting @qa0
Feature: QA0: To Add the Band of the pointing observations - https://ictjira.alma.cl/browse/ICT-8567

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
    Given QA0 test environment is available
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the user is authenticated
      | username |

  @regression @all @sanity
  Scenario: The band of the pointing observations is available for each plot
    When the specific search tab is selected
      | advanced_eb_search |
    And the user inserts a value into a specific field
      | project | 2017 |
    Then the EBs are displayed
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    When the user clicks on a specific tab
      | pointing |
    Then the pointing offsets plot is available