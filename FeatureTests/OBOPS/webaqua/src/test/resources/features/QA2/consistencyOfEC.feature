@MultiBrowserTesting
Feature: Consistent EC between AQUA, PT and Scheduler - https://ictjira.alma.cl/browse/ICT-10230

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties

  Background:
    Given that the test environment is available
    And that the aqua login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    And the user is authenticated
      | username |

  @smoke
  Scenario: The EC is consistent between AQUA, PT and Scheduler
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
      | OUS Search |
    When the user clicks on a specific field
      | OUS State |
    Then the available options are displayed
    When the user selects a specific option
      | OUS State flag | ReadyForReview (Pipeline) |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
#    When the user selects one of the available OUS
#      | 2016.1.00314.S |
    Then the OUS Summary details page is displayed
    When the user clicks on a specific button
      | Do QA2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | Fail (re-observe) |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | Yes |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on QA2 Status Reason
    Then the QA2 Status Reason options are displayed
    And the user selects one QA2 Reason status by position
#    If the value is 0, empty or non-numerical, the first element on the list will be selected
      |  |
    And the user sets an EC value
    And the user adds a QA2 comment
      | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | Save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | Yes |
    Then the OUS Summary details page is displayed
#    TODO - to be continued
