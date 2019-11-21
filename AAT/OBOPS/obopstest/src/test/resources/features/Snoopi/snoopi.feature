  @MultiBrowserTesting @snoopi @2018oct @regression-phaa

  Feature: ICT-9206: Port SnooPI to the latest version of Angular
    # @regression-phab

    Background:
      Given go to Snoopi test environment
      And CAS login page is displayed
      And Change user password digest
        | weiwang | 47bce5c74f589f4867dbd57e9ca9f808 |
      When the user fills the credentials
        | weiwang |
        | ci3GjTuj/x8= |
      And the user clicks the LOGIN button Snoopi
      Then generic user is authenticated to Snoopi

    Scenario: Snoopi Homepage validation
      Given the Snoopi view is displayed
      When User info contains
        | Wei Wang |
      Then Validate home page Snoopi
      And Click PI Projects widget Snoopi
      And Go to Home page Snoopi
      And Click Co-I Projects widget Snoopi
      And Go to Home page Snoopi
      And Click PI SBS widget Snoopi
      And Go to Home page Snoopi
      And Click Co-I SBS widget Snoopi

    Scenario: Snoopi My Projects validation
      Given the Snoopi view is displayed
      When Go to My Projects Snoopi
      Then Select random Project Snoopi

    Scenario: Snoopi My SBs validation
      Given the Snoopi view is displayed
      When Go to My SBs Snoopi
      Then Select random SB Snoopi



