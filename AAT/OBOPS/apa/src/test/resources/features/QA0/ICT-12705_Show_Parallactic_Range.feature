@MultiBrowserTesting @qa0
Feature: QA0: AQUA: Polarization: Session reports wrong parallactic range for SBs - https://ictjira.alma.cl/browse/ICT-12705

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
    And the QA0 view is displayed

  @all @regression-phaa @2018jun @regression-phab
  Scenario Outline: Session reports correct parallactic range for SB <SBuid>
    When the specific search tab is selected
      | advanced_eb_search |
    And the user inserts a value into a specific field
      | eb_uid | <SBuid> |
    Then the user checks if the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And scroll to specific label
      | parallactic_range |
    And the specific label is displayed
      | parallactic_range |

    Examples:
      | SBuid              |
      | A002/Xcca365/X2398 |
      | A002/Xcca365/X1f60 |
      | A002/Xcca365/X1890 |