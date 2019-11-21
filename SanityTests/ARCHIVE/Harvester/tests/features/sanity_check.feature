@sanity @testing @2016.5
Feature: Check that the environment contains all dependencies needed by Harvester to run properly 
  Scenario: Java is installed and is the correct version
    Given I have installed "java"
    When I run the following command in a terminal
      """
      java -version
      """
    Then version should be "1.8.0_45"
    
  Scenario: The environment contains enough HDD space and RAM to run harvester
    Given logs are stored in "/var/log"
    When I look for the free space on the mountpoint of "/var/log"
      And I look for total RAM size
    Then total RAM size should be greater than "2G"
      And mountpoint of "/var/log" should has at least "1G" free space
      
  Scenario: Bibliography API is set in archiveConfig.properties and it should be reachable
    Given I use "http://telbib.eso.org" as bibliography API
      And "ACSDATA" environment variable is set
      And I use properties file "archiveConfig.properties"
      And the configuration file "archiveConfig.properties" has the property "source.biblio.url" set with value "http://telbib.eso.org"
    When I fetch bibliography for bibcode "2015ApJ...812..134"
    Then I should get a response with status code "200"
      And I should get the bibliography in xml format
      
  Scenario: Database defined in archiveConfig.properties is reachable at network layer
    Given "ACSDATA" environment variable is set 
      And I use properties file "archiveConfig.properties"
      And I use "archive.relational" database defined in "archiveConfig.properties"
    When I ping to defined database server
    Then I should get ping response

  Scenario: ASA related tables are created properly in Database
    Given "ACSDATA" environment variable is set 
      And I use tnsnames.ora
      And I use properties file "archiveConfig.properties"
      And I use "archive.relational" database defined in "archiveConfig.properties"
    When I connect to database server
    Then ASA tables should exist 

  Scenario: Harvesting a single ASDM
    Given "ACSDATA" environment variable is set 
      And I use properties file "archiveConfig.properties"
      And I use the ASDM with uid "uid://A002/X30a93d/X43e"
    When I run harvester in dry-run mode for given ASDM
    Then I should not get errors in stdout
