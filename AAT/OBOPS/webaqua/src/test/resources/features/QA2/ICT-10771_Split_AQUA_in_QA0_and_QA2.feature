@MultiBrowserTesting @qa0 @qa2
Feature: Split AQUA in QA0 and QA2 - https://ictjira.alma.cl/browse/ICT-10771

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

#  @all @smoke @sanity
#  Scenario Outline: <Tab> tab is not visible in the <Tool> tool environment
#    Given the <Tool> test environment is available
#    And AQUA login page is displayed
#    When the user fills the credentials
#      | username |
#      | password |
#    And the user clicks the LOGIN button
#    Then the user is authenticated
#      | username |
#    And the <Tab> tab is not displayed
#
#    Examples:
#      | Tool  | Tab   |
#      | "QA0" | "QA2" |
#      | "QA2" | "QA0" |
