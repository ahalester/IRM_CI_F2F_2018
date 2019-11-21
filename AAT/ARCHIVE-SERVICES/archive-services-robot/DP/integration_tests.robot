
*** Settings ***
Documentation     DataPacker Integration Tests
...
...               ------------------------
...               Scenario
...               ------------------------
...               - Download request have been done by a proprietary user
...               - Download scripts are available for different cycles
...               ----------------
...               Test cases
...               ----------------
...               - TC: Download And Validate Project Files For Cycle 4
...               - TC: Download And Validate Project Files For Cycle 5
...
...               - Steps: Pre-prepared download script for each TC will be executed from command line with authentication.
...               - Expected Results: Files should be downloaded. Tar should be able to unpack and content should be correct.
...               ----------------
...               Execution
...               ----------------
...               - cd DP
...               - robot --exclude skip   --noncritical non-critical --critical critical --outputdir output --variable phase:PHAA --variable full_data_project:2017.1.00019.S  .
...
...

Library         Process
Library         OperatingSystem
Library         String
#Library         RequestsLibrary
#Library         ./resources/py/UtilsLibrary.py



#Suite Setup     Run Keywords
#...             Clean Workspace    AND
#...             Create Session  acserver  http://ldoming:ldoming4CAS@phase-a1.hq.eso.org/rh-ARCHIVE-2018AUG


#Suite Setup     Clean Workspace
#Suite Teardown  Clean Workspace




*** Variables ***
${RESOURCES_DIR}                           $PWD/resources

${dump_tar_content_script}                 ${RESOURCES_DIR}/dump_tar_content.sh
${execute_with_password_script}            ${RESOURCES_DIR}/execute_script_with_password.sh
${read_property_script}                    ${RESOURCES_DIR}/read_property.sh
${extract_tar_name}                        ${RESOURCES_DIR}/extract_tar_name.sh
${chmod}                                   ${RESOURCES_DIR}/chmod.sh
${find_recursive}                          ${RESOURCES_DIR}/find_recursive.sh


${phase}                                   PHAA
${full_data_project}                       2017.1.00019.S

*** Test Cases ***



Download And Validate Project Files For Project ${full_data_project}
    [tags]  critical     all

    ${result}=    Download And Validate Files For Project/Environment       ${full_data_project}       ${phase}







*** Keywords ***

Clean Workspace
    # Remove Directory    output     recursive=yes -> Not applicable, output.xml is being used

    ${output} =	                    Run	rm -rf output/*.txt
    ${output} =	                    Run	rm -rf *readme.txt
    ${output} =	                    Run	rm -rf *.tar


Get Property
    [Arguments]                     ${property_key}=property.key

    ${result} =                     Run Process        ${read_property_script} /etc/offline/config/aatConfig.properties ${property_key}      shell=true        stdout=output/dp_stdout.txt        stderr=output/dp_stderr.txt
    Log Many                        stdout: ${result.stdout}        stderr: ${result.stderr}

    ${value_file} =                 Catenate           ${property_key}.txt
    ${property_value} =             Get File           ./output/${value_file}

    Log Many                        -> Property value: '${property_value.replace("\n", " ").replace(" ", "")}'

    [return]                        ${property_value.replace("\n", " ").replace(" ", "")}


Prepare Test For Cycle
    [Arguments]                     ${cycle}=cycle0     ${phase}=PHAB

    ${result}=                      set test urls list  ${cycle}    ${phase}
    Log To Console                  ${result}

Generate Download Scripts
    [Arguments]                     ${cycle}=cycle5     ${phase}=PHAA

    # POST http://phase-a1.hq.eso.org/rh-ARCHIVE-2018AUG/requests/ldoming/2145100637370/downloadFile


Download Project Products
    [Arguments]                     ${full_data_project}=2017.1.00019.S     ${phase}=PHAA

    log many                  -> Downloading data for project '${full_data_project}' files from ${phase} testing environment

    # (Not working because the resulting script cannot login) Replace download script with appropiate URL
    # ${result} =             Prepare Test For Cycle      ${cycle}    ${phase}
    # Workaround instead

    ${download_scripts_dir} =       Get Property       files.download.dir

    ${script_to_execute} =          Catenate     ${download_scripts_dir}/${full_data_project}_${phase}.sh

    # Grant execution permissions to the download script
    ${password} =                   Get Property       sudo.password
    ${result} =                     Run Process        ${chmod} ${password} ${script_to_execute}   shell=true        stdout=output/dp_stdout.txt        stderr=output/dp_stderr.txt
    Log Many                        stdout: ${result.stdout}        stderr: ${result.stderr}

    # Execute download script
    ${password} =                   Get Property       ldoming.user.password
    ${result} =                     Run Process        ${execute_with_password_script} ${script_to_execute} ${password}     shell=true        stdout=output/dp_stdout.txt        stderr=output/dp_stderr.txt
    Log Many                        stdout: ${result.stdout}        stderr: ${result.stderr}

    # Check script output and verify download succeed
    ${result} =	                    Grep File	output/dp_stdout.txt	succesfully
    Log Many                        ${result}
    Should Not Be Empty             ${result}

    # Save tar name into variable for returning it
    ${result} =                     Run Process        ${extract_tar_name}       shell=true
    Log Many                        stdout: ${result.stdout}        stderr: ${result.stderr}
    ${tar_name} =                   Get File      output/tar_name.txt
    ${tar_name} =                   Remove String      ${tar_name}        \n
    Log To Console                  -> Tar name: "${tar_name}"

    [return]                        ${tar_name}


Extract Tar
    [Arguments]                     ${tar_file}=2016.1.00004.S_uid___A001_X879_Xea_001_of_001.tar

    Log To Console                  -> Extracting ${tar_file} and creating a dump of the content

    # Extract tar
    ${output} =	                    Run	tar -xvf ${tar_file}
    Log To Console                  ${output}




Dump Tar Content
    [Arguments]                     ${tar_file}=2016.1.00004.S_uid___A001_X879_Xea_001_of_001.tar

    Log To Console                  -> Extracting ${tar_file} and creating a dump of the content

    # Extract tar
    ${output} =	                    Run	tar -xvf ${tar_file}
    Log Many                        ${output}
    ${extracted_directory} =        Get Extracted Dir Name      ${output}
    Log To Console                  -> Tar was extracted in ${extracted_directory}

    # Create dump file to be compared with the expected dump
    ${dump_name} =                  Catenate           ${tar_file}_dump.txt
    Log To Console                  -> Dump will be named: ${dump_name}
    ${result} =                     Run Process         ${dump_tar_content_script} ${dump_name} ${extracted_directory}      shell=true        stdout=output/dp_stdout.txt        stderr=output/dp_stderr.txt
    Log To Console                  ${result}


Validate Tar Content
    [Arguments]                     ${tar_file}=2016.1.00004.S_uid___A001_X879_Xea_001_of_001.tar

    Log To Console                  -> Validating content of extracted ${tar_file}

    # Get dump file
    ${dump_name} =                  Catenate           ${tar_file}_dump.txt
    Log To Console                  -> Dump used: ${dump_name}

    # Verify extraction was successful by searching for expected file extensions in the output
    ${product_match} =	            Grep File	output/${dump_name} 	product

    Should Not Be Empty             ${product_match}

    # Validate tar content by comparing current unpacked directory dump with expected one
    # ${expected_content} =           Get File      ./resources/dumps/${dump_name}
    # ${actual_content} =             Get File      ./output/${dump_name}
    ## TODO: Fix the extracted directory dump issue
    # Should Be Equal As Strings      ${expected_content}    ${actual_content}


Check Extracted Tar Contains
    [Arguments]                     ${to_be_found}=product

    Log To Console                  -> Validating content of extracted tar

    ${result} =	            Run Process         ${find_recursive} $PWD ${to_be_found}      shell=true        stdout=output/dp_stdout.txt
    Log Many                        stdout: ${result.stdout}        stderr: ${result.stderr}

    Should Not Be Empty             ${result.stdout}


Download And Validate Files For Project/Environment
    [Arguments]                     ${full_data_project}=2017.1.00019.S     ${phase}=PHAA

    Log Many                        -> Downloading and validation files for project: ${full_data_project} in ${phase} environment
    # Download project files and get tar file name
    ${tar_file_name} =              Download Project Products             ${full_data_project}    ${phase}


    # Validate tar contents by comparing with expected dump
    ${result} =                     Extract tar                           ${tar_file_name}

    ${result} =                     Check Extracted Tar Contains               science_goal
    ${result} =                     Check Extracted Tar Contains               group
    ${result} =                     Check Extracted Tar Contains               member
    ${result} =                     Check Extracted Tar Contains               product

