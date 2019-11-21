@MultiBrowserTesting @qa2
Feature: QA2: disable editing of QA2 comment for DiP and Delivered states - https://ictjira.alma.cl/browse/ICT-13006

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
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
    And the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    When the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | ous_state |

  @all @regression-phab @2018jul @sanity
  Scenario Outline: Uneditable QA2 comment for <OUSState> state
    And the user selects a specific option
      | ous_state | <OUSState> |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And scroll to specific element
      | button | add_comment |
    And the element is no longer displayed
      | label | qa2_comment |

    Examples:
      | OUSState             |
      | delivery_in_progress |