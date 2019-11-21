@MultiBrowserTesting @protrack @2018aug @regression-phaa @regression-phab


Feature: ICT-13017 - Saved search

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/project.properties
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


  Scenario: ICT-13017 - Saved search error
    Given open OUS search tab
    When Select OUS status in OUS search page
      | FullyObserved |
    And Save OUS search
      | savedSearch1 |
    And Reset OUS search
    And Select OUS saved search
      | savedSearch1 |
    Then Perform OUS search
    And protrack project search results found