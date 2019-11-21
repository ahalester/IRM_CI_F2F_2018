@RestApiTest @xtss
Feature: XTSS tests

  Background:
    Given Init xtss rest

  @ICT13413
  Scenario: ICT13431 - Search for a project on a specific state and substate [ManualCalAndImg]
    When find ObsUnitSets with State and Substate
      | Reviewing | ManualCalAndImg |

  @ICT13123
  Scenario: ICT13123 - Search for a project on a specific state and substate [UserInitiated]
    When find ObsUnitSets with State and Substate
      | QA3InProgress | UserInitiated |

  @ICT13123
  Scenario: ICT13123 - Search for a project on a specific state and substate [ObservatoryInitiated]
    When find ObsUnitSets with State and Substate
      | QA3InProgress | ObservatoryInitiated |