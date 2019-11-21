
*** Settings ***
Documentation     Product Ingestor Integration Tests
...
...               ------------------------
...               Scenario characteristics
...               ------------------------
...               - Ingest new project
...
...               ----------------
...               Expected results
...               ----------------
...               - Generated dump and master dump are equal
...
...               ----------------
...               Execution
...               ----------------
...               - cd PI
...               - robot --exclude skip --noncritical non-critical --critical critical --outputdir reports .
...
...



# External RF libraries used in this tests

Library         Process
Library         OperatingSystem


#Suite Setup     Clean Worspace
#Suite Teardown  Clean test data




*** Variables ***
${RESOURCES_DIR}                   $PWD/resources
${INTREGRATION_TEST_DIR}           ${RESOURCES_DIR}/integration_testing
${run_test_cmd}                    ${RESOURCES_DIR}/run_test.sh ${INTREGRATION_TEST_DIR} ${VERSION} ${UID}



*** Test Cases ***

Ingest new project
    [tags]          critical    script
    Log To Console  Ingesting project ${UID} using ingestor version: ${VERSION}
    ${result} =     Run Process        ${run_test_cmd}       shell=true        stdout=reports/pi_stdout.txt        stderr=reports/pi_stderr.txt
    Log Many        stdout: ${result.stdout}        stderr: ${result.stderr}
    ${failures} =	Grep File	reports/pi_stdout.txt	FAIL
    Should Be Empty  ${failures}



*** Keywords ***
Clean Workspace

