@MultiBrowserTesting
Feature: RH: Unprotected Data Download Request


  @Regression @RH-Unprotected
  Scenario: Download proprietary data logged in as a OPERATOR user
    Given test environment is available
      | aq |
    And   user input value on field
      | project_code | full.data.proprietary.project.id |
    Then  results table is shown
    When  user select the first item and click 'Submit Download'
    Then  login page is displayed in a new tab
    When  user fills the credentials
      | a-operator.sc-admin.roles.user |
    And   user clicks the LOGIN button
    Then  request tree is shown in the RH

    # This step is downloading scripts that may be later used by the DP suite
    When  user select first item and click 'Download Selected'
    And   popup appears and user clicks 'Download Script'
    Then  download script is available and prepared for integration tests
      | full.data.proprietary.project.id |





