@MultiBrowserTesting @qa0
Feature: QA0: Increase Execution Fraction cap in AQUA - https://ictjira.alma.cl/browse/ICT-13405

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
    And the user clicks on "QA0" tab
    Then the user is authenticated
      | username |

  @regression @regression-phaa-disabled @2018oct-disabled @sanity-disabled @regression-phab-disabled
  Scenario: Compute execution fraction for EB where the expected value is greater than 4
    When the QA0 view is displayed
    When the specific search tab is selected
      | advanced_eb_search |
    And the user inserts a value into a specific field
#    ADD VALID EB
      | eb_uid | uid://A002/Xd08a99/X1736 |
    And the user clicks on a specific button
      | search |
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    When the user clicks on a specific button
      | do_qa0 |
    Then the Do QA0 pop-up form is displayed
    And the user clicks on the Set QA0 Status
    And the user selects specific status
      | pass |
    And the user sets an EC value
      | 5 |
    When the user clicks on a specific button
      | compute_ef |
    And wait for the loading progress to be completed
#    And the EF value is capped to 4