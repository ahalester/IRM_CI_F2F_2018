@MultiBrowserTesting @qa2
Feature: QA2: enable pre-imaging QA1.5 assessment - https://ictjira.alma.cl/browse/ICT-9455

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/AQUA.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties

  Background:
    Given QA2 test environment is available
    And AQUA login page is displayed
    When the user fills the credentials
      | username |
      | password |
    And the user clicks the LOGIN button
    Then the user is authenticated
      | username |
    When the specific search tab is selected
      | advanced_ous_search |
    And the user clicks on a specific field
      | processing_flags |
    Then the available options are displayed
      | processing_flags |

  @all @regression
  Scenario: ICT-9455 'Predicted sensitivity' and 'Predicted synthesised beam' fields available for 'PipelineCalAndImg'
    When the user selects a specific option
      | processing_flags | pipeline_cal_and_img |
    And the user inserts a value into a specific field
      | sb_name_qa2 | PG2304+0_a_06_7M |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | pipeline_cal_and_img |
    Then the specific label and data are displayed
      | predicted_sensitivity      |
      | predicted_synthesised_beam |

  @all @regression
  Scenario: ICT-9455 'Predicted sensitivity' and 'Predicted synthesised beam' fields available for 'PipelineCalibration'
    When the user selects a specific option
      | processing_flags | pipeline_calibration |
    And the user inserts a value into a specific field
      | sb_name_qa2 | NGC2903_b_06_7M |
    Then the user checks if the OUS are displayed
    When the user selects the first available OUS
    Then the OUS Summary details page is displayed
    When the user clicks on a specific tab
      | pipeline_calibration |
    Then the specific label and data are displayed
      | predicted_sensitivity      |
      | predicted_synthesised_beam |
    When the user clicks on a specific tab
      | pipeline_cal_and_img |
    Then the specific label and data are displayed
      | predicted_sensitivity      |
      | predicted_synthesised_beam |

