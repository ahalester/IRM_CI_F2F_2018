@DbTest
Feature: Inconsistent achieved rms values between AQUA and PT - https://ictjira.alma.cl/browse/ICT-12028

  @2018may @regression-phaa @sql
  Scenario: Enable Client ID for Oracle connection - aqua
    When Execute DB query and validate response
      | RMS_VALUES |