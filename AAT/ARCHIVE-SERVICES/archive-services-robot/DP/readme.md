Data Portal Integration Test Suite
=======================================

The DP integration test suite contains tests aimed to verify the download of Product Files from the Data Portal 
using the download script.

It also verifies the content of the download data by comparing a dump of the extracted download data with expected templates.

Table of contents
=================

<!--ts-->
   * [Project structure](#Project structure)
   * [Setup the test environment](#Setup the test environment)
   * [Execute the test suite](#Execute the test suite)
   * [Test Results](#Test Results)   
<!--te-->

### Project structure

1. resources/ 
    - contains the scripts executing different tasks and template dumps
2. integration_tests.robot 
    - The Robot Framework test suite
3. readme.md 
    - This documentation



### Setup the test environment

* Install Python (used Python 2.7.10)
    
  [About python versions ](https://wiki.python.org/moin/Python2orPython3)    
            
* Install Robot framework ([Docs](http://robotframework.org/robotframework/latest/RobotFrameworkUserGuide.html))
    
* Install Robot framework required libraries

        pip install robotframework-requests

* Install python libraries

        pip install requests
    
* Tests environment configuration
        
      /etc/offline/config/aat.properties
      
* Grant x-permission to provided and generated scripts 

    chmod 777  <this_dir>
    chmod 777  /etc/offline/aat
      
* Grant execution permission to all contained shell scripts

      chmod -R 700 path-to/DP/


### Execute the test suite

    robot -v phase:PHAA --exclude skip   --noncritical non-critical   --critical critical   --outputdir output  .

will executed the tests found in curren directory (DP) with the following arguments:

    --exclude skip: Will exclude tests tagged with "skip"
    --noncritical non-critical: Will consider as non critical tests tagged with "non-critical"
    --critical critical: Will consider as critical tests tagged with "critical"
    --outputdir output: Will create and write reports to output/


### Test Results

TODO: Display sample report (image)

