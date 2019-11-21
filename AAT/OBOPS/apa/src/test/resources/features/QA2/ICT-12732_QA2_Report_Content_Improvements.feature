@MultiBrowserTesting @qa2
Feature: QA2: improvements to the QA2 report content - https://ictjira.alma.cl/browse/ICT-12732

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
    Then the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |

  @all @regression-phaa @2018jun @regression-phab
  Scenario: QA2 report contains instructions for Total Power observations
    When the user inserts a value into a specific field
      | sb_name_qa2 | TP |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific toolbar button
      | html_qa2_report |
    Then the QA2 Report html page is displayed
    And the QA2 Report html page contains the specific data
      | instructions |

  @all @regression-phaa @2018jun @regression-phab
  Scenario Outline: QA2 report contains instructions for <ProcessingFlag> Interferometric observations
    When the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    When the user selects a specific option
      | processing_flags | <ProcessingFlag> |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific toolbar button
      | html_qa2_report |
    Then the QA2 Report html page is displayed
    And the QA2 Report html page contains the specific data
      | instructions |

    Examples:
      | ProcessingFlag       |
      | pipeline_calibration |
      | pipeline_cal_and_img |
      | manual_calibration   |
#      | pipeline_imaging     |
