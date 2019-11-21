@MultiBrowserTesting @qa0
Feature: QA0: Optimize QA0 Robot use - https://ictjira.alma.cl/browse/ICT-12061

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

  @all @regression-phaa @2018apr @regression-phab
  Scenario: Run QA0 Robot button is displayed and action performed
    And the QA0 view is displayed
    When the user clicks on a specific image
      | bulk-update-16.png |
    Then the specific button is displayed
      | run_qa0_robot |
    When the user clicks on a specific button
      | run_qa0_robot |
    And wait for element to be enabled
      | button | run_qa0_robot |
    And wait for dialog box no. "1" to be visible
    Then the confirmation dialog no. "1" contains the specific message
      | QA0Robot execution completed. |
    And the user clicks on a specific button
      | ok |
