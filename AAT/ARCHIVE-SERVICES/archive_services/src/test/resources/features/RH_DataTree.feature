@MultiBrowserTesting
Feature: RH: Data Tree

  @Regression @RH-DataTree
  Scenario: Request Handler data tree is displayed and preselected correctly
    Given test environment is available
      | aq |
    And user input value on field
      | project_code | proprietary.project.id |
    Then results table is shown
    When user select the first item and click 'Submit Download'
    Then login page is displayed in a new tab

    # Take a user with enough privileges to see unreleased data to feature in place
    When user fills the credentials
      | a-operator.sc-admin.roles.user |
    And  user clicks the LOGIN button
    Then request tree is shown in the RH
    Then ICT-13005: readme, product and auxiliary data is preselected
    Then ICT-13217: displayed total size is the sum of individual file sizes
    When SBs are expanded
    Then ICT-12977: the source list is unique
    And  ICT-12656: Product member files can be individually downloaded