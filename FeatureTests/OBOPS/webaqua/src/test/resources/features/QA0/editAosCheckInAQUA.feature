@MultiBrowserTesting
Feature: Display and allow editing of AOS check output in AQUA - https://ictjira.alma.cl/browse/ICT-10247

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties

  Background:
    Given that the test environment is available
    And that the aqua login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the user is authenticated
      | username |

#  Use "Re-run AOS check" button. A progress icon will be displayed.
  @smoke
  Scenario: The AOS check json data button appears after user clicks on Re-run AOS check
    When the specific search tab is selected
      | EB Search |
    Then the specific elements are available
      | BL,checkbox          |
      | ACA,checkbox         |
      | Science,checkbox     |
      | Calibration,checkbox |
      | Unset,checkbox       |
      | Pending,checkbox     |
      | Pass,checkbox        |
      | Fail,checkbox        |
      | SemiPass,checkbox    |
      | From,datebox,2011    |
      | To,datebox,today     |
      | Other,combobox       |
      | Search,button        |
      | Reset,button         |
    And the user checks/unchecks the checkbox
      | uncheck,id,zk_c_63-real |
      | uncheck,id,zk_c_64-real |
      | uncheck,id,zk_c_71-real |
      | uncheck,id,zk_c_72-real |
      | check,id,zk_c_76-real   |
      | uncheck,id,zk_c_77-real |
      | uncheck,id,zk_c_78-real |
      | uncheck,id,zk_c_79-real |
      | uncheck,id,zk_c_80-real |
    When the user clicks on a specific button
      | Search |
    Then the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the user selects the next EB if the Re-run AOS check was already triggered
      | AOS check json data |
    When the user clicks on a specific button
      | Re-run AOS check |
    Then the specific button is displayed
      | AOS check json data |

#  Use "Re-run AOS check" button. A progress icon will be displayed.
  @smoke
  Scenario: The new Weather information is displayed after user clicks on Re-run AOS check
    When the specific search tab is selected
      | EB Search |
    And the user checks/unchecks the checkbox
      | uncheck,id,zk_c_63-real |
      | uncheck,id,zk_c_64-real |
      | uncheck,id,zk_c_71-real |
      | uncheck,id,zk_c_72-real |
      | check,id,zk_c_76-real   |
      | uncheck,id,zk_c_77-real |
      | uncheck,id,zk_c_78-real |
      | uncheck,id,zk_c_79-real |
      | uncheck,id,zk_c_80-real |
    And the user clicks on a specific button
      | Search |
    Then the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the user selects the next EB if the Re-run AOS check was already triggered
      | AOS check json data |
    And the specific data is collected for validation
      | Weather |
    When the user clicks on a specific button
      | Re-run AOS check |
    Then the specific button is displayed
      | AOS check json data |
    And the data was successfully retrieved
      | Weather | QA0 Status |
    And the user checks if the specific data is different
      | Weather |

#  Use "Re-run AOS check" button. A progress icon will be displayed.
  @smoke
  Scenario: The Final QA0 Comment is displayed after user clicks on Re-run AOS check
    When the user clicks on a specific button
      | Search |
    Then the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the user selects the next EB if the Re-run AOS check was already triggered
      | AOS check json data |
    And the specific data is collected for validation
      | Final QA0 Comment |
    When the user clicks on a specific button
      | Re-run AOS check |
    Then the specific button is displayed
      | AOS check json data |
    And the data was successfully retrieved
      | Final QA0 Comment | Total duration |
    And the user checks if the specific data is different
      | Final QA0 Comment |

#  new information in the "Weather" section will be added;
#  new information will be appended to the "Final QA0 Comment".
#  new numerical columns (if there is data) can be added to the "Source summary" table.
#  below "Source summary" table "Bandpass calibrator maximum number of channels" and "Phase cal/science completed cycles" will have values (if the data available).
  @smoke
  Scenario: The ExecBlock Summary data was successfully retrieved after the user clicks on Re-run AOS check
    When the user clicks on a specific button
      | Search |
    Then the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the specific button is displayed
      | AOS check json data |
#    TODO - replace the content from the second column with data that should appear after the re-run AOS check
#    Or leave it like this if you want to validate that the next available label after the one in the first column is
#    not the one from the second column
#    If the test should fail when the condition is not met, the Assume should be replaced with an Assert and the LOG
#    as well
    And the data was successfully retrieved
      | Weather                                         | QA0 Status                          |
      | Final QA0 Comment                               | Total duration                      |
      | Bandpass calibrator maximum number of channels: | Phase cal/science completed cycles: |
      | Phase cal/science completed cycles:             | Comments                            |
    And the data grid was successfully retrieved
#    TODO - replace "Total duration" with specific values that have to be validated
      | Source summary | Total duration |

#  There is a new QA0 Flags tab. It has a text area with QA0 Pipeline flags information.
  @smoke
  Scenario: The QA0 Flags view has a text area with QA0 Pipeline flags info after the user clicks on Re-run AOS check
    When the user clicks on a specific button
      | Search |
    Then the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    And the specific data is collected for validation
      | ExecBlock |
    When the user clicks on a specific tab
      | QA0 Flags |
    Then the QA0 Flags view is displayed
      | ExecBlock |

#  Phase tab at the very end will have the "Aoscheck Phase" table with some values.
  @smoke
  Scenario: The Phase view has the Aoscheck Phase table with some values after the user clicks on Re-run AOS check
    When the user clicks on a specific button
      | Search |
    Then the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    When the user clicks on a specific tab
      | Phase |
    And the data grid was successfully retrieved
#    TODO - replace "Antenna-based phase differences on PhaseCal [deg]:" with specific values that have to be validated
      | Aoscheck Phase | Antenna-based phase differences on PhaseCal [deg]: |

#  Baseline Coverage tab: "Array summary" table can have one new row "Antennas with issues (aoscheck):".
  @smoke
  Scenario: The Baseline Coverage view can have a new row "Antennas with issues (aoscheck)" into the Array summary
  table after the user clicks on Re-run AOS check
    When the user clicks on a specific button
      | Search |
    Then the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    When the user clicks on a specific tab
      | Baseline Coverage |
    And the data grid was successfully retrieved
#    TODO - replace "Antennas with issues (aoscheck):" with specific values that have to be validated
      | Array Summary | Antennas with issues (aoscheck): |

#  Baseline Coverage tab:  Antenna positions plot have new type of points: "Antennas with issues", marked with red color.
  @smoke
  Scenario: The Antenna positions plot can have new type of points: "Antennas with issues", marked with red color after
  the user clicks on Re-run AOS check
    When the user clicks on a specific button
      | Search |
    Then the EBs are displayed
    When the user selects the first available EB
    Then the EB Summary details page is displayed
    When the user clicks on a specific tab
      | Baseline Coverage |
#    TODO - to be continued
    Then the legend item is displayed
      | red | Antennas with issues |