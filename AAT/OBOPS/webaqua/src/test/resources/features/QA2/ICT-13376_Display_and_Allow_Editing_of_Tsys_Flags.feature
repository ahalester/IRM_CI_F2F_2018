@MultiBrowserTesting @qa2
Feature: QA2: display and allow editing of Tsys flags - https://ictjira.alma.cl/browse/ICT-13376

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
    Given the test environment is available
      | QA2 |
    And AQUA login page is displayed
    When the user fills the credentials
      | username_obops |
      | password_obops |
    And the user clicks the LOGIN button
    Then the user is authenticated
      | username_obops |
    When the user clicks on QA2 tab
    And the QA2 view is displayed
    And the specific search tab is selected
      | advanced_ous_search |
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state | reviewing |
    And the user clicks on a specific field
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_calibration |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | pipeline_calibration |
    Then the specific button is displayed
      | finish_pl_cal_review |
    When the user clicks on a specific button without wait
      | finish_pl_cal_review |
    And the user clicks on a specific combobox
      | next_step |
    Then the user selects a specific combobox item
      | pl_calibrate |
    When the user clicks on a specific text tab
      | builder |

  @all @2018oct @regression-phab @sanity
  Scenario: Manage Tsys flags elements
    And the user clicks on a specific toolbar button
      | manage_tsys_flags |
    Then the new pop-up is displayed and the options are available
      | antennas_popup         |
      | spectral_windows_popup |
      | fields                 |
      | scans                  |
      | time_range             |
      | baseline               |
      | reason                 |
      | flags                  |
    And expand the specific dropdown list
      | baseline | 0 |
      | baseline | 1 |
      | reason   | 0 |
      | reason   | 1 |
      | reason   | 2 |
    And the specific button is displayed
      | save_flag |
    And the specific label is displayed
      | pl_stage  |
      | y_axis    |
      | x_axis    |
      | antenna_1 |
      | antenna_2 |
    And the user clicks on a specific button
      | cancel |

  @all @regression-phab @sanity @regression @2018oct
  Scenario: Save then Delete Tsys Flag
    And the user clicks on a specific toolbar button
      | manage_tsys_flags |
    Then select a specific builder area checkbox
      | antennas_popup | DA54 |
    And select a specific spectral window
      | spectral_windows_popup | 0 |
    And select specific checkbox from the check list
      | fields | 0 |
    And select a specific builder area checkbox
      | scans | 1 |
    And scroll to specific element
      | button | save_flag |
    And expand the specific dropdown list and select item
      | baseline | 0 | DA43      |
      | baseline | 1 | DA45      |
      | reason   | 0 | applycal  |
      | reason   | 1 | amplitude |
      | reason   | 2 | antenna1  |
    And the specific button is displayed
      | save_flag |
    And collect the current number of saved flags
      | flags |
    When the user clicks on a specific button
      | save_flag |
    Then the flag was saved/deleted
      | flags | saved |
    And collect the current number of saved flags
      | flags |
    When the user deletes the saved flag
      | flags |
    Then the flag was saved/deleted
      | flags | deleted |
    And the user clicks on a specific button
      | save |

  @all @regression-phab @2018oct @sanity @regression
  Scenario: Flagging by channels for Tsys
    And the user clicks on a specific toolbar button
      | manage_tsys_flags |
    Then select a specific builder area checkbox
      | antennas_popup | DA43 |
    And select a specific spectral window
      | spectral_windows_popup | 20 |
    And set the specific spectral window channel range
      | 20 | 0 | 100 |
    And the user clicks on a specific button
      | add |
    And scroll to specific element
      | button | save_flag |
    And expand the specific dropdown list and select item
      | reason | 0 | applycal  |
      | reason | 1 | phase     |
      | reason | 2 | frequency |
    And the specific button is displayed
      | save_flag |
    And collect the current number of saved flags
      | flags |
    When the user clicks on a specific button
      | save_flag |
    Then the flag was saved/deleted
      | flags | saved |
    And collect the current number of saved flags
      | flags |
    And the specific label is displayed
      | mode='manual' antenna='DA43' spw='            |
      | :0~100' reason='Tsys:applycal_phase_frequency' |

  @all @regression-phab @sanity
  Scenario: Flagging by baseline for Tsys
    And the user clicks on a specific toolbar button
      | manage_tsys_flags |
    Then select a specific builder area checkbox
      | antennas_popup | DA43 |
      | antennas_popup | DA48 |
      | antennas_popup | DA52 |
    Then scroll to specific element
      | button | save_flag |
    And expand the specific dropdown list and select item
      | baseline | 0 | DA52     |
      | baseline | 1 | DA55     |
      | reason   | 0 | applycal |
      | reason   | 1 | phase    |
      | reason   | 2 | time     |
    And the specific button is displayed
      | save_flag |
    And collect the current number of saved flags
      | flags |
    When the user clicks on a specific button
      | save_flag |
    Then the flag was saved/deleted
      | flags | saved |
    And collect the current number of saved flags
      | flags |
#    And scroll to specific label
#      | mode='manual' antenna='DA51&amp;DA55;DA43,DA48,DA52' reason='Tsys:applycal_phase_time' |
    And the specific label is displayed
      | mode='manual' antenna='DA52&amp;DA55;DA43,DA48,DA52' reason='Tsys:applycal_phase_time' |