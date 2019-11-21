  @MultiBrowserTesting @dra @drm @2018jun @regression-phaa @regression-phab
  Feature: ICT-12822: DRA Tool: workload for DR and availability in AQUA and Protrack

    Background:
      Given go to DRA test environment
#      Given the DRA test environment is available
      And CAS login page is displayed
      When the user fills the credentials
        | username |
        | password |
      And the user clicks the LOGIN button DRA
      Then generic user is authenticated
      And the DRA view is displayed

    Scenario: Add Data Reducer - Scenario 1
      Given the DRA view is displayed
      When generic user is authenticated
      And go to view
        | add |
      And search account in Add/Remove
        | username |
      Then remove data reducer if it exists
        | username |
#      Then add default data reducer
#        |username |
      And add data reducer
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | EU  | Germany | yes                | no             | no            | yes          |
      And go to view
        | manage |
      And enter id in search box - Management
        | username |
      And click Search button - Management
      And verify data reducer details table - Management
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | EU  | Germany | yes                | no             | no            | yes          |

    Scenario: Add Data Reducer - Scenario 2
      Given the DRA view is displayed
      When generic user is authenticated
      And go to view
        | add |
      And search account in Add/Remove
        | username |
      Then remove data reducer if it exists
        | username |
#      Then add default data reducer
#        |username |
      And add data reducer
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | EU  | Germany | no                | no             | no            | no          |
      And go to view
        | manage |
      And enter id in search box - Management
        | username |
      And click Search button - Management
      And verify data reducer details table - Management
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | EU  | Germany | no                | no             | no            | no          |

    Scenario: Add Data Reducer - Scenario 3
      Given the DRA view is displayed
      When generic user is authenticated
      And go to view
        | add |
      And search account in Add/Remove
        | username |
      Then remove data reducer if it exists
        | username |
#      Then add default data reducer
#        |username |
      And add data reducer
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | EU  | Germany | yes                | yes             | yes            | yes          |
      And go to view
        | manage |
      And enter id in search box - Management
        | username |
      And click Search button - Management
      And verify data reducer details table - Management
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | EU  | Germany | yes                | yes             | yes            | yes          |

    Scenario: Add Data Reducer - Scenario 4 - JAO [ ICT-13147 ]
      Given the DRA view is displayed
      When generic user is authenticated
      And go to view
        | add |
      And search account in Add/Remove
        | username |
      Then remove data reducer if it exists
        | username |
#      Then add default data reducer
#        |username |
      And add data reducer
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | JAO | skip    | yes                | yes             | yes            | yes        |
      And go to view
        | manage |
      And enter id in search box - Management
        | username |
      And click Search button - Management
      And verify data reducer details table - Management
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | JAO | skip    | yes                | yes            | yes           | yes          |

    Scenario: Add Data Reducer - Scenario 5 - NA
      Given the DRA view is displayed
      When generic user is authenticated
      And go to view
        | add |
      And search account in Add/Remove
        | username |
      Then remove data reducer if it exists
        | username |
#      Then add default data reducer
#        |username |
      And add data reducer
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | NA  | Canada | yes                | yes             | yes            | yes          |
      And go to view
        | manage |
      And enter id in search box - Management
        | username |
      And click Search button - Management
      And verify data reducer details table - Management
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | NA  | Canada | yes                | yes             | yes            | yes          |

    Scenario: Add Data Reducer - Scenario 6 - EA
      Given the DRA view is displayed
      When generic user is authenticated
      And go to view
        | add |
      And search account in Add/Remove
        | username |
      Then remove data reducer if it exists
        | username |
#      Then add default data reducer
#        |username |
      And add data reducer
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | EA  | Japan | yes                | yes             | yes            | yes          |
      And go to view
        | manage |
      And enter id in search box - Management
        | username |
      And click Search button - Management
      And verify data reducer details table - Management
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | EA  | Japan | yes                | yes             | yes            | yes          |

    Scenario: Data reducer edit in Management view
      Given the DRA view is displayed
      When generic user is authenticated
      And go to view
        | add |
      And search account in Add/Remove
        | username |
      And remove data reducer if it exists
        | username |
      And add data reducer
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | EU  | Germany | yes                | yes             | yes            | yes          |
      And go to view
        | manage |
      And enter id in search box - Management
        | username |
      And click Search button - Management
      And verify data reducer details table - Management
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | EU  | Germany | yes                | yes             | yes            | yes          |
      And click on search result - Management
        | username |
      And modify data reducer
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | EU  | Sweden | yes                | no             | no            | no          |
      Then verify data reducer details table - Management
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | EU  | Sweden | no                | yes             | yes            | yes          |



    Scenario: Management view search by qualifications
      Given the DRA view is displayed
      When generic user is authenticated
      And go to view
        | add |
      And search account in Add/Remove
        | username |
      And remove data reducer if it exists
        | username |
      And add data reducer
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | EU  | Germany | yes                | yes             | yes            | yes          |
      And go to view
        | manage |
      And select data reducer ARC
        | EU |
      And select qualification in management view
        | Manual Calibration |
      And select qualification in management view
        | Manual Imaging |
      And select qualification in management view
        | WebLog Review |
      And select qualification in management view
        | QA2 Approval |
      And click Search button - Management
      Then validate user is found - Management
        | username |

    Scenario: Management view search by id
      Given the DRA view is displayed
      When generic user is authenticated
      And go to view
        | manage |
      And enter id in search box - Management
        | username |
      And click Search button - Management
      Then validate user is found - Management
        | username |

    Scenario: Verify DRM admin tabs
      Given the DRA view is displayed
      When generic user is authenticated
      Then validate tab exists
        | add |
      And validate tab exists
        |  manage |
      And validate tab exists
        |  booking |
      And validate tab exists
        |  availability |

    Scenario: Search for account in Add/Remove view
      Given the DRA view is displayed
      When generic user is authenticated
      And go to view
        | add |
      And search account in Add/Remove
        | username |
      Then validate user id in Add/Remove
        | username |

    Scenario: Verify DRM admin qualifications in Booking view
      Given the DRA view is displayed
      When generic user is authenticated
      And go to view
        | add |
      And search account in Add/Remove
        | username |
      And remove data reducer if it exists
        | username |
      And add data reducer
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | EU  | Germany | yes                | yes             | yes            | yes          |
      And go to view
        | booking |
      Then validate qualification status
        | Manual Calibration | Yes |
      And validate qualification status
        |  Manual Imaging | Yes |
      And validate qualification status
        |  WebLog Review | Yes |
      And validate qualification status
        |  QA2 Approval | Yes |

    Scenario: Add Availability Booking tomorrow
      Given the DRA view is displayed
      When generic user is authenticated
      And go to view
        | add |
      And search account in Add/Remove
        | username |
      And remove data reducer if it exists
        | username |
      And add data reducer
        | username | arc | node    | Manual Calibration | Manual Imaging | WebLog Review | QA2 Approval |
        | username | EU  | Germany | yes                | yes             | yes            | yes          |
      And go to view
        | booking |
      And open availability booking view
      And add availability booking
        | Available |
      And add start date availability booking
        | tomorrow |
      And add end date availability booking
        | tomorrow |
      And submit availability booking ok
      Then go to view
        | availability |
      And select data reducer ARC in availability view
        | EU |
      And select data reducer Node in availability view
        | Germany |
      And click availability search button
      And verify availability for
        | Alex Popa |
      And go to view
        | booking |
      And remove booking availability

