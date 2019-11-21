@MultiBrowserTesting
Feature: Archive-Services Up-and-Running

#  The URLs can be found here: /src/test/resources/properties/navigationURLs.url
#  The credentials should be set here: /src/test/resources/properties/project.properties
#  The names of the elements can be fond here: /src/test/resources/properties/elements.properties


  @Sanity
  Scenario: AQ up and running
    Given Service available
      | aq  |

  @Sanity
  Scenario: RH up and running
    Given Service available
      | rh  |

  @Sanity @Regression
  Scenario: DP up and running
    Given Service available
      | dataportal  |

  @Sanity @Regression
  Scenario: SC up and running
    Given Service available
      | sc  |


  @Sanity @Regression
  Scenario: DT up and running
    Given Service available
      | datatracker  |