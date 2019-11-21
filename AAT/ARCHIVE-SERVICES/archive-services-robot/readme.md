Product Ingestor Integration Test Suite
=======================================

The PI integration test suite contains tests aimed to verify the the ingestor artifact.

Table of contents
=================

<!--ts-->
   * [Requirements to execute the test](#Requirements to execute the test)
   * [Installing sqlclient (Linux)](#Installing sqlclient (Linux))
   * [Installing pip and robotframework (Linux)](#Installing pip and robotframework (Linux))
   * [PI Configuration](#PI Configuration)   
   * [Adding components to be tested](#Adding components to be tested)  
   
   
<!--te-->



### Requirements to execute the test

    1. SQLPlus
    2. Python
    3. Robot framework


### Installing sqlclient (Linux)

    rpm -ivh oracle-instantclient11.2-basic-11.2.0.4.0-1.x86_64.rpm oracle-instantclient11.2-sqlplus-11.2.0.4.0-1.x86_64.rpm
    rpm -ivh oracle-instantclient12.2-sqlplus-12.2.0.1.0-1.x86_64.rpm
    PATH=$PATH:/usr/lib64/qt-3.3/bin:/usr/lib/oracle/11.2/client64/bin

### Installing pip and robotframework (Linux)

    curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py
    python get-pip.py
    python -m pip install robotframework
    robot


### PI Configuration

    Replace DB connection and other services properties in config file:
    ./resources/integration_testing/config/archiveConfig.properties


### Adding components to be tested

- resources/integration_testing/jar/product-ingestor-2018.06-SNAPSHOT-all.jar (from build output ARCHIVE-SERVICES/PipelineIngestor/target/)
- resources/integration_testing/jar/test-sql-builder-2018.06-SNAPSHOT-all.jar (from build output ARCHIVE-SERVICES/Support/test-utils/testsqlbuilder/target)
- resources/integration_testing/script/product_analyzer.py (from build output ARCHIVE-SERVICES/PipelineIngestor/scripts/)
- resources/integration_testing/script/product_ingestor.sh
