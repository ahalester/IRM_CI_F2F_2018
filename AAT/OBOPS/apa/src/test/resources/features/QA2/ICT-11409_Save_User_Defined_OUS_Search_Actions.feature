@MultiBrowserTesting @qa2
Feature: QA2: save user-defined OUS searches - https://ictjira.alma.cl/browse/ICT-11409

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
#    Given QA2 test environment is available
    Given the test environment is available
      | QA2 |
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the user is authenticated
      | username |
    When the user clicks on QA2 tab
    Then the QA2 view is displayed
    When the specific search tab is selected
      | advanced_ous_search |

  @all @qa2 @smoke
  Scenario: Save user-defined OUS search
    Then the user inserts a value into a specific field
      | data_reducer | username |
    And the user inserts a value into a specific field
      | drm | username |
    And the user inserts a value into a specific field
      | sb_name_qa2 | test_ |
    And the user inserts a value into a specific field
      | ous_status_id | test_ |
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state | reviewing |
    When the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_calibration |
    When the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_imaging |
    When the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | manual_calibration |
    When the user clicks on a specific field
      | observing_modes |
    Then the available options are displayed
      | observing_modes |
    And the user selects a specific option
      | observing_modes | long_baseline |
    When the user clicks on a specific field
      | data_reduction_methods |
    Then the available options are displayed
      | data_reduction_methods |
    Then the user selects a specific option
      | data_reduction_methods | pipeline_calibration_standard |
#    And the user clicks on a specific checkbox
#      | unset |
    And the user clicks on a specific combobox
      | time_in_current_state |
    And the user selects a specific combobox item
      | 30 |
#    And the user clicks on a specific checkbox
#      | jao |
    And the user inserts a value into a specific field
      | project_code | 2017.1 |
    And the user clicks on a specific combobox
      | to |
    And the user selects a specific combobox item
      | last_month |
    And the user sets the 'Favourite searches' name
      | favourite_searches | user_search |
    When the user clicks on a specific button
      | save |
    Then the confirmation dialog contains the specific message
      | Do you want to save the search 'user_search |
    When the user clicks on a specific button
      | ok |
    When the user clicks on a specific button without wait
      | reset |
#    Then the specific checkbox is checked/unchecked
#      | unset | unchecked |
#    And the specific checkbox is checked/unchecked
#      | jao | unchecked |
    Then the user clicks on a specific combobox
      | favourite_searches |
    And the user selects a specific combobox item
      | user_search |
    And the specific field option is selected
      | ous_state | reviewing |
    And the specific field option is selected
      | processing_flags | pipeline_calibration |
    And the specific field option is selected
      | processing_flags | manual_calibration |
    And the specific field option is selected
      | processing_flags | pipeline_imaging |
    And the specific field option is selected
      | observing_modes | long_baseline |
    And the specific field option is selected
      | data_reduction_methods | pipeline_calibration_standard |

  @all @qa2 @smoke
  Scenario: Delete user-defined OUS search
    Then the user clicks on a specific combobox
      | favourite_searches |
    And the user selects a specific combobox item
      | user_search |
    When the user clicks on a specific button
      | delete |
    Then the confirmation dialog contains the specific message
      | Do you want to delete the search 'user_search |
    And the user clicks on a specific button
      | ok |
    When the user clicks on a specific combobox
      | favourite_searches |
    Then the specific list item is not available
      | user_search |

  @all @qa2 @smoke @2018mar
  Scenario: Save user-defined OUS search, update then save again
    Then the user inserts a value into a specific field
      | data_reducer | username |
    And the user inserts a value into a specific field
      | drm | username |
    And the user inserts a value into a specific field
      | sb_name_qa2 | test_ |
    And the user inserts a value into a specific field
      | ous_status_id | test_ |
    When the user clicks on a specific field
      | ous_state |
    Then the available options are displayed
      | ous_state |
    And the user selects a specific option
      | ous_state | reviewing |
    When the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_calibration |
    When the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | pipeline_imaging |
    When the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |
    And the user selects a specific option
      | processing_flags | manual_calibration |
    When the user clicks on a specific field
      | observing_modes |
    Then the available options are displayed
      | observing_modes |
    And the user selects a specific option
      | observing_modes | long_baseline |
    When the user clicks on a specific field
      | data_reduction_methods |
    Then the available options are displayed
      | data_reduction_methods |
    Then the user selects a specific option
      | data_reduction_methods | pipeline_calibration_standard |
    And the user clicks on a specific combobox
      | time_in_current_state |
    And the user selects a specific combobox item
      | 30 |
    And the user inserts a value into a specific field
      | project_code | 2017.1 |
    And the user sets the 'Favourite searches' name
      | favourite_searches | user_search |
    When the user clicks on a specific button
      | save |
    Then the confirmation dialog contains the specific message
      | Do you want to save the search 'user_search |
    When the user clicks on a specific button
      | ok |
    And the user checks/un-checks a specific checkbox
      | array_tm      | check |
      | array_7m      | check |
    When the user clicks on a specific button
      | save |
    Then the confirmation dialog contains the specific message
      | Do you want to save the search 'user_search |
    When the user clicks on a specific button
      | ok |
