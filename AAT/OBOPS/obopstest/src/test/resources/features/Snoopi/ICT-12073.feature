  @MultiBrowserTesting @snoopi

  Feature: ICT-12073: export list of projects and SBs with the real status for CSs
    #@2018oct @regression-phaa @regression-phab

    Background:
      Given go to Snoopi test environment
      And CAS login page is displayed
      And Change user password digest
        | akawamura | 47bce5c74f589f4867dbd57e9ca9f808 |
      When the user fills the credentials
        | akawamura |
        | ci3GjTuj/x8= |
      And the user clicks the LOGIN button Snoopi
      Then generic user is authenticated to Snoopi


    Scenario: ICT-12073: export list of projects and SBs with the real status for CSs- validate Export button
      Given the Snoopi view is displayed
      And User info contains
      | Akiko Kawamura |
      And Go to My Projects Snoopi
      When Select contact scientist checkbox Snoopi
      Then Validate Export button is displayed Snoopi
