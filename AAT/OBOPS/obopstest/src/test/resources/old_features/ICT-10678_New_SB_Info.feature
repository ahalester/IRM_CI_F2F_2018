@MultiBrowserTesting @qa2
Feature: QA2: add SG info to tabs - https://ictjira.alma.cl/browse/ICT-10678

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

  @regression @all
  Scenario: New fields available within the OUS Summary tab view
    When the specific search tab is selected
      | ous_search |
    And the user clicks on a specific checkbox
      | pass |
    And the user clicks on a specific checkbox
      | fail |
    And the user clicks on a specific checkbox
      | semi_pass |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific label and data are displayed
      | project_title     |
      | spatial_setup     |
      | sources           |
      | contact_scientist |
      | sensitivity_goal  |

  @regression @all
  Scenario: Spectral windows table displays bandwidth values in [MHz] and [km/s]
    When the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state | ready_for_review |
#    When the user clicks on a specific field
#      | processing_flags |
#    Then the available options are displayed
#      | processing_flags |
#    And the user selects a specific option
#      | processing_flags | pipeline_calibration |
#    When the user clicks on a specific field
#      | processing_flags |
#    Then the available options are displayed
#      | processing_flags |
#    And the user selects a specific option
#      | processing_flags | pipeline_imaging |
    When the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_cal_and_img |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | pl_process |
    Then the specific button is displayed
      | start_pl_process_review |
    And the specific grid header contains data
      | spectral_windows | Bandwidth [GHz]  |
      | spectral_windows | Bandwidth [km/s] |