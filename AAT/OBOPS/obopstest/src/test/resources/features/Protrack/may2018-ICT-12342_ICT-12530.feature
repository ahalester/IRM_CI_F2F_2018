@MultiBrowserTesting @protrack @2018may @regression-phaa @regression-phab
Feature: ICT-12342_ICT-12530

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

  @RestApiTest @skip
  Scenario: ICT-12342 - Prefix with tool identifier in state change history - XTSS and verify in Protrack
    Given find ObsUnitSet with State
      |   Processing  |
    When transition OUS state
      | ous_session_data |  Waiting  |
    And open OUS search tab
    And enter OUS Status UID in search field
      | ous_session_data |
    And click search for OUS
    And verify OUS UID on Entity page
      | ous_session_data |
    Then verify state for OUS in Protrack
      | Waiting |
    And verify OUS status history info contains
      | (XTSS) auto test |


  @RestApiTest
  Scenario: ICT-12342 - Prefix with tool identifier in state change history - Protrack
    Given find ObsUnitSet with State
      |   Processing  |
    And open OUS search tab
    And enter OUS Status UID in search field
      | ous_session_data |
    And click search for OUS
    And verify OUS UID on Entity page
      | ous_session_data |
    When change OUS state in Protrack
      | Waiting | Waiting |
    Then verify state for OUS in Protrack
      | Waiting |
    And verify OUS status history info contains
      | (PT) auto test |


  Scenario: ICT-12530 - Schedulable Dates SB query and Time Constraint cause Search error.
    Given open SB search tab
    When enter SB Schedulable dates interval selection From To
      | 2016/07/26 14:15 PM | 2018/05/21 14:16 PM |
    And click search for SB
    Then verify SB search results are found

