@MultiBrowserTesting @qa2
Feature: Add possibility to download Weblog tar+gz file - https://ictjira.alma.cl/browse/ICT-10462

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
    Then the QA2 view is displayed
    And the user is authenticated
      | username |

  @all @regression @smoke @onetime
  Scenario: Download Weblog tar+gz file
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    When the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state | verified |
    And the user clicks on a specific field
      | processing_flags |
    And the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_calibration |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user hovers a specific element
      | weblog_download |
    Then the tooltip is displayed
    When the user clicks on a specific toolbar button
      | weblog |
#    Then the specific elements are available
#      | bla bla bla | label |  |

#    TODO - to be continued when the configuration issues will be fixed