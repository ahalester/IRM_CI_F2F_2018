@MultiBrowserTesting @integration


Feature: Integration testing feature

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/project.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties


  Scenario: Integration test scenario
    Given go to Protrack State Change test environment
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    And the Protrack State Change test environment is available
    And enter OUS Status UID
      | uid://A001/X12a2/X293 |
    And select OUS target state
      | Reviewing |
    And select OUS flag
      | PipelineCalAndImg |
    And click OUS Change button
    And successful state change
    #Protrack steps
    And the Protrack test environment is available
    And the user is authenticated
      | username |
    And open OUS search tab
    And enter OUS Status UID in search field
      | uid://A001/X12a2/X293 |
    And click search for OUS
    And verify OUS UID on Entity page
      | uid://A001/X12a2/X293 |
    And click OUS AQUA link
    And switch to second tab
    And wait for AQUA OUS page to load
    #AQUA steps
    And the user clicks on a specific button
      | do_qa2 |
    And the Do QA2 pop-up form is displayed
    And the user clicks on the Set QA2 Status
    And the user selects specific status
      | pass  |
    And the user adds a QA2 comment
      | final_qa2_comment | Test comment |
    And the user clicks on a specific button on Do QA2 page
      | save |
    Then the confirmation pop-up is displayed
    And the confirmation dialog contains the specific message
      | The state will be changed to Verified (PipelineCalAndImg) Do you want to proceed ? |
    And the user clicks one of the popup's buttons
      | Yes |
    And the confirmation dialog contains the specific message
      | Products have already been ingested for this OUS. Would you like to replace them? |
    And the user clicks one of the popup's buttons
      | Yes |
    And the confirmation dialog contains the specific message
      | ObsUnitSet Member OUS (Lupus_3_MMS) has been automatically changed to Verified |
    And the user clicks one of the popup's buttons
      | OK |
    Then the OUS state is changed
      | State | DeliveryInProgress [IngestionTriggered] |
#    Then verify OUS state
#      | DeliveryInProgress [IngestionTriggered] |




