@MultiBrowserTesting @protrack @2018apr @regression-phaa @regression-phab
Feature: Protrack sanity test suite

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
    Given the Protrack test environment is available
    And Protrack login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the user is authenticated
      | username |
    And the Protrack view is displayed
      | img_protrack |


  Scenario: Search for a specific project code
    Given the user enters a value into a specific field
      | alma_project_tracker_search | 2017.1.01718.S |
    Then select protrack project by text
      | 2017.1.01718.S |
    And the user clicks on a specific toolbar button
      | logout |

#  Scenario: Search for a specific project name
#    Given the user enters a value into a specific field
#     | alma_project_tracker_search | Outflow Feedback from a Massive Protostar |
#    Then select protrack project by text
#      | Outflow Feedback from a Massive Protostar |
#    And the user clicks on a specific toolbar button
#      | logout |

  Scenario: Search for a specific project code
    Given the user enters a value into a specific field
      | alma_project_tracker_search | E2E5.1.00042.S |
    Then protrack project search results found
    Then select protrack project by text
      | E2E5.1.00042.S |
    And the user clicks on a specific toolbar button
      | logout |

  Scenario: Search for a project on a specific state
    Given the user enters a value into a specific field
      | alma_project_tracker_search| 2017 |
    Then protrack project search results found
    And select protrack project by state
      | Phase2Submitted |
    And the user clicks on a specific toolbar button
      | logout |

  Scenario: Search for a project on a specific state
    Given the user enters a value into a specific field
      | alma_project_tracker_search| 2017 |
    Then protrack project search results found
    And select protrack project by state
      | Phase1Submitted |
    And the user clicks on a specific toolbar button
      | logout |

  Scenario: Search for a project on a specific state
    Given the user enters a value into a specific field
      | alma_project_tracker_search| 2017 |
    Then protrack project search results found
    And select protrack project by state
      | InProgress |
    And the user clicks on a specific toolbar button
      | logout |

  Scenario: Search for a project on a specific state
    Given the user enters a value into a specific field
      | alma_project_tracker_search| 2017 |
    Then protrack project search results found
    And select protrack project by state
      | Ready |
    And the user clicks on a specific toolbar button
      | logout |

  Scenario: Search for a project on a specific state
    Given the user enters a value into a specific field
      | alma_project_tracker_search| 2017 |
    Then protrack project search results found
    And select protrack project by state
      | Completed |
    And the user clicks on a specific toolbar button
      | logout |

  Scenario: Search for a project on a specific state
    Given the user enters a value into a specific field
      | alma_project_tracker_search| 2017 |
    Then protrack project search results found
    And select protrack project by state
      | Rejected |
    And the user clicks on a specific toolbar button
      | logout |



