@MultiBrowserTesting @qa0
Feature: QA0: Add warning for special modes - https://ictjira.alma.cl/browse/ICT-8573

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

  @all @regression
  Scenario Outline: EB has a 'Special modes' value available in the QA0 EB Summary view
    When the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |
    And the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state | <OusState> |
    And the user clicks on a specific field
      | observing_modes |
    And the available options are displayed
      | observing_modes |
    And the user selects a specific option
      | observing_modes | <ObservingMode> |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | pipeline_calibration |
    And the user clicks on one available EB UIDs
    Then the specific label is displayed
      | special_modes |

    Examples:
      | ObservingMode      | OusState  |
      | external_ephemeris | Delivered |
#      | vlbi                     | Delivered |
#      | full_polarisation        | Delivered |
#      | time_constrained         | Delivered |
#      | high_frequency           | Delivered |
#      | narrow_bandwidth         | Delivered |
#      | long_baseline            | Delivered |
#      | solar                    | Delivered |
#      | user_defined_calibration | Delivered |
#      | target_of_opportunity    | Delivered |