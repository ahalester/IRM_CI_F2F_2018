#!/bin/bash
#*******************************************************************************
# ALMA - Atacama Large Millimeter Array
# Copyright (c) ESO - European Southern Observatory, 2013
# (in the framework of the ALMA collaboration).
# All rights reserved.
#
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
#*******************************************************************************
#
# "@(#) $Id: all_tests.sh 196781 2013-10-25 14:10:59Z pkratzer $"
#
# Helper script to run ALMA-OT GUI test for all telescope types
#
# IMPORTANT NOTE:
# This script requires ssh access to the user/host controlling the
# submission service when this test and the submission service 
# are running on different hosts.
#

# global variables

# location of the submission service configuration file
#SERVICE_CONFIG="tomcat5/conf/Catalina/localhost/ObsprepSubmissionService.xml"
SERVICE_CONFIG="/usr/share/tomcat7/webapps/ObsprepSubmissionService/META-INF/context.xml"
LOCKDIR="/root/.ottest.lock"
LOCALFILENAME=$(basename "${SERVICE_CONFIG}")

###############################################################################

MYNAME=$(basename "${0}")
DIRNAME=$(dirname ${0})
if [[ "${DIRNAME}" == "." ]]
then
  BINDIR=$(pwd)
else
  BINDIR="${DIRNAME}"
fi

#set -x

echo "${MYNAME}: Sourcing common settings and functions..."
source "${BINDIR}/common"
echo "OK"

TIMESTAMP=$(getTimeStamp)
MODROOT=$(dirname ${BINDIR})
LOGDIR="${MODROOT}/log"
#[[ ! -d "${LOGDIR}" ]] && mkdir -p "${LOGDIR}"
[[ -d "${LOGDIR}" ]] && mv -f "${LOGDIR}" "${LOGDIR}.${TIMESTAMP}"
mkdir -p "${LOGDIR}"
WORKDIR="${MODROOT}/work"
[[ ! -d "${WORKDIR}" ]] && mkdir -p "${WORKDIR}"

###############################################################################
# functions

#function getRemoteACSDATA () {
#  HOST="${1}"
#  USER="${2}"
#
#  ssh -o "BatchMode=yes" "${SERVICE_USER}@${HOST}" '/bin/bash -l -c "echo +START+\$ACSDATA"+END+' | fgrep '+START+' | sed -e 's#^.*+START+##' -e 's#+END+.*$##'
#  #echo "${RESULT}"
#}

function locateSubmissionService () {
  SERVICE="${1}"
  CURRENT=$(hostname | cut -d. -f1)
  #REFERENCE=$(echo "${SUBMISSION_SERVICE}" | sed -e 's#^[a-zA-Z]*://##' -e 's#:[0-9]*$##')
  REFERENCE=$(getServiceHostName "${SERVICE}" | cut -d. -f1)
  if [[ "${CURRENT}" == "${REFERENCE}" ]]
  then
    echo "LOCAL"
  else
    echo "REMOTE"
  fi
}

function getObservingToolConfig () {
  _HOST="${1}"
  _USER="${2}"
  _MODE="${3}"
  _LOCALCOPY="${4}"

  if [[ -f "${_LOCALCOPY}" ]]
  then
    rm -rf "${_LOCALCOPY}"
  fi

  if [[ "${_MODE}" == "LOCAL" ]]
  then
    cat "${SERVICE_CONFIG}" >"${_LOCALCOPY}"
  else
    #HOST=$(getServiceHostName "${SUBMISSION_SERVICE}")
    #REMACSDATA=$(getRemoteACSDATA "${_HOST}" "${_USER}")
    ssh -o "BatchMode=yes" "${_USER}@${_HOST}" "/bin/bash -c 'cat ${SERVICE_CONFIG}'" >"${_LOCALCOPY}"
  fi

  if [[ ! -f "${_LOCALCOPY}" ]]
  then
    echo "ERROR: Copying configuration file to '${_LOCALCOPY}' failed." >&2
    return 1
  else
    _CHECK=$(egrep 'name="Telescope"[ \t\n]*value="[^"]*"' "${_LOCALCOPY}")
    #_CHECK=$(egrep '^Telescope[ \t\n]*=.*$' "${_LOCALCOPY}")
    if [[ -z "${_CHECK}" ]]
    then
      echo -e "\nERROR: Submission service configuration file\n   '${_LOCALCOPY}'\ndoes not contain expected configuration entry.\n" >&2
      return 2
    else
      return 0
    fi
  fi
}

function setObservingToolConfig () {
  _HOST="${1}"
  _USER="${2}"
  _MODE="${3}"
  _FILE="${4}"

  echo "Installing new tomcat configuration file..."
  if [[ "${_MODE}" == "LOCAL" ]]
  then
    cp -f "${_FILE}" "${SERVICE_CONFIG}"
    sleep 5
    # restart tomcat to activate new configuration
    echo "Restarting tomcat for user $(whoami) on local host..."
    service tomcat7 restart
    sleep 10
    service tomcat7 status
  else
    #HOST=$(getServiceHostName "${SUBMISSION_SERVICE}")
    #REMACSDATA=$(getRemoteACSDATA "${_HOST}" "${_USER}")
    _CONFIGDIR=$(dirname "${SERVICE_CONFIG}")
    cat "${_FILE}" | ssh -o "BatchMode=yes" "${_USER}@${_HOST}" "/bin/bash -l -c 'cat > ${SERVICE_CONFIG}
sleep 5
ls -l ${_CONFIGDIR}/
echo
# restart tomcat to activate new configuration
echo \"Restarting tomcat for user \$(whoami) on host \$(hostname)...\"
service tomcat7 restart
sleep 10
service tomcat7 status
'"
  fi
  echo "-------------------------------------------"
}


function createLockDir () {
  _HOST="${1}"
  _USER="${2}"
  _LOCKDIR="${3}"

  _CHECK=$(ssh -o "BatchMode=yes" "${_USER}@${_HOST}" "/bin/bash -l -c '
ls -a ${_LOCKDIR} 2>/dev/null >&2
if [[ \${?} -ne 0 ]]
then
  mkdir -p -m 700 ${_LOCKDIR}
  #echo \"Lock directory ${_LOCKDIR} created\" >&2
  echo 0
else
  echo \"Lock directory ${_LOCKDIR} exists already on host ${_HOST}\" >&2
  echo 1
fi
'")

  return ${_CHECK}
}


function removeLockDir () {
  _HOST="${1}"
  _USER="${2}"
  _LOCKDIR="${3}"

  ssh -o "BatchMode=yes" "${_USER}@${_HOST}" "/bin/bash -l -c 'rm -rf ${_LOCKDIR}'"
}


function createLockFile () {
  _HOST="${1}"
  _USER="${2}"
  _LOCKDIR="${3}"
  _TELTYPE="${4}"
  _CLIENT="${5}"

  _LOCKFILE="${_LOCKDIR}/${_TELTYPE}"
  if [[ -n "${_CLIENT}" ]]
  then
    _LOCKFILE="${_LOCKFILE}.${_CLIENT}"
  fi

  _CHECK=$(ssh -o "BatchMode=yes" "${_USER}@${_HOST}" "/bin/bash -l -c '
touch ${_LOCKFILE} >/dev/null 2>&1
if [[ \${?} -eq 0 ]]
then
  echo 0
else
  echo \"Cannot create lock file ${_LOCKFILE} on host ${_HOST}\" >&2
  echo 1
fi
'")

  return ${_CHECK}
}


function removeLockFile () {
  _HOST="${1}"
  _USER="${2}"
  _LOCKDIR="${3}"
  _TELTYPE="${4}"
  _CLIENT="${5}"

  _LOCKFILE="${_LOCKDIR}/${_TELTYPE}"
  if [[ -n "${_CLIENT}" ]]
  then
    _LOCKFILE="${_LOCKFILE}.${_CLIENT}"
  fi

  _CHECK=$(ssh -o "BatchMode=yes" "${_USER}@${_HOST}" "/bin/bash -l -c '
rm -f ${_LOCKFILE} >/dev/null 2>&1
if [[ \${?} -eq 0 ]]
then
  echo 0
else
  echo \"Cannot remove lock file ${_LOCKFILE} on host ${_HOST}\" >&2
  echo 1
fi
'")

  return ${_CHECK}
}


function checkLockFile () {
  _HOST="${1}"
  _USER="${2}"
  _LOCKDIR="${3}"
  _TELTYPE="${4}"
  _CLIENT="${5}"

  _LOCKFILE="${_LOCKDIR}/${_TELTYPE}"
  if [[ -n "${_CLIENT}" ]]
  then
    _LOCKFILE="${_LOCKFILE}.${_CLIENT}"
  #else
#  _LOCKFILE="${_LOCKFILE}.*"
  fi

  _CHECK=$(ssh -o "BatchMode=yes" "${_USER}@${_HOST}" "/bin/bash -l -c '
ls ${_LOCKFILE} >/dev/null 2>&1
if [[ \${?} -eq 0 ]]
then
  echo 0
else
  echo 1
fi
'")

  return ${_CHECK}
}


function listLockedHosts () {
  _HOST="${1}"
  _USER="${2}"
  _LOCKDIR="${3}"
  _TELTYPE="${4}"

  _LOCKFILES="${_TELTYPE}.*"

  _RESULT=$(ssh -o "BatchMode=yes" "${_USER}@${_HOST}" "/bin/bash -l -c '
cd ${_LOCKDIR} && ls ${_LOCKFILES} 2>/dev/null | sed -e "s#^.*${_TELTYPE}\.##g"
'")

  echo "${_RESULT}" | sed -e 's#^#- #'
}



###############################################################################
# main programme

DATE=$(getDate)

# telescope types to be tested
TEL_LIST=$(getTelescopeTypes)

# test submission service
# (usually replaced by command-line parameters)
SUBMISSION_SERVICE=$(getSubmissionService)
SERVICE_USER=$(getSubmissionServiceOwner)

# evaluate command-line options
ALMA_OT_PKG=
DEBUG=
DEBUGOPT=
JAVA_COMMAND=
REVISION=
MASTER=
CLIENT=
CURRENTHOST=$(hostname | cut -d. -f1)
SYNCWAIT=300
while [[ ${#} -gt 0 ]]
do
  # Limited fast mode for debugging
  if [[ "${1}" == "--debug" ]]
  then
    shift
    DEBUG=true
    DEBUGOPT="--debug"
    SYNCWAIT=30
    echo "${MYNAME}: WARNING! DEBUGGING MODE ACTIVE (LIMITED TEST EXECUTION)"

  # ALMA-OT package selection
  elif [[ "${1}" == "-p" ]]
  then
    shift
    if [[ ${#} -lt 1 ]]
    then
      echo "ERROR: Missing parameter for option -p" >&2
      #usage
      exit 1
    else
      ALMA_OT_PKG="${1}"
      echo "ALMA-OT package selected: ${ALMA_OT_PKG}"
      shift
    fi

  # submission service selection
  elif [[ "${1}" == "-s" ]]
  then
    shift
    if [[ ${#} -lt 1 ]]
    then
      echo "ERROR: Missing parameter for option -s" >&2
      #usage
      exit 1
    else
      SUBMISSION_SERVICE="${1}"
      echo "Submission service selected: ${SUBMISSION_SERVICE}"
      shift
    fi

  # Java command selection
  elif [[ "${1}" == "-j" ]]
  then
    shift
    if [[ ${#} -lt 1 ]]
    then
      echo "ERROR: Missing parameter for option -j" >&2
      usage
      exit 1
    else
      JAVA_COMMAND="${1}"
      if [[ ! -x ${JAVA_COMMAND} ]]
      then
        echo "ERROR: '${JAVA_COMMAND}' not found or missing execute permissions"
        exit 1
      fi
      shift
    fi

  # revision info (for report identification only)
  elif [[ "${1}" == "-r" ]]
  then
    shift
    if [[ ${#} -lt 1 ]]
    then
      echo "ERROR: Missing parameter for option -r" >&2
      #usage
      exit 1
    else
      REVISION="${1}"
      echo "Additional revision info: ${REVISION}"
      shift
    fi

  # run as master
  elif [[ "${1}" == "-m" ]]
  then
    shift
    if [[ ${CLIENT} ]]
    then
      echo "ERROR: options -c and -m are mutually exclusive" >&2
      exit 1
    else
      MASTER=true
    fi

  # run as client
  elif [[ "${1}" == "-c" ]]
  then
    shift
    if [[ ${MASTER} ]]
    then
      echo "ERROR: options -m and -c are mutually exclusive" >&2
      exit 1
    else
      CLIENT=true
    fi

  else
    echo "ERROR: Unknown option ${1}"
    exit 1

  fi
done

#set -x 

if [[ "${OS}" == "Windows_NT" ]]
then
  OS=Windows
else
  type -a sw_vers >/dev/null 2>&1
  if [[ ${?} -eq 0 ]]
  then
    OS="MacOSX"
  else
    OS="Linux"
  fi
fi

{
getNiceTimeStamp

if [[ ${MASTER} ]]
then
  echo "Running in *MASTER MODE*"
elif [[ ${CLIENT} ]]
then
  echo "Running in *CLIENT MODE*"
else
  echo "Running in *STANDARD MODE*"
fi 

echo "Telescope types to be tested:"
echo "${TEL_LIST}"

MODE=$(locateSubmissionService "${SUBMISSION_SERVICE}")
HOST=$(getServiceHostName "${SUBMISSION_SERVICE}")

ADD_OPTIONS=-b

if [[ -n "${JAVA_COMMAND}" ]]
then
  ADD_OPTIONS="${ADD_OPTIONS} -j ${JAVA_COMMAND}"
fi

if [[ -n "${ALMA_OT_PKG}" ]]
then
  ADD_OPTIONS="${ADD_OPTIONS} -p -"
fi

if [[ "${MODE}" == "REMOTE" ]]
then
  echo "Checking access to remote submission service host..."
  checkRemoteLogin "${HOST}" "${SERVICE_USER}"
  RETVAL=${?}
  if [[ ${RETVAL} -ne 0 ]]
  then
    echo "ERROR: Remote access to submission service host via user '${SERVICE_USER}@${HOST}' failed." >&2
    exit 2
  else
    echo "OK"
  fi
fi


if [[ ${MASTER} ]]
then
  echo "Checking lock directory on remote submission service host..."
  createLockDir "${HOST}" "${SERVICE_USER}" "${LOCKDIR}"
  RETVAL=${?}
  if [[ ${RETVAL} -eq 0 ]]
  then
    echo "Lock directory ${LOCKDIR} created" >&2
  else
    echo "Another master is running already or the previous master crashed."
    echo "In case of failure please remove the lock directory and restart the master."
    exit 10
  fi
fi

LOCALFILENAME="${WORKDIR}/${LOCALFILENAME}"

# extract ALMA-OT package if selected
if [[ -n "${ALMA_OT_PKG}" ]]
then
  # package name "-" stands for installed package in work directory
  if [[ "${ALMA_OT_PKG}" != "-" ]]
  then
    # check (and correct if neccessary) path to ALMA-OT package
    CHECK=$(echo "${ALMA_OT_PKG}" | cut -c 1)
    if [[ "${CHECK}" != "/" ]]
    then
      # relative path needs adaptation
      ALMA_OT_PKG="$(pwd)/${ALMA_OT_PKG}"
    fi
    # extract software from package and patch submission service
    "${BINDIR}/extract_ot_pkg.sh" -s "${SUBMISSION_SERVICE}" "${ALMA_OT_PKG}"
    RETVAL=${?}
  else
    # skip extraction
    RETVAL=0
  fi
  if [[ ${RETVAL} -ne 0 ]]
  then
    echo "ERROR: ALMA-OT package extraction failed." >&2
    exit 3
  else
    PKGDIR=$(ls -1d ${WORKDIR}/ot_inst/ALMAOT-*/)
    RETVAL=${?}
    if [[ ${RETVAL} -ne 0 ]]
    then
      echo "ERROR: Cannot access ALMA-OT package directory '${PKGDIR}'." >&2
      exit 4
    fi
  fi
fi

# submission service is patched only in stand-alone or master mode
if [[ ! ${CLIENT} ]]
then
  getObservingToolConfig "${HOST}" "${SERVICE_USER}" "${MODE}" "${LOCALFILENAME}"
  RETVAL=${?}
  if [[ ${RETVAL} -ne 0 ]]
  then
    echo "Aborting test run due to previous irrecoverable error(s)."
    exit 5
  fi

  # prepare configuration files for all telescope types
  #if [[ ! -f "${SERVICE_CONFIG}" ]]
  if [[ ! -f "${LOCALFILENAME}" ]]
  then
    echo -e "\nERROR: Submission service configuration file\n   '${LOCALFILENAME}'\nnot found.\n" >&2
    exit 6
  else
    echo '============================================================================'
    echo "Creating configuration files for all telescope types..."
    for TEL in ${TEL_LIST}
    do
      #NEWNAME=ObsprepSubmissionService_${TEL}.xml 
      NEWNAME=$(echo "${LOCALFILENAME}" | sed -e "s#\.xml\$#_${TEL}.xml#")
      echo "Creating configuration file ${NEWNAME}..."
  
      cat "${LOCALFILENAME}" | \
        sed -e 's#name="Telescope"[ \t\n]*value="[^"]*"#name="Telescope" value="'${TEL}'"#g' | \
        #sed -e 's#^Telescope[ \t\n]*=.*$#Telescope='${TEL}'#g' | \
        cat >"${NEWNAME}"
  
      # check result entry
      CHECK=$(egrep 'name="Telescope"[ \t\n]*value="'${TEL}'"' "${NEWNAME}")
      if [[ -z "${CHECK}" ]]
      then
        echo -e "\nERROR: Generated configuration file\n   '${NEWNAME}'\ninconsistent.\n" >&2
        exit 7
      fi
    done
    echo "Configuration files for all telescope types created."
  fi
fi

echo
echo "Starting tests for all telescope types..."
echo

# for statistics output
declare -a TESTTIME

# run test for each telescope configuration
INDEX=0
for TEL in ${TEL_LIST}
do
  echo
  echo '============================================================================'
  echo "Running test for telescope configuration ${TEL}..."
  echo

  # submission service is patched only in stand-alone or master mode
  if [[ ! ${CLIENT} ]]
  then
    # install and activate configuration file for selected telescope type
    CFGNAME=$(echo "${LOCALFILENAME}" | sed -e "s#\.xml\$#_${TEL}.xml#")
    setObservingToolConfig "${HOST}" "${SERVICE_USER}" "${MODE}" "${CFGNAME}"
  fi

  if [[ ${MASTER} ]]
  then
    echo "Creating lock file for telescope type ${TEL}..."
    createLockFile "${HOST}" "${SERVICE_USER}" "${LOCKDIR}" "${TEL}"
    echo "Done."

    ## not strictly required but useful for test monitoring
    #echo "Creating host lock file for telescope type ${TEL}..."
    #createLockFile "${HOST}" "${SERVICE_USER}" "${LOCKDIR}" "${TEL}" "${CURRENTHOST}"
    #echo "Done."
  fi

  if [[ ${CLIENT} ]]
  then
    RETVAL=1
    while [[ ${RETVAL} -ne 0 ]]
    do
      # wait for telescope type lock file (created by master) to appear
      checkLockFile "${HOST}" "${SERVICE_USER}" "${LOCKDIR}" "${TEL}"
      RETVAL=${?}
      if [[ ${RETVAL} -eq 1 ]]
      then
        echo "Waiting until lock file for telescope type ${TEL} appears..."
        sleep ${SYNCWAIT}
      fi
    done
  fi

  # not strictly required for master but useful for test monitoring
  if [[ ${CLIENT} || ${MASTER} ]]
  then
    echo "Creating host lock file for telescope type ${TEL}..."
    createLockFile "${HOST}" "${SERVICE_USER}" "${LOCKDIR}" "${TEL}" "${CURRENTHOST}"
    echo "Done."
  fi

  STARTTIME=$(getSystemTime)

  # run test suite
  export TELESCOPE=${TEL}
  "${BINDIR}/run_test.sh" ${DEBUGOPT} ${ADD_OPTIONS} -s "${SUBMISSION_SERVICE}" -r "${REVISION}"

  ENDTIME=$(getSystemTime)
  TESTTIME[${INDEX}]=$(getTimeDiff ${STARTTIME} ${ENDTIME})

  if [[ ${CLIENT} || ${MASTER} ]]
  then
    echo "Removing host lock file for telescope type ${TEL}..."
    removeLockFile "${HOST}" "${SERVICE_USER}" "${LOCKDIR}" "${TEL}" "${CURRENTHOST}"
    echo "Done."
  fi

  if [[ ${MASTER} ]]
  then
    RETVAL=0
    while [[ ${RETVAL} -eq 0 ]]
    do
      # wait for lock files created by clients to disappear
      checkLockFile "${HOST}" "${SERVICE_USER}" "${LOCKDIR}" "${TEL}" "*"
      RETVAL=${?}
      if [[ ${RETVAL} -eq 0 ]]
      then
        echo "Waiting for host(s):"
        listLockedHosts "${HOST}" "${SERVICE_USER}" "${LOCKDIR}" "${TEL}"
        sleep ${SYNCWAIT}
      fi
    done

    # remove lock file for current telescope type
    echo "Removing lock file for current telescope type..."
    removeLockFile "${HOST}" "${SERVICE_USER}" "${LOCKDIR}" "${TEL}"
    echo "Done."
  fi

  sleep 10
  INDEX=$((${INDEX} + 1))
done

if [[ ${MASTER} ]]
then
  echo "Cleaning up lock directory on submission service host..."
  removeLockDir "${HOST}" "${SERVICE_USER}" "${LOCKDIR}"
  echo "Done."
fi


echo
echo '============================================================================'
echo

# output statistics
INDEX=0
for TEL in ${TEL_LIST}
do
  echo "Test time ${TEL} (h:min): ${TESTTIME[${INDEX}]}"
  INDEX=$((${INDEX} + 1))
done

echo "All tests completed."
getNiceTimeStamp
} 2>&1 | tee ${LOGDIR}/${DATE}_ALMA-OT_${OS}_TEST_LOG_${REVISION}.txt

echo "Collecting results in single archive..."
LOGARCHIVE=log_${OS}_${REVISION}_${TIMESTAMP}.tgz
cd ${LOGDIR} && tar cvzf ../${LOGARCHIVE} . && mv ../${LOGARCHIVE} .
echo "Done."
ls -ld ${LOGARCHIVE}

