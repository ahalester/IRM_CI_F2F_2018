@MultiBrowserTesting @protrack @2018jul @regression-phaa @regression-phab
Feature: ICT-12917 - Improvement to Protrack GUI: Link to parent/child entities

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
   And Init xtss rest


  @RestApiTest
  Scenario: ICT-12917 - Navigate between parent and child entities
    Given find ObsUnitSet with State
      |   Waiting  |
    And open OUS search tab
    And enter OUS Status UID in search field
      | ous_session_data |
    And click search for OUS
    And verify OUS UID on Entity page
      | ous_session_data |
    When navigate to Child SchedBlock on Entity page
    And go to parent OUS on Entity page
    Then verify OUS UID on Entity page
      | ous_session_data |



