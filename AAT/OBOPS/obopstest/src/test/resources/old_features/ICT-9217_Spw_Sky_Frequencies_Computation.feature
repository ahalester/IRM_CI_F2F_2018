@MultiBrowserTesting @qa2
Feature: QA2: spw sky frequencies not correctly computed - https://ictjira.alma.cl/browse/ICT-9217

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
    Given QA2 test environment is available
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the user is authenticated
      | username |

  @all @regression @singleTest
  Scenario: Spw sky frequencies are correctly computed
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    When the specific search tab is selected
      | advanced_ous_search |
    And the user inserts a value into a specific field
      | project_code | 2016.1.01554.S |
    And the user inserts a value into a specific field
      | sb_name_qa2 | SPT0538-_a_06_TM1 |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | pipeline_calibration |
      | manual_calibration   |

#    TODO - to be continued when the configuration issues will be fixed