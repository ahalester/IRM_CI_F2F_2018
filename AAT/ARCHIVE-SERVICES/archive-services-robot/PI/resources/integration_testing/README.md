Product Ingestor Integration Test Suite
=======================================

The product ingestor integration test suite contains the following directories:

1. config - contains the archiveConfiguration.properties file
2. csv - contains the master copies of the database dump files in CSV format
3. jar - install directory for the JAR executables
4. product - install directory for the test pipeline product tarballs
5. script - install directory for the bash and python scripts
6. sql - contains SQL scripts

Two kinds of tests can be executed...

1. Batch integration tests (multiple OUS)
2. Singular integration test (single OUS)

### How to configure the integration test environment

Copy the template archive configuration properties file

    $ cp config/NO-LGPL/archiveConfig.properties.localhost config/archiveConfig.properties

Edit the properties file and customise the settings according to your test environment

### How to start a batch integration test

A new batch integration test can be started from the command line...

    $ batch_test_product_ingestor.sh SOFTWARE_RELEASE

Where the command line arguments are...

    SOFTWARE_RELEASE    Software version used for testing
                        (e.g. 2018.07-SNAPSHOT)

A new directory tree will be created at the beginning of the batch test
where all the files that are used and created during the test can be found.
The top level directory name contains a timestamp for when the test was
initiated. As the batch tests progress more and more test run subdirectories
will be created containing new pipeline product files and test files from
other OUS projects.

    batch_run_YYYYMMDD_HHMMSS/test_run_YYYYMMDD_HHMMSS/csv/test_sql_builder_alma_1_add.log
    batch_run_YYYYMMDD_HHMMSS/test_run_YYYYMMDD_HHMMSS/csv/test_sql_builder_ngas_1_add.log
    batch_run_YYYYMMDD_HHMMSS/test_run_YYYYMMDD_HHMMSS/csv/test_sql_builder_alma_2_replace.log
    batch_run_YYYYMMDD_HHMMSS/test_run_YYYYMMDD_HHMMSS/csv/test_sql_builder_ngas_2_replace.log
    batch_run_YYYYMMDD_HHMMSS/test_run_YYYYMMDD_HHMMSS/log/test_run_YYYYMMDD_HHMMSS.log
    batch_run_YYYYMMDD_HHMMSS/test_run_YYYYMMDD_HHMMSS/test_uid___XXXX_XXXX_XXXX

### How to start an integration test

A new integration test can be started from the command line...

    $ test_product_ingestor.sh SOFTWARE_RELEASE OUS_UID

Where the command line arguments are...

    SOFTWARE_RELEASE    Software version used for testing
                        (e.g. 2018.07-SNAPSHOT)
    PRODUCT_TARBALL     Pipeline product tarball
                        (e.g. test_product_07_uid___A001_X12d0_Xfa.tar)

A new directory tree will be created at the beginning of the integration test
where all the files that are used and created during the test can be found.
The top level directory name contains a timestamp for when the test was
initiated.

    test_run_YYYYMMDD_HHMMSS/csv/test_sql_builder_alma_1_add.log
    test_run_YYYYMMDD_HHMMSS/csv/test_sql_builder_ngas_1_add.log
    test_run_YYYYMMDD_HHMMSS/csv/test_sql_builder_alma_2_replace.log
    test_run_YYYYMMDD_HHMMSS/csv/test_sql_builder_ngas_2_replace.log
    test_run_YYYYMMDD_HHMMSS/log/test_run_YYYYMMDD_HHMMSS.log
    test_run_YYYYMMDD_HHMMSS/test_uid___XXXX_XXXX_XXXX

