#!/bin/bash

# TODO: add command line options for choosing tarball sizes

BASE_PATH=${PWD}

TIMESTAMP=$(date +'%Y%m%d_%H%M%S')

PRODUCT_PATH=${BASE_PATH}/product
CONFIG_FILE=${BASE_PATH}/NO-LGPL/config.sh

LOG_FILE=${BASE_PATH}/batch_test_${TIMESTAMP}.log

# Properties file settings
DEFAULT_ARCHIVE_PROPERTIES_PATH="./config/archiveConfig.properties"
ACSDATA_ARCHIVE_PROPERTIES_PATH="${ACS_DATA}/config/archiveConfig.properties"

FTP_URL_PROPERTY_NAME="archive.test_product_ingestor.ftp.url"
FTP_USER_PROPERTY_NAME="archive.test_product_ingestor.ftp.username"
FTP_PASSWORD_PROPERTY_NAME="archive.test_product_ingestor.ftp.password"

TEST_PRODUCT_LIST=(
test_product_07_uid___A001_X12d0_Xfa.tar
test_product_10_uid___A001_X1284_X1304.tar
test_product_17_uid___A001_X1284_Xb68.tar
test_product_20_uid___A001_X1284_X20e0.tar
test_product_22_uid___A001_X1284_X12fc.tar
test_product_23_uid___A001_X1296_Xa87.tar
test_product_24_uid___A001_X1284_X2843.tar
test_product_29_uid___A001_X12cc_X125.tar
test_product_30_uid___A001_X1284_X2749.tar
test_product_32_uid___A001_X898_Xda.tar )

# Foreground colours
BLACK="$(tput setaf 0)"
RED="$(tput setaf 1)"
GREEN="$(tput setaf 2)"
ORANGE="$(tput setaf 3)"
BLUE="$(tput setaf 4)"
MAGENTA="$(tput setaf 5)"
CYAN="$(tput setaf 6)"
WHITE="$(tput setaf 7)"
RESET="$(tput sgr0)"

function log_debug {
    local message=" BATCH DEBUG - ${1}"
    local timestamp=$(date +'%F %T')
    echo -e "${timestamp}${message}"
    echo -e "${timestamp}${message}" >> ${LOG_FILE}
}

function log_info {
    local message=" BATCH INFO  - ${1}"
    local timestamp=$(date +'%F %T')
    echo -e "${timestamp}${message}"
    echo -e "${timestamp}${message}" >> ${LOG_FILE}
}

function log_warn {
    local message=" BATCH WARN  - ${1}"
    local timestamp=$(date +'%F %T')
    echo -e "${ORANGE}${timestamp}${message}${RESET}"
    echo -e "${timestamp}${message}" >> ${LOG_FILE}
}

function log_error {
    local message=" BATCH ERROR - ${1}"
    local timestamp=$(date +'%F %T')
    echo -e "${RED}${timestamp}${message}${RESET}"
    echo -e "${timestamp}${message}" >> ${LOG_FILE}
}

function log_pass {
    local message=" BATCH PASS  - ${1}"
    local timestamp=$(date +'%F %T')
    echo -e "${GREEN}${timestamp}${message}${RESET}"
    echo -e "${timestamp}${message}" >> ${LOG_FILE}
}

function log_fail {
    local message=" BATCH FAIL  - ${1}"
    local timestamp=$(date +'%F %T')
    echo -e "${RED}${timestamp}${message}${RESET}"
    echo -e "${timestamp}${message}" >> ${LOG_FILE}
}

function signal_handler {
    local signal=${1}
    log_error "Interrupted by system signal ${signal}. Exiting the batch test."
    if [ -d ${batch_log_path} ]; then
        mv ${LOG_FILE} ${batch_log_file}
    fi
    exit 1
}

function get_property {
    local config_file=${1}
    local property_name=${2}
    local property_value=$(grep -e ${property_name} ${config_file} | sed 's/.*=\(.*\)$/\1/')
    echo ${property_value}
}

function print_usage {
    echo "Usage: batch_test_product_ingestor.sh [options] SOFTWARE_RELEASE"
    echo ""
    echo "Executes a batch run for the product ingestor integration test"
    echo ""
    echo "Requires archiveConfig.properties for configuration settings"
    echo ""
    echo "Options:"
    echo ""
    echo "    -c CONFIG_FILE    Archive configuration file"
    echo "    -d                Only download test product tarballs and skip integration tests"
    echo "    -h                Display this help"
    echo "    -m                Generate master CSV dump files"
    echo "    -n NUMBER         Number of product tarballs to be downloaded/ingested"
    echo "    -r RUN_PATH       Batch run directory path"
    echo "    -v                Verbose log messages"
}

# Main script body
# ================

# Set a signal handler to handle interruptions
trap "signal_handler" SIGINT SIGTERM

# Batch test run paths
batch_run_path=${BASE_PATH}/batch_test_${TIMESTAMP}
batch_log_path=${batch_run_path}/log
batch_log_file=${batch_log_path}/batch_test.log

# The exit_status is set to 1 when a test fails
exit_status=0
test_product_ingestor_options=""

# Command line options
config_file=""
download_only=0
master=0
num_tarballs=0
run_path=""
verbose=0

while getopts "c:dhmn:r:v" opt; do
    case ${opt} in
        c)
            config_file="${OPTARG}"
            if [ -n "${test_product_ingestor_options}" ]; then
                test_product_ingestor_options="${test_product_ingestor_options} -c ${config_file}"
            else
                test_product_ingestor_options="-c ${config_file}"
            fi
            ;;
        d)
            download_only=1
            ;;
        h)
            print_usage
            exit 0
            ;;
        m)
            master=1
            if [ -n "${test_product_ingestor_options}" ]; then
                test_product_ingestor_options="${test_product_ingestor_options} -m"
            else
                test_product_ingestor_options="-m"
            fi
            ;;
        n)
            num_tarballs="${OPTARG}"
            ;;
        r)
            run_path="${OPTARG}"
            ;;
        v)
            verbose=1
            ;;
        \?)
            exit 1
            ;;
        :)
            exit 1
            ;;
    esac
done
shift $((${OPTIND} - 1))

if [ ${download_only} -eq 0 ] && [ $# -ne 1 ]; then
    echo "Missing SOFTWARE_RELEASE argument!"
    print_usage
    exit 1
fi

software_release="${1}"

if [ -n "${config_file}" ]; then
    log_debug "Command line option config_file is ${config_file}"
else
    log_debug "Command line option config_file is empty"
fi
log_debug "Command line option download_only is ${download_only}"
log_debug "Command line option master is ${master}"
log_debug "Command line option num_tarballs is ${num_tarballs}"
if [ -n "${run_path}" ]; then
    log_debug "Command line option run_path is ${run_path}"
else
    log_debug "Command line option run_path is empty"
fi
log_debug "Command line option verbose is ${verbose}"
log_debug "Command line argument software_release is ${software_release}"

log_info "Testing product_ingestor version ${software_release}"

# Find the archive config properties file
archive_config_file=${DEFAULT_ARCHIVE_PROPERTIES_PATH}
if [ -n "${config_path}" ]; then
    archive_config_file=${config_file}
else
    # ARCHIVE_CONFIG is sometimes set as a shell environment variable
    if [ -n "${ARCHIVE_CONFIG}" ]; then
        archive_config_file=${ARCHIVE_CONFIG}
    else
        log_info "ARCHIVE_CONFIG environment variable is not set"
        if [ -n "${ACS_DATA}" ]; then
            archive_config_file=${ACSDATA_ARCHIVE_PROPERTIES_PATH}
        else
            log_info "ACS_DATA environment variable is not set"
        fi
    fi
fi

log_info "Checking archive configuration properties file ${archive_config_file}"
if [ ! -f ${archive_config_file} ]; then
    log_error "Archive configuration properties file is not a regular file ${archive_config_file}"
    exit 1
fi

log_info "Reading archive configuration properties"
ftp_url=$(get_property ${archive_config_file} ${FTP_URL_PROPERTY_NAME})
ftp_user=$(get_property ${archive_config_file} ${FTP_USER_PROPERTY_NAME})
ftp_password=$(get_property ${archive_config_file} ${FTP_PASSWORD_PROPERTY_NAME})

log_debug "FTP URL property value is ${ftp_url}"
log_debug "FTP user property value is ${ftp_user}"
#log_debug "FTP password property value is ${ftp_password}"

if [ -n "${run_path}" ]; then
    batch_run_path=${run_path}/batch_test_${TIMESTAMP}
    batch_log_path=${batch_run_path}/log
    batch_log_file=${batch_log_path}/batch_test.log
fi

if [ ${download_only} -eq 0 ]; then
    log_info "Creating batch run directory ${batch_run_path}"
    mkdir ${batch_run_path}
    if [ ! -d ${batch_run_path} ]; then
        log_error "Failed to create batch run directory ${batch_run_path}"
        exit 1
    fi

    log_info "Creating batch run log directory ${batch_log_path}"
    mkdir ${batch_log_path}
    if [ ! -d ${batch__log_path} ]; then
        log_error "Failed to create batch run log directory ${batch_log_path}"
        exit 1
    fi
fi

if [ -n "${test_product_ingestor_options}" ]; then
    test_product_ingestor_options="${test_product_ingestor_options} -r ${batch_run_path}"
else
    test_product_ingestor_options="-r ${batch_run_path}"
fi

count=0
for test_product in "${TEST_PRODUCT_LIST[@]}"; do

    (( count++ ))

    if [ ${num_tarballs} -ne 0 ] && [ ${count} -gt ${num_tarballs} ]; then
        if [ ${download_only} -eq 1 ]; then
            log_info "Downloaded only ${num_tarballs} product tarballs as requested"
        else
            log_info "Ingested only ${num_tarballs} product tarballs as requested"
        fi
        break
    fi

    test_product_path=${PRODUCT_PATH}/${test_product}

    # Check if the test product tarball has been downloaded already
    if [ -f ${PRODUCT_PATH}/${test_product} ]; then
        log_info "Found test product ${test_product} at ${test_product_path}. Skipping download!" 
    else
        test_product_url="${ftp_url}/${test_product}"
        log_info "Downloading test product ${test_product}"
        curl --progress-bar --insecure --ftp-ssl --user ${ftp_user}:${ftp_password} ${test_product_url} --output ${test_product_path}
#        wget --quiet --no-check-certificate --ftp-user=${ftp_user} --ftp-password=${ftp_password} "${test_product_url}"
    fi

    if [ ${download_only} -eq 1 ]; then
        continue
    fi

    # Extract the OUS UID from the test product tarball filename
    sanitized_ous_uid=$(echo "${test_product}" | sed "s/^test_product_[0-9]\{2\}_\(uid___.*\).tar$/\1/")
    ous_uid=$(echo "${sanitized_ous_uid}" | sed 's~___~://~g' | sed 's~_~/~g')

    log_info "Test run ${count} ingesting test product ${test_product} with OUS UID ${ous_uid}"

    ${BASE_PATH}/test_product_ingestor.sh ${test_product_ingestor_options} -i ${ous_uid} ${software_release} ${test_product}
    if [ $? -eq 0 ]; then
        log_info "Test run ${count} ingestion for test product ${test_product} with OUS UID ${ous_uid} was successful"
    else
        log_error "Test run ${count} ingestion for test product ${test_product} with OUS UID ${ous_uid} failed"
        exit_status=1
    fi

done

if [ ${download_only} -eq 0 ]; then
    # Always the last step
    mv ${LOG_FILE} ${batch_log_file}
fi

exit ${exit_status}

