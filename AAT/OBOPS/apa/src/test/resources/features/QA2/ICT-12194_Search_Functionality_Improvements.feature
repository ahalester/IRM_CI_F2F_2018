@MultiBrowserTesting @qa2
Feature: QA2: improvements to the search functionality - https://ictjira.alma.cl/browse/ICT-12194
#  https://ictjira.alma.cl/browse/ICT-12377

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
#    Given QA2 test environment is available
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

  @all @qa2 @2018mar
  Scenario Outline: Search by <Option>
    When the specific search tab is selected
      | advanced_ous_search |
    And the user inserts a value into a specific field
      | <Option> | <Value> |
    And select a specific suggestion
      | <Value> |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed

    Examples:
      | Option       | Value       |
      | data_reducer | suzannatest |
      | drm          | suzannatest |

  @all @qa2 @2018mar
  Scenario: Search by Data reducer and DRM
    When the specific search tab is selected
      | advanced_ous_search |
    And the user inserts a value into a specific field
      | data_reducer | suzannatest |
    And select a specific suggestion
      | suzannatest |
    And the user inserts a value into a specific field
      | drm | suzannatest |
    And select a specific suggestion
      | suzannatest |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed

  @all @qa2 @2018mar
  Scenario: Array options TM, 7M, TP are available in QA2 advanced search
    When the specific search tab is selected
      | advanced_ous_search |
    Then the user checks/un-checks a specific checkbox
      | array_tm | check |
      | array_7m | check |
      | array_tp | check |
    Then the user checks/un-checks a specific checkbox
      | array_tm | uncheck |
      | array_7m | uncheck |
      | array_tp | uncheck |

  @all @qa2 @2018mar
  Scenario: 'Text in comments' field available in QA2 advanced search
    When the specific search tab is selected
      | advanced_ous_search |
    Then the specific label is displayed
      | text_in_comments |
    When the user inserts a value into a specific field
      | text_in_comments | 12345abcdef test comment |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    Then the specific label is displayed
      | 12345abcdef test comment |
