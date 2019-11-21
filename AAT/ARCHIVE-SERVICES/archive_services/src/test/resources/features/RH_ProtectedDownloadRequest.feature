@MultiBrowserTesting
Feature: RH: Protected Download Request


  @Regression @RH-Protected
  Scenario: Download proprietary data logged in as a non-proprietary user
    Given test environment is available
      | aq |
    And   user input value on field
      | project_code | full.data.proprietary.project.id |
    Then  results table is shown
    When  user select the first item and click 'Submit Download'
    Then  login page is displayed in a new tab
    When  user fills the credentials
      | user.roles.user |
    And   user clicks the LOGIN button
    Then  request tree is shown in the RH

    When  user click 'Download Selected'
    And   popup appears and user clicks 'File List'
    Then  ICT-12656: Only readme.txt can be download when data is not released






