@MultiBrowserTesting
Feature: Ask whether data reducer should be unset - https://ictjira.alma.cl/browse/ICT-9983

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
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the specific search tab is selected
#      | OUS Search |
      | Advanced OUS Search |
    When the user inserts a value into a specific field
      | Data reducer | a |
    And the user clicks on a specific field
      | OUS State |
    Then the available options are displayed

  @smoke
  Scenario: The DR should remain assigned to the OUS when selecting Fail (re-observe) status and choose to keep the DR
    When the user selects a specific option
      | OUS State | DeliveryInProgress |

#    FIXME - to be completed
#    Then the specific OUS State flag is selected
#      | OUS State | DeliveryInProgress |

#    When the user clicks on the OUS State flag field
#    Then the OUS State flag options are displayed
#    When the user selects a specific OUS State flag
#      | ReadyForReview (Pipeline) |
#    Then the specific OUS State flag is selected
#      | ReadyForReview (Pipeline) |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
#    When the user selects one of the available OUS
#      | 2016.1.00314.S |
    Then the OUS Summary details page is displayed
    And the specific data is collected for validation
      | Data reduction methods |
    And the specific data is collected for validation
      | Data reducer |
    And the Data reduction methods are displayed
      | Data reduction methods |
    When the user clicks on a specific button
      | Do QA2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | Fail (re-observe) |
    Then the confirmation pop-up is displayed
    And the confirmation dialog contains the specific message
      | Should the data reducer stay assigned to this OUS? |
    When the user clicks one of the popup's buttons
      | Yes |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on QA2 Status Reason
    Then the QA2 Status Reason options are displayed
    And the user selects one QA2 Reason status by position
#    If the value is 0, empty or non-numerical, the first element on the list will be selected
      |  |
    And the user sets an EC value
    When the user clicks on a specific button on Do QA2 page
      | Save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | Yes |
    Then the OUS Summary details page is displayed
    And the Data reduction methods are displayed
      | Data reduction methods |
    And the Data reducer is displayed
      | Data reducer |

  @smoke
  Scenario: The DR should be removed from the OUS when selecting Fail (re-observe) status and choose to remove the DR
    When the user selects a specific option
      | OUS State | DeliveryInProgress |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific data is collected for validation
      | Data reduction methods |
    And the specific data is collected for validation
      | Data reducer |
    And the Data reduction methods are displayed
      | Data reduction methods |
    When the user clicks on a specific button
      | Do QA2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | Fail (re-observe) |
    Then the confirmation pop-up is displayed
    And the confirmation dialog contains the specific message
      | Should the data reducer stay assigned to this OUS? |
    When the user clicks one of the popup's buttons
      | No |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on QA2 Status Reason
    Then the QA2 Status Reason options are displayed
    And the user selects one QA2 Reason status by position
#    If the value is 0, empty or non-numerical, the first element on the list will be selected
      |  |
    And the user sets an EC value
    When the user clicks on a specific button on Do QA2 page
      | Save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | Yes |
    And the Data reduction methods are displayed
      | Data reduction methods |
    And the Data reducer is no longer displayed
      | Data reducer |

  @smoke
  Scenario: The DR should remain assigned to the OUS when selecting Semi-Pass status and choose to keep the DR
    When the user selects a specific option
      | OUS State | Reviewing |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    And the specific data is collected for validation
      | Data reduction methods |
    And the specific data is collected for validation
      | Data reducer |
    And the Data reduction methods are displayed
      | Data reduction methods |
    When the user clicks on a specific button
      | Do QA2 |
    Then the Do QA2 pop-up form is displayed
    When the user clicks on the Set QA2 Status
    And the user selects specific status
      | Semi-Pass |
    When the user clicks on QA2 Status Reason
    Then the QA2 Status Reason options are displayed
    And the user selects one QA2 Reason status by position
#    If the value is 0, empty or non-numerical, the first element on the list will be selected
      |  |
    And the user adds a QA2 comment
      | Final QA2 Comment | Test comment |
    When the user clicks on a specific button on Do QA2 page
      | Save |
    Then the confirmation pop-up is displayed
    When the user clicks one of the popup's buttons
      | Yes |
#    And the user clicks one of the popup's buttons
#      | Yes |
    And the Data reduction methods are displayed
      | Data reduction methods |
    And the Data reducer is displayed
      | Data reducer |

#  @smoke
#  Scenario: The Data Reducer should be removed from the OUS when selecting Semi-Pass status and choose to
#  keep the DR
#    And the user selects specific status
#      | Semi-Pass |
#    When the user clicks on QA2 Status Reason
#    Then the QA2 Status Reason options are displayed
#    And the user selects one QA2 Reason status by position
##    If the value is 0, empty or non-numerical, the first element on the list will be selected
#      |  |
##    And the user adds a QA2 comment
##      | Test comment |
#    When the user clicks on a specific button on Do QA2 page
#      | Save |
#    Then the confirmation pop-up is displayed
#    When the user clicks one of the popup's buttons
#      | No |
#    Then the OUS Summary details page is displayed
#    And the Data reduction methods are displayed
#      | Data reduction methods |
#    And the Data reducer is no longer displayed
#      | Data reducer |