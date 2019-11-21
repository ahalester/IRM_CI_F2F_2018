@DbTest
Feature: Enable Client ID for Oracle connection - https://ictjira.alma.cl/browse/ICT-12561

  @2018may @regression-phaa @sql
  Scenario: Enable Client ID for Oracle connection - aqua
    When Execute DB query and validate response
      | ORACLE_CLIENT_ID |