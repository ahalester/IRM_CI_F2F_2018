#!/bin/bash

BASE_PATH=${PWD}

TIMESTAMP=$(date +'%Y%m%d_%H%M%S')

LOG_FILE=${BASE_PATH}/ngas_cleaner_${TIMESTAMP}.log

# Properties file settings
DEFAULT_ARCHIVE_PROPERTIES_PATH="../config/archiveConfig.properties"
ACSDATA_ARCHIVE_PROPERTIES_PATH="${ACS_DATA}/config/archiveConfig.properties"

NGAS_SERVERS_PROPERTY_NAME="^archive.test_product_ingestor.ngas.servers"
NGAS_DB_CONNECTION_PROPERTY_NAME="^archive.test_product_ingestor.ngas.db.connection"
NGAS_DB_USER_PROPERTY_NAME="^archive.test_product_ingestor.ngas.db.user"
NGAS_DB_PASSWORD_PROPERTY_NAME="^archive.test_product_ingestor.ngas.db.password"

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
    local message=" CLEAN DEBUG - ${1}"
    local timestamp=$(date +'%F %T')
    echo -e "${timestamp}${message}"
    echo -e "${timestamp}${message}" >> ${log_file}
}

function log_info {
    local message=" CLEAN INFO  - ${1}"
    local timestamp=$(date +'%F %T')
    echo -e "${timestamp}${message}"
    echo -e "${timestamp}${message}" >> ${log_file}
}

function log_warn {
    local message=" CLEAN WARN  - ${1}"
    local timestamp=$(date +'%F %T')
    echo -e "${ORANGE}${timestamp}${message}${RESET}"
    echo -e "${timestamp}${message}" >> ${log_file}
}

function log_error {
    local message=" CLEAN ERROR - ${1}"
    local timestamp=$(date +'%F %T')
    echo -e "${RED}${timestamp}${message}${RESET}"
    echo -e "${timestamp}${message}" >> ${log_file}
}

function signal_handler {
    local signal=${1}
    log_error "Interrupted by system signal ${signal}. Exiting the test run."
    exit 1
}

function get_property {
    local config_file=${1}
    local property_name=${2}
    local property_value=$(grep -e ${property_name} ${config_file} | sed 's/.*=\(.*\)$/\1/')
    echo ${property_value}
}

function print_usage {
    echo "Usage: ngas_cleaner.sh"
    echo ""
    echo "Cleans all archived files from the NGAS database and storage volumes"
    echo ""
    echo "Requires archiveConfig.properties for configuration settings"
    echo ""
    echo "Options:"
    echo "    -c CONFIG_FILE    Archive configuration file"
    echo "    -h                Display this help"
    echo "    -l LOG_FILE       Log file"
    echo "    -p NUMBER         Number of parallel comands (default ${parallel_number})"
    echo "    -t                Test status of NGAS file"
    echo "    -v                Verbose log messages"
}

function ngas_checkfile_request {
    disk_id=${1}
    file_id=${2}
    file_version=${3}

    response=$(curl --silent "${ngas_server_url}/CHECKFILE?disk_id=${disk_id}&file_id=${file_id}&file_version=${file_version}&execute=1")

    if echo "${response}" | grep -q "NGAMS_INFO_FILE_OK"; then
        echo 0
    else
        echo 1
    fi
}

function ngas_discard_request {
    disk_id=${1}
    file_id=${2}
    file_version=${3}

    response=$(curl --silent "${ngas_server_url}/DISCARD?disk_id=${disk_id}&file_id=${file_id}&file_version=${file_version}&execute=1")

    if echo "${response}" | grep -q "NGAMS_INFO_DISCARD_OK"; then
        echo 0
    else
        echo 1
    fi
}

# Main body
# =========

# Set a signal handler to handle interruptions
trap "signal_handler" SIGINT SIGTERM

# The exit_status is set to 1 when a test fails
exit_status=0

# Command line options
config_file=""
log_file="${LOG_FILE}"
test_status=0
parallel_number=8
verbose=0

while getopts "c:hl:p:tv" opt; do
    case ${opt} in
        c)
            config_file="${OPTARG}"
            ;;
        h)
            print_usage
            exit 0
            ;;
        l)
            log_file="${OPTARG}"
            ;;
        p)
            parallel_number="${OPTARG}"
            ;;
        t)
            test_status=1
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

if [ -n "${config_file}" ]; then
    log_debug "Command line option config_file is ${config_file}"
else 
    log_debug "Command line option config_file is empty"
fi
if [ -n "${log_file}" ]; then
    log_debug "Command line option log_file is ${log_file}"
else
    log_debug "Command line option log_file is empty"
fi
log_debug "Command line option parallel_number is ${parallel_number}"
log_debug "Command line option test_status is ${test_status}"
log_debug "Command line option verbose is ${verbose}"

# Find the archive config properties file
archive_config_file=${DEFAULT_ARCHIVE_PROPERTIES_PATH}
if [ -n "${config_file}" ]; then
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
ngas_servers=$(get_property ${archive_config_file} ${NGAS_SERVERS_PROPERTY_NAME})
ngas_db_connection=$(get_property ${archive_config_file} ${NGAS_DB_CONNECTION_PROPERTY_NAME})
ngas_db_user=$(get_property ${archive_config_file} ${NGAS_DB_USER_PROPERTY_NAME})
ngas_db_password=$(get_property ${archive_config_file} ${NGAS_DB_PASSWORD_PROPERTY_NAME})

log_debug "NGAS servers property value is ${ngas_servers}"
log_debug "NGAS DB connection property value is ${ngas_db_connection}"
log_debug "NGAS DB user property value is ${ngas_db_user}"
#log_debug "NGAS DB password property value is ${ngas_db_password}"

ngas_server="${ngas_servers}"
# Check for multiple servers (comma separated value)
if echo "${ngas_servers}" | grep -q ","; then
    # Convert comma separated string into an array
    ngas_servers_array=$(echo ${ngas_servers} | sed 's/,/ /g')
    ngas_server="${ngas_server_array[0]}"
fi

ngas_server_url="http://${ngas_server}"
ngas_db_connect="${ngas_db_user}/${ngas_db_password}@${ngas_db_connection}"

log_info "Using NGAS server URL: ${ngas_server_url}"
#log_info "Using NGAS database connection: ${ngas_db_connect}"
log_info "Using NGAS database connection: ${ngas_db_user}/######@${ngas_db_connection}"

log_info "Querying all files stored on NGAS server ${ngas_server_url}"
results=$(sqlplus -s ${ngas_db_connect} << EOF
set pages 0
set linesize 200
set feedback off
set heading off;
select disk_id, file_id, file_version from ngas_files;
EOF
)

#echo ${results}

# Convert the results string into an array
results_array=(${results})
log_debug "NGAS SQL results has ${#results_array[@]} elements"

num_files=$(expr ${#results_array[@]} / 3)
log_info "NGAS has ${num_files} files"

count=0
for (( index=0; index<${#results_array[@]}; index+=3 )); do

    (( count++ ))

    # Speed up NGAS discards by parallelizing the requests
    test "$(jobs | wc -l)" -ge ${parallel_number} && wait -n || true
    {
        disk_id_index=${index}
        file_id_index=$(expr ${index} + 1)
        file_version_index=$(expr ${index} + 2)

        disk_id=${results_array[${disk_id_index}]}
        file_id=${results_array[${file_id_index}]}
        file_version=${results_array[${file_version_index}]}

        file_info="Disk ID: ${disk_id}, File ID: ${file_id}, File Version: ${file_version}"

        if [ ${test_status} -eq 1 ]; then
            log_debug "Checking file ${count} - ${file_info}"
            checkfile_status=$(ngas_checkfile_request ${disk_id} ${file_id} ${file_version})

            if [ ${checkfile_status} -eq 0 ]; then
                log_debug "Successfully confirmed status of file ${count} - ${file_info}";
            else
                log_error "Failed to confirm status of file ${count} - ${file_info}";
                exit_status=1
                continue
            fi
        fi

        log_info "Discarding file ${count} - ${file_info}"
        discard_status=$(ngas_discard_request ${disk_id} ${file_id} ${file_version})

        if [ ${discard_status} -eq 0 ]; then
            log_info "Successfully discarded file ${count} - ${file_info}";
        else
            log_info "failed to discard file ${count} - ${file_info}";
            exit_status=1
        fi
    } &

done

wait

exit ${exit_status}

