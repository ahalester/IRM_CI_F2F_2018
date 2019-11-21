#!/bin/bash

BASE_PATH=${PWD}

TIMESTAMP=$(date +'%Y%m%d_%H%M%S')

CONFIG_PATH=${BASE_PATH}/config
CSV_PATH=${BASE_PATH}/csv
JAR_PATH=${BASE_PATH}/jar
LOG_PATH=${BASE_PATH}/log
PRODUCT_PATH=${BASE_PATH}/product
SCRIPT_PATH=${BASE_PATH}/script
SQL_PATH=${BASE_PATH}/sql
LOG_FILE=${BASE_PATH}/test_product_ingestor_${TIMESTAMP}.log

export JARSDIR=${JAR_PATH}
export PYTHONPATH=${SCRIPT_PATH}:${PYTHONPATH}
export SCRIPT_FOR_PI=${SCRIPT_PATH}/scriptForPI.py

# Properties file settings
DEFAULT_ARCHIVE_PROPERTIES_PATH="./config/archiveConfig.properties"
ACSDATA_ARCHIVE_PROPERTIES_PATH="${ACS_DATA}/config/archiveConfig.properties"

ALMA_DB_CONNECTION_PROPERTY_NAME="archive.test_product_ingestor.alma.db.connection"
ALMA_DB_USER_PROPERTY_NAME="archive.test_product_ingestor.alma.db.username"
ALMA_DB_PASSWORD_PROPERTY_NAME="archive.test_product_ingestor.alma.db.password"

NGAS_DB_CONNECTION_PROPERTY_NAME="archive.test_product_ingestor.ngas.db.connection"
NGAS_DB_USER_PROPERTY_NAME="archive.test_product_ingestor.ngas.db.username"
NGAS_DB_PASSWORD_PROPERTY_NAME="archive.test_product_ingestor.ngas.db.password"

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
    local message=" TEST  DEBUG - ${1}"
    local timestamp=$(date +'%F %T')
    echo -e "${timestamp}${message}"
    echo -e "${timestamp}${message}" >> ${LOG_FILE}
}

function log_info {
    local message=" TEST  INFO  - ${1}"
    local timestamp=$(date +'%F %T')
    echo -e "${timestamp}${message}"
    echo -e "${timestamp}${message}" >> ${LOG_FILE}
}

function log_warn {
    local message=" TEST  WARN  - ${1}"
    local timestamp=$(date +'%F %T')
    echo -e "${ORANGE}${timestamp}${message}${RESET}"
    echo -e "${timestamp}${message}" >> ${LOG_FILE}
}

function log_error {
    local message=" TEST  ERROR - ${1}"
    local timestamp=$(date +'%F %T')
    echo -e "${RED}${timestamp}${message}${RESET}"
    echo -e "${timestamp}${message}" >> ${LOG_FILE}
}

function log_pass {
    local message=" TEST  PASS  - ${1}"
    local timestamp=$(date +'%F %T')
    echo -e "${GREEN}${timestamp}${message}${RESET}"
    echo -e "${timestamp}${message}" >> ${LOG_FILE}
}

function log_fail {
    local message=" TEST  FAIL  - ${1}"
    local timestamp=$(date +'%F %T')
    echo -e "${RED}${timestamp}${message}${RESET}"
    echo -e "${timestamp}${message}" >> ${LOG_FILE}
}

function signal_handler {
    local signal=${1}
    log_error "Interrupted by system signal ${signal}. Exiting the test run."
    if [ -d ${test_log_path} ]; then
        mv ${LOG_FILE} ${test_log_file}
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
    echo "Usage: test_product_ingestor.sh [options] SOFTWARE_RELEASE PRODUCT_TARBALL"
    echo ""
    echo "Executes product ingestor integration test sequence:"
    echo ""
    echo "  1. Read configuration properties from archiveConfig.properties file"
    echo "  2. Delete entries in ALMA database tables"
    echo "  3. Disable constraints and triggers in ALMA database tables"
    echo "  4. Insert MOUS entries in ALMA database tables"
    echo "  5. Enable triggers in ALMA database tables"
    echo "  6. Reset counters in ALMA database tables"
    echo "  7. Create test run directory and extract MOUS product files"
    echo "  8. Execute product ingestor for MOUS products"
    echo "  9. Generate CSV dump files from database"
    echo "  10. Compare new CSV files with master CSV files"
    echo ""
    echo "Requires archiveConfig.properties for configuration settings"
    echo ""
    echo "Options:"
    echo ""
    echo "    -c CONFIG_FILE    Archive configuration file"
    echo "    -h                Display this help"
    echo "    -i OUS_UID        Observation unit set unqiue ID (OUS UID)"
    echo "    -m                Generate master CSV dump files"
    echo "    -r RUN_PATH       Test run directory path"
    echo "    -v                Verbose log messages"
}


# Main script body
# ================

# Set a signal handler to handle interruptions
trap "signal_handler" SIGINT SIGTERM

# Test run paths
test_run_path=${BASE_PATH}/test_run_${TIMESTAMP}
test_csv_path=${test_run_path}/csv
test_log_path=${test_run_path}/log
test_log_file=${test_log_path}/test_product_ingestor.log

# The exit_status is set to 1 when a test fails
exit_status=0
sanitized_ous_uid=""

# Command line options
config_file=""
master=0
ous_uid=""
run_path=""
verbose=0

while getopts "c:hi:mr:v" opt; do
    case ${opt} in
        c)
            config_file="${OPTARG}"
            ;;
        h)
            print_usage
            exit 0
            ;;
        i)
            ous_uid="${OPTARG}"
            ;;
        m)
            master=1
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

if [ $# -ne 2 ]; then
    echo "Missing SOFTWARE_RELEASE PRODUCT_TARBALL arguments!"
    print_usage
    exit 1
fi

software_release="${1}"
product_tarball="${2}"

if [ -n "${config_file}" ]; then
    log_debug "Command line option config_file is ${config_file}"
else
    log_debug "Command line option config_file is empty"
fi
log_debug "Command line option master is ${master}"
if [ -n "${ous_uid}" ]; then
    log_debug "Command line option ous_uid is ${ous_uid}"
else
    log_debug "Command line option ous_uid is empty"
fi
if [ -n "${run_path}" ]; then
    log_debug "Command line option run_path is ${run_path}"
else
    log_debug "Command line option run_path is empty"
fi
log_debug "Command line option verbose is ${verbose}"
log_debug "Command line argument software_release is ${software_release}"
log_debug "Command line argument product_tarball is ${product_tarball}"

log_info "Testing product_ingestor version ${software_release} with test product tarball ${product_tarball}"

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
alma_db_connection=$(get_property ${archive_config_file} ${ALMA_DB_CONNECTION_PROPERTY_NAME})
alma_db_user=$(get_property ${archive_config_file} ${ALMA_DB_USER_PROPERTY_NAME})
alma_db_password=$(get_property ${archive_config_file} ${ALMA_DB_PASSWORD_PROPERTY_NAME})

ngas_db_connection=$(get_property ${archive_config_file} ${NGAS_DB_CONNECTION_PROPERTY_NAME})
ngas_db_user=$(get_property ${archive_config_file} ${NGAS_DB_USER_PROPERTY_NAME})
ngas_db_password=$(get_property ${archive_config_file} ${NGAS_DB_PASSWORD_PROPERTY_NAME})

log_debug "ALMA DB connection property value is ${alma_db_connection}"
log_debug "ALMA DB user property value is ${alma_db_user}"
#log_debug "ALMA DB password property value is ${alma_db_password}"
log_debug "NGAS DB connection property value is ${ngas_db_connection}"
log_debug "NGAS DB user property value is ${ngas_db_user}"
#log_debug "NGAS DB password property value is ${ngas_db_password}"

if [ -n "${run_path}" ]; then
    test_run_path=${run_path}/test_run_${TIMESTAMP}
    test_csv_path=${test_run_path}/csv
    test_log_path=${test_run_path}/log
    test_log_file=${test_log_path}/test_product_ingestor.log
fi

# Extract the OUS UID value from the product tarball name unless one is
# provided as a command line option value
if [ -n "${ous_uid}" ]; then
    sanitized_ous_uid=$(echo ${ous_uid} | sed 's/[:|\/]/_/g')
else
    sanitized_ous_uid=$(echo "${product_tarball}" | sed "s/^test_product_[0-9]\{2\}_\(uid___.*\).tar$/\1/")
    ous_uid=$(echo "${sanitized_ous_uid}" | sed 's~___~://~g' | sed 's~_~/~g')
fi

# TODO: verify that the OUS UID value is a correctly formatted value

# Check the ngas_cleaner.sh script file is available
ngas_cleaner_script_path=${SCRIPT_PATH}/ngas_cleaner.sh
if [ ! -f ${ngas_cleaner_script_path} ]; then
    log_error "NGAS cleaner script ${ngas_cleaner_script_path} not found!"
    exit 1
fi

# Check the product tarball file is available
product_tarball_path=${PRODUCT_PATH}/${product_tarball}
if [ ! -f ${product_tarball_path} ]; then
    log_error "Product tarball ${product_tarball_path} not found!"
    exit 1
fi

# Check the product_ingestor.sh script file is available
product_ingestor_script_path=${SCRIPT_PATH}/product_ingestor.sh
if [ ! -f ${product_ingestor_script_path} ]; then
    log_error "Product ingestor script ${product_ingestor_script_path} not found!"
    exit 1
fi

# Check the product_analyzer.py file is available
product_analyzer_path=${SCRIPT_PATH}/product_analyzer.py
if [ ! -f ${product_analyzer_path} ]; then
    log_error "Product analyzer script ${product_analyzer_path} not found!"
    exit 1
fi

# Check the product-ingestor jar file is available
product_ingestor_jar="product-ingestor-${software_release}-all.jar"
product_ingestor_jar_path=${JAR_PATH}/${product_ingestor_jar}
if [ ! -f ${product_ingestor_jar_path} ]; then
    log_error "Product ingestor tool ${product_ingestor_jar_path} not found!"
    exit 1
fi

# Check the test-sql-builder jar file is available
test_sql_builder_jar="test-sql-builder-${software_release}-all.jar"
test_sql_builder_jar_path=${JAR_PATH}/${test_sql_builder_jar}
if [ ! -f ${test_sql_builder_jar_path} ]; then
    log_error "Test SQL builder tool ${test_sql_builder_jar_path} not found!"
    exit 1
fi

if [ -d ${test_run_path} ]; then
    log_error "Test run directory ${test_run_path} already exists"
    exit 1
fi

log_info "Creating test run directory ${test_run_path}"
mkdir ${test_run_path}
if [ ! -d ${test_run_path} ]; then
    log_error "Failed to create test run directory ${test_run_path}"
    exit 1
fi

log_info "Creating test run CSV directory ${test_csv_path}"
mkdir ${test_csv_path}
if [ ! -d ${test_csv_path} ]; then
    log_error "Failed to create test run CSV directory ${test_csv_path}"
    exit 1
fi

log_info "Creating test run log directory ${test_log_path}"
mkdir ${test_log_path}
if [ ! -d ${test_log_path} ]; then
    log_error "Failed to create test run log directory ${test_log_path}"
    exit 1
fi

alma_db_connect="${alma_db_user}/${alma_db_password}@${alma_db_connection}"
ngas_db_connect="${ngas_db_user}/${ngas_db_password}@${ngas_db_connection}"

#log_info "Using ALMA database connection: ${alma_db_connect}"
#log_info "Using NGAS database connection: ${ngas_db_connect}"

log_info "Using ALMA database connection: ${alma_db_user}/######@${alma_db_connection}"
log_info "Using NGAS database connection: ${ngas_db_user}/######@${ngas_db_connection}"

log_info "Deleting entries in ALMA database tables"
sqlplus -S ${alma_db_connect} @sql/alma_delete_all_data.sql >> /dev/null

log_info "Disabling constraints in ALMA database tables"
sqlplus -S ${alma_db_connect} @sql/constraints_disable.sql >> /dev/null

log_info "Disabling triggers in ALMA database tables"
sqlplus -S ${alma_db_connect} @sql/triggers_disable.sql >> /dev/null

log_info "Inserting entries for OUS UID ${ous_uid} in ALMA database tables"
sqlplus -S ${alma_db_connect} @sql/test_sql_builder_${sanitized_ous_uid}.sql >> /dev/null

#log_info "Enabling constraints in ALMA database tables"
#sqlplus -S ${alma_db_connect} @sql/constraints_enable.sql >> /dev/null

log_info "Enabling triggers in ALMA database tables"
sqlplus -S ${alma_db_connect} @sql/triggers_enable.sql >> /dev/null

log_info "Reseting sequence counters in ALMA database tables"
sqlplus -S ${alma_db_connect} @sql/sequence_counter_reset.sql >> /dev/null

log_info "Cleaning NGAS products"
${ngas_cleaner_script_path} -c ${archive_config_file} -l ${test_log_path}/ngas_cleaner.log
if [ $? -ne 0 ]; then
    log_error "NGAS cleanup failed"
    exit 1
fi

# Prepare the pipeline products for add ingestion
test_product_add_path="${test_run_path}/test_add_${sanitized_ous_uid}"
if [ -d ${test_product_add_path} ]; then
    log_error "Test product directory ${test_product_add_path} already exists"
    exit 1
fi

log_info "Creating new test product directory for add ingestion ${test_product_add_path}"
mkdir ${test_product_add_path}
if [ ! -d ${test_product_add_path} ]; then
    log_error "Failed to create test product directory ${test_product_add_path}"
    exit 1
fi

log_info "Extracting test products tarball ${product_tarball} for adding OUS UID ${ous_uid}"
tar -xf ${PRODUCT_PATH}/${product_tarball} -C ${test_product_add_path}

find_products_path=$(find ${test_product_add_path} -regex ".*/product[s]?")
if [ -n ${find_products_path} ]; then
    log_info "Found products directory ${find_products_path}"
    test_product_add_ingest_path=$(dirname ${find_products_path})
else 
    log_info "Products directory not found. Using top level directory."
    test_product_add_ingest_path=$(test_product_add_path)
fi

product_ingestor_1_add_log_file=${test_log_path}/product_ingestor_1_add_${sanitized_ous_uid}.log

master_dump_alma_1_add_file=${CSV_PATH}/master_dump_alma_1_add_${sanitized_ous_uid}.csv
master_dump_ngas_1_add_file=${CSV_PATH}/master_dump_ngas_1_add_${sanitized_ous_uid}.csv

test_sql_builder_alma_1_add_log_file=${test_log_path}/test_sql_builder_alma_1_add.log
test_sql_builder_ngas_1_add_log_file=${test_log_path}/test_sql_builder_ngas_1_add.log

log_info "Product ingestion step 1: Add pipeline products for OUS UID ${ous_uid}"
${product_ingestor_script_path} -t -l ${product_ingestor_1_add_log_file} -a -i ${ous_uid} ${test_product_add_ingest_path}

if [ $? -eq 0 ]; then
    log_info "Product ingestion for adding ${ous_uid} completed successfully"
else
    log_error "Product ingestion for adding ${ous_uid} reported problems"
fi


if [ ${master} -eq 1 ]; then
    alma_dump_file=${test_csv_path}/master_dump_alma_1_add_${sanitized_ous_uid}.csv
    ngas_dump_file=${test_csv_path}/master_dump_ngas_1_add_${sanitized_ous_uid}.csv
else
    alma_dump_file=${test_csv_path}/dump_alma_1_add_${sanitized_ous_uid}.csv
    ngas_dump_file=${test_csv_path}/dump_ngas_1_add_${sanitized_ous_uid}.csv
fi

log_info "Dumping ALMA database tables to CSV file"
java -Dlogfile=${test_sql_builder_alma_1_add_log_file} -jar ${test_sql_builder_jar_path} --alma --csv --purify --file ${alma_dump_file} ${ous_uid}

log_info "Dumping NGAS database tables to CSV file"
java -Dlogfile=${test_sql_builder_ngas_1_add_log_file} -jar ${test_sql_builder_jar_path} --ngas --csv --purify --file ${ngas_dump_file}

if [ ${master} -ne 1 ]; then
    result=$(diff -q -s ${master_dump_alma_1_add_file} ${alma_dump_file})
    if [ $? -eq 0 ]; then
        log_pass "${result}"
    else
        log_fail "${result}"
        exit_status=1
    fi

    result=$(diff -q -s ${master_dump_ngas_1_add_file} ${ngas_dump_file})
    if [ $? -eq 0 ]; then
        log_pass "${result}"
    else
        log_fail "${result}"
        exit_status=1
    fi
fi

# Prepare the pipeline products for replace ingestion
test_product_replace_path="${test_run_path}/test_replace_${sanitized_ous_uid}"
if [ -d ${test_product_replace_path} ]; then
    log_error "Test product directory ${test_product_replace_path} already exists"
    exit 1
fi

log_info "Creating new test product directory for add ingestion ${test_product_replace_path}"
mkdir ${test_product_replace_path}
if [ ! -d ${test_product_replace_path} ]; then
    log_error "Failed to create test product directory ${test_product_replace_path}"
    exit 1
fi

log_info "Extracting test products tarball ${product_tarball} for replacing OUS UID ${ous_uid}"
tar -xf ${PRODUCT_PATH}/${product_tarball} -C ${test_product_replace_path}

find_products_path=$(find ${test_product_replace_path} -regex ".*/product[s]?")
if [ -n ${find_products_path} ]; then
    log_info "Found products directory ${find_products_path}"
    test_product_replace_ingest_path=$(dirname ${find_products_path})
else 
    log_info "Products directory not found. Using top level directory."
    test_product_replace_ingest_path=$(test_product_replace_path)
fi

product_ingestor_2_replace_log_file=${test_log_path}/product_ingestor_2_replace_${sanitized_ous_uid}.log

master_dump_alma_2_replace_file=${CSV_PATH}/master_dump_alma_2_replace_${sanitized_ous_uid}.csv
master_dump_ngas_2_replace_file=${CSV_PATH}/master_dump_ngas_2_replace_${sanitized_ous_uid}.csv

test_sql_builder_alma_2_replace_log_file=${test_log_path}/test_sql_builder_alma_2_replace.log
test_sql_builder_ngas_2_replace_log_file=${test_log_path}/test_sql_builder_ngas_2_replace.log

log_info "Product ingestion step 2: Replace pipeline products for OUS UID ${ous_uid}"
${product_ingestor_script_path} -t -l ${product_ingestor_2_replace_log_file} -r -i ${ous_uid} ${test_product_replace_ingest_path}

if [ $? -eq 0 ]; then
    log_info "Product ingestion for replacing ${ous_uid} completed successfully"
else
    log_error "Product ingestion for replacing ${ous_uid} reported problems"
fi

if [ ${master} -eq 1 ]; then
    alma_dump_file=${test_csv_path}/master_dump_alma_2_replace_${sanitized_ous_uid}.csv
    ngas_dump_file=${test_csv_path}/master_dump_ngas_2_replace_${sanitized_ous_uid}.csv
else
    alma_dump_file=${test_csv_path}/dump_alma_2_replace_${sanitized_ous_uid}.csv
    ngas_dump_file=${test_csv_path}/dump_ngas_2_replace_${sanitized_ous_uid}.csv
fi

log_info "Dumping ALMA database tables to CSV file"
java -Dlogfile=${test_sql_builder_alma_2_replace_log_file} -jar ${test_sql_builder_jar_path} --alma --csv --purify --file ${alma_dump_file} ${ous_uid}

log_info "Dumping NGAS database tables to CSV file"
java -Dlogfile=${test_sql_builder_ngas_2_replace_log_file} -jar ${test_sql_builder_jar_path} --ngas --csv --purify --file ${ngas_dump_file}

if [ ${master} -ne 1 ]; then
    result=$(diff -q -s ${master_dump_alma_2_replace_file} ${alma_dump_file})
    if [ $? -eq 0 ]; then
        log_pass "${result}"
    else
        log_fail "${result}"
        exit_status=1
    fi

    result=$(diff -q -s ${master_dump_ngas_2_replace_file} ${ngas_dump_file})
    if [ $? -eq 0 ]; then
        log_pass "${result}"
    else
        log_fail "${result}"
        exit_status=1
    fi
fi

# Always the last step
mv ${LOG_FILE} ${test_log_file}

exit ${exit_status}

