@MultiBrowserTesting
Feature: Display the aqua login page and authenticate

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties

  Background:
    Given that the test environment is available

  @smoke
  Scenario: Display the QA2 page and and check if the OUS summary page is displayed
    Given that the aqua login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user checks the warn checkbox
    And the user unchecks the warn checkbox
    And the user clicks the LOGIN button
    Then the QA0 view is displayed
    And the user is authenticated
      | username |
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    And the user checks/unchecks the checkbox
      | check,id,zk_c_193-real   |
      | check,id,zk_c_195-real   |
      | check,id,zk_c_196-real   |
      | check,id,zk_c_204-real   |
      | check,id,zk_c_205-real   |
      | check,id,zk_c_206-real   |
      | check,id,zk_c_207-real   |
      | check,id,zk_c_210-real   |
      | check,id,zk_c_211-real   |
      | check,id,zk_c_212-real   |
      | check,id,zk_c_219-real   |
      | check,id,zk_c_220-real   |
      | check,id,zk_c_221-real   |
      | check,id,zk_c_222-real   |
      | check,id,zk_c_195        |
      | uncheck,id,zk_c_193-real |
      | uncheck,id,zk_c_195-real |
      | uncheck,id,zk_c_196-real |
      | uncheck,id,zk_c_204-real |
      | uncheck,id,zk_c_205-real |
      | uncheck,id,zk_c_206-real |
      | uncheck,id,zk_c_207-real |
      | uncheck,id,zk_c_210-real |
      | uncheck,id,zk_c_211-real |
      | uncheck,id,zk_c_212-real |
      | uncheck,id,zk_c_195      |
      | uncheck,id,zk_c_219-real |
      | uncheck,id,zk_c_220-real |
      | uncheck,id,zk_c_221-real |
      | uncheck,id,zk_c_222-real |
    And the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
