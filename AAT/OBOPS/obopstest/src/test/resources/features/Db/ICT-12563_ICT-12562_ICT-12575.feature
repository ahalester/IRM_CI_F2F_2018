@DbTest @2018may @regression-phaa
Feature: ICT-12563_ICT-12562_ICT-12575 - Enable Client ID for Oracle connection

  Scenario: [ICT-12563] Enable Client ID for Oracle connection - protrack
    When Verify Enabled Client ID for Oracle DB connection
      |select * from v$session where program = 'obops.protrack'|

  Scenario: [ICT-12562] Enable Client ID for Oracle connection - ph1m
    When Verify Enabled Client ID for Oracle DB connection
      |select * from v$session where program = 'obops.ph1m'|

  Scenario: [] Enable Client ID for Oracle connection - aqua
    When Verify Enabled Client ID for Oracle DB connection
      |select * from v$session where program = 'obops.aqua'|

  Scenario: [ICT-12575] Enable Client ID for Oracle connection - xtss
    When Verify Enabled Client ID for Oracle DB connection
      |select * from v$session where program = 'obops.xtss'|
