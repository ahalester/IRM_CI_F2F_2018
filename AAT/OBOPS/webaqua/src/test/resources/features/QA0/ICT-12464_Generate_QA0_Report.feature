@MultiBrowserTesting @qa0
Feature: QA0 report is not being created if last scan is incomplete - https://ictjira.alma.cl/browse/ICT-12464

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
    Given the test environment is available
      | QA0 |
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the user is authenticated
      | username |

  @all @2018apr @regression-phab
  Scenario: QA0 report is generated if the last scan of the EB was incomplete
    And the QA0 view is displayed
    When the specific search tab is selected
      | advanced_eb_search |
    And the user inserts a value into a specific field
      | eb_uid | A002/Xba9cdb/X4ee4 |
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the specific data is collected for validation
      | exec_block |
    And scroll to specific element
      | toolBarButton | html_qa0_report |
    When the user clicks on a specific toolbar button
      | html_qa0_report |
    Then the new browser tab is available
      | qa0_html_report |
    And the QA0 Report html page is displayed
    And the QA0 Report html page contains the specific data
      | qa0_report |
      | exec_block |
