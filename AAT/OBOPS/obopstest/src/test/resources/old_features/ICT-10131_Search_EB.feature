@MultiBrowserTesting @qa0
Feature: Show both BL and ACA QA0 UNSET EBs by default - https://ictjira.alma.cl/browse/ICT-10131

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
    Given QA0 test environment is available
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the QA0 view is displayed
    And the user is authenticated
      | username |

  @all @smoke @regression @sanity
  Scenario: The EB Search and Advanced EB Search tabs are available
    When the specific search tab is selected
      | eb_search |
    Then the specific elements are available
      | bl          | checkbox |      |
      | aca         | checkbox |      |
      | science     | checkbox |      |
      | calibration | checkbox |      |
      | unset       | checkbox |      |
      | pending     | checkbox |      |
      | pass        | checkbox |      |
      | fail        | checkbox |      |
      | semi_pass   | checkbox |      |
      | from        | datebox  | 2011 |
      | to,datebox  | today    |      |
      | search      | button   |      |
      | reset       | button   |      |
    When the specific search tab is selected
      | advanced_eb_search |
    Then the specific elements are available
      | project     | textbox |  |
      | eb_uid      | textbox |  |
      | sb_name_qa0 | textbox |  |

  @all @regression @sanity
  Scenario: BL and ACA boxes are ticked by default to show all Unset Science EBs
    When the specific search tab is selected
      | eb_search |
    Then the specific elements are available
      | bl      | checkbox |  |
      | aca     | checkbox |  |
      | science | checkbox |  |
      | unset   | checkbox |  |
    And the specific checkbox is checked/unchecked
      | bl      | checked |
      | aca     | checked |
      | science | checked |
      | unset   | checked |
