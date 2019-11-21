@RestApiTest @xtss @regression-phab @2018may
Feature: XTSS tests

  Background:
    Given Init xtss rest


  Scenario: Search for a project on a specific state and substate
    When find ObsUnitSets with State and Substate
      |   Processing  |  ManualImaging  |

  Scenario: Search for a project on a specific state
    When find ObsUnitSet with State
      |   Waiting  |

  Scenario: Transition OUS state
    Given find ObsUnitSet with State
      |   ReadyForProcessing  |
    When transition OUS state
      | ous_session_data |  FullyObserved  |
