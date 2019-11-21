@MultiBrowserTesting @qa2
Feature: Introduce PL_Processing_Executive flag - https://ictjira.alma.cl/browse/ICT-10448

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

  @all @regression
  Scenario: Do an Advanced search by 'PL Processing Exec'
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    And the specific elements are available
      | pl_processing_exec | label    |  |
      | jao                | checkbox |  |
      | na                 | checkbox |  |
      | eu                 | checkbox |  |
      | ea                 | checkbox |  |
    And the user checks/un-checks a specific checkbox
      | jao | check |
#    And the user clicks on a specific checkbox
#      | na  |
#      | eu  |
#      | ea  |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And a specific element is displayed on specific page area
      | pl_processing_exec | ous_summary |
    And the label is positioned as required
      | advanced_ous_search | pl_processing_exec | beneath | data_reducer_arc |
    And the label is positioned as required
      | ous_summary | pl_processing_exec | beneath | data_reduction_methods |