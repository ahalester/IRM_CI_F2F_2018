@MultiBrowserTesting
Feature: Simple Google search test

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/project.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  @2018may
  Scenario: Open new google page and perform a simple search
    Given the user navigates to a specific URL
      | google_url |
    When the test environment is available
    Then the user inserts text in the user search field
      | test automation |
    And the user clicks on a specific button
      | Google-Suche |