#!/bin/bash
#*******************************************************************************
# ALMA - Atacama Large Millimeter Array
# Copyright (c) ESO - European Southern Observatory, 2011, 2013
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
# "@(#) $Id: run_test.sh 250369 2017-10-12 14:44:36Z psivera $"
#
# Execution script for ALMA-OT GUI test (using QF-Test)
#

# set -x

function terminateTestRun () {
  echo
  echo "Termination request received"
  echo "===================================================================="
  ps -f
  echo "===================================================================="
  # covers both QF-Test and the ALMA-OT
  SUBPROC=$(ps | egrep "java|ALMA-OT" | grep -F -v grep | awk '{ print $1 }')
  echo "Terminating test suite (and possible clients) with PID(s) "${SUBPROC}"..."
  echo
  kill ${SUBPROC}
  sleep 2
  echo "Done"
}

LINE="-----------------------------------------------------------------------"

DIRNAME=$(dirname "${0}")
MYNAME=$(basename "${0}")

if [[ "${DIRNAME}" == "." ]]
then
  BINDIR=$(pwd)
else
  BINDIR="${DIRNAME}"
fi

# by default the installed (=> PATH) ALMA-OT is used
ALMA_OT_PKG=

WINDOWS=
EXT=
PATHSEP="/"

# To be adapted when other operating systems are supported
if [[ "${OS}" == "Windows_NT" || "${OS}" == "Windows" ]]
then
  OS=Windows
  EXT=.exe
  PATHSEP="\\"
  WINDOWS=true
  ALMA_OT_PKG=-
else
  type -a sw_vers >/dev/null 2>&1
  if [[ ${?} -eq 0 ]]
  then
    OS="MacOSX"
    sw_vers
  else
    # default setting
    OS="Linux"
    if [[ -f /etc/redhat-release ]]
    then
      cat /etc/redhat-release
    fi
  fi
fi
SUITE_VARIABLES="-variable SYSTEM_OS=${OS}"

MODROOT=$(dirname "${BINDIR}")
if [[ ${WINDOWS} ]]
then
  LOGDIR="${MODROOT}/log"
  PASSLOGDIR="..${PATHSEP}log"
else
  LOGDIR="${MODROOT}/log"
  PASSLOGDIR="${LOGDIR}"
fi
WORKDIR="${MODROOT}/work"
TESTDIR="${MODROOT}/test"

JAVA_COMMAND=java
JAVA_VERSION_FILE="${WORKDIR}/java_version.txt"

echo "${MYNAME}: Sourcing common settings and functions..."
source "${BINDIR}/common"
echo "OK"

# create log, report and work directories if not existent
if [[ ! -d "${LOGDIR}" ]]
then
  echo "Creating log directory '${LOGDIR}'..."
  mkdir -p "${LOGDIR}"
fi
if [[ ! -d "${WORKDIR}" ]]
then
  echo "Creating work directory '${WORKDIR}'..."
  mkdir -p "${WORKDIR}"
fi

# create separate project work directories for each telescope type if not existent
TELTYPES=$(getTelescopeTypes)
for ONETYPE in ${TELTYPES}
do
  TTNAME="${WORKDIR}/Projects-${ONETYPE}"
  if [[ ! -d "${TTNAME}" ]]
  then
    echo "Creating work directory '${TTNAME}'..."
    mkdir -p "${TTNAME}"
  fi
done

if [[ "${LOCATION}" == "NRI" ]]
then    # NRI execution support

  # temporary restriction for integration into NRI
  #SELECTION="TestCaseVersionInfo"
  #SELECTION="TestCaseVersionInfo TestCasePrefsWin"
  SELECTION="TestCaseVersionInfo TestSetBasicOpsAndSettings"
  #SELECTION="TestCaseVersionInfo TestCaseSingleContinuumSetupICT530"

  # for NRI store everything in test directory for publishing
  LOGDIR=".QFTEST_LOG"
  TELESCOPE="autodetect"
fi

function usage() {
  echo "  usage: ${MYNAME} [-b] [-p ALMA-OT package] [-csv|-es|-c2|-c3|c4|c5|c6|c7|-full] [-j Java executable] [test set id|test case id]*"
  echo "         All options must precede any test case specification."
}

if [[ ! -d "${TESTDIR}" ]]
then
  echo "ERROR: Test directory '${TESTDIR}' not found." >&2
  exit 1
else
  cd "${TESTDIR}"
  RETVAL=$?
  if [[ ${RETVAL} -ne 0 ]]
  then
    echo "ERROR: Test directory '${TESTDIR}' not accessible." >&2
    exit 1
  fi
fi

echo "Test directory: $(pwd)"

TESTSUITE="ALMA-OT_Test_Suite.qft"
if [[ ! -f "${TESTSUITE}" ]]
then
  echo "ERROR: Test suite '${TESTSUITE}' not found." >&2
  exit 1
fi

# test suite parameters (defaults from test suite used)
#PREPARATIONS_DONE=
#BASIC_TESTS_ONLY=
#LARGE_NUM_PROJECTS=
#TELESCOPE=
#SELECTION=
# test suite parameters (override settings in test suite)
#PREPARATIONS_DONE=true
#BASIC_TESTS_ONLY=true
#LARGE_NUM_PROJECTS=true
#SELECTION=TestCaseVersionInfo

# software directory containing QF-Test must be mounted
#QFTBASE="/software/qftest"
QFTBASE=$(getQFTestBase)
if [[ ! -d "${QFTBASE}" ]]
then
  echo "QF-Test base directory '${QFTBASE}' not mounted!" >&2
  exit 1
fi

EXEC="${QFTBASE}/$(getQFTestVersion)/bin/qftest${EXT}"
#EXEC="${QFTBASE}/qftest-4.0.11/bin/qftest${EXT}"

# setting required for QF-Test
# default java memory to run the sanity checks under Jenkins in Garching
OPTIONS=" -engine 'awt,swt'"
# additional jvm memroy to run the full test suite; to be counter-checked
#OPTIONS="-J-Xmx1024m -engine 'awt,swt'"

# setting required for client (ALMA_OT)
export JAVA_OPTIONS="-XX:MaxPermSize=512m"

# setting of test suite variables (override settings in test suite)
if [[ -n "${PREPARATIONS_DONE}" ]]
then
  SUITE_VARIABLES="${SUITE_VARIABLES} -variable PreparationsDone=${PREPARATIONS_DONE}"
fi
if [[ -n "${BASIC_TESTS_ONLY}" ]]
then
  SUITE_VARIABLES="${SUITE_VARIABLES} -variable BasicTestsOnly=${BASIC_TESTS_ONLY}"
fi
if [[ -n "${LARGE_NUM_PROJECTS}" ]]
then
  SUITE_VARIABLES="${SUITE_VARIABLES} -variable LargeNumberProjects=${LARGE_NUM_PROJECTS}"
fi

# check command-line options and parameters
BATCHMODE=
DEBUG=
REVISION=
if [[ ${#} -gt 0 ]]
then
  if [[ "${1}" == "-h" || "${1}" == "--help" ]]
  then
    usage
    exit 0
  else
    while [[ ${#} -gt 0 ]]
    do
      # Limited fast mode for debugging
      if [[ "${1}" == "--debug" ]]
      then
        shift
        DEBUG=true
        echo "${MYNAME}: WARNING! DEBUGGING MODE ACTIVE (LIMITED TEST EXECUTION)"

      # batch mode
      elif [[ "${1}" == "-b" ]]
      then
        echo "Batch mode selected"
        BATCHMODE=true
        shift

      # ALMA-OT package selection
      elif [[ "${1}" == "-p" ]]
      then
        shift
        if [[ ${#} -lt 1 ]]
        then
          echo "ERROR: Missing parameter for option -p" >&2
          usage
          exit 1
        else
          ALMA_OT_PKG="${1}"
          echo "ALMA-OT package selected: ${ALMA_OT_PKG}"
          shift
        fi

      # telescope setting
      elif [[ "${1}" == "-csv" ]]
      then
        TELESCOPE="CSVALMA"
        shift
      elif [[ "${1}" == "-es" ]]
      then
        TELESCOPE="ESALMA"
        shift
      elif [[ "${1}" == "-c2" ]]
      then
        TELESCOPE="Cycle2"
        shift
      elif [[ "${1}" == "-c3" ]]
      then
        TELESCOPE="Cycle3"
        shift
      elif [[ "${1}" == "-c4" ]]
      then
        TELESCOPE="Cycle4"
        shift
      elif [[ "${1}" == "-c5" ]]
      then
        TELESCOPE="Cycle5"
        shift
      elif [[ "${1}" == "-c6" ]]
      then
        TELESCOPE="Cycle6"
        shift
      elif [[ "${1}" == "-c7" ]]
      then
        TELESCOPE="Cycle7"
        shift
      elif [[ "${1}" == "-full" ]]
      then
        TELESCOPE="FULL"
        shift
      elif [[ "${1}" == "-auto" ]]
      then
        TELESCOPE="autodetect"
        shift

      # submission service setting
      elif [[ "${1}" == "-s" ]]
      then
        shift
        if [[ ${#} -eq 0 ]]
        then
          echo "ERROR: Missing parameter for option -s" >&2
          usage
          exit 1
        else
          SUBMISSION_SERVICE="${1}"
          shift
        fi

      elif [[ "${1}" == "-j" ]]
      then
        shift
        if [[ ${#} -eq 0 ]]
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
        if [[ ${#} -eq 0 ]]
        then
          echo "ERROR: Missing parameter for option -r" >&2
          usage
          exit 1
        else
          REVISION="${1}"
          shift
        fi

      else
        # no more recognized option indicates the start of
        # test case specifications
        break

      fi
    done
  fi
fi

# assemble options for selected test cases (if specified)
TESTS=
TESTCOUNT=0
PREFIX="ALMA-OT"

if [[ ${BATCHMODE} ]]
then
  while [[ ${#} -gt 0 ]]
  do
    if [[ ${TESTCOUNT} -eq 0 ]]
    then
      PREFIX="${1}"
    fi
    TESTS="${TESTS} -test ${1}"
    TESTCOUNT=$(expr $TESTCOUNT + 1)
    shift
  done

  # add preselected tests to option list
  for TESTCASE in ${SELECTION}
  do
    if [[ ${TESTCOUNT} -eq 0 ]]
    then
      PREFIX="${TESTCASE}"
    fi
    TESTS="${TESTS} -test ${TESTCASE}"
    TESTCOUNT=$(expr $TESTCOUNT + 1)
    shift
  done
else
  if [[ -n "${*}${SELECTION}" ]]
  then
    echo "WARNING: Test selection ignored in interactive mode"
  fi
fi

if [[ ${DEBUG} && -z "${TESTS}" ]]
then
  TESTCASE="TestCaseVersionInfo"
  TESTS="-test ${TESTCASE}"
  echo "WARNING: Limiting test suite execution to test case(s): ${TESTCASE}"
fi

# submission service settings
if [[ -z "${SUBMISSION_SERVICE}" ]]
then
  # default setting
  SUBMISSION_SERVICE=$(getSubmissionService)
fi
# set variable for internal use in test suite
SUITE_VARIABLES="${SUITE_VARIABLES} -variable DBAddress=${SUBMISSION_SERVICE}"
# add Java option to provide correct setting at ALMA-OT startup
JAVA_OPTIONS="${JAVA_OPTIONS} -Dot.submission.host=${SUBMISSION_SERVICE}"

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
    "${BINDIR}/extract_ot_pkg.sh" -s "${SUBMISSION_SERVICE}" "${ALMA_OT_PKG}"
    RETVAL=${?}
  else
    # skip extraction but patch startup files with selected submission service
    PKGDIR=$(ls -1d ${WORKDIR}/ot_inst/ALMAOT-*/)
    RETVAL=${?}
    if [[ ${RETVAL} -ne 0 ]]
    then
      echo "ERROR: Cannot access ALMA-OT package directory '${PKGDIR}'." >&2
    else
      "${BINDIR}/patch_setup.sh" -d "${PKGDIR}" "${SUBMISSION_SERVICE}"
    fi
    #RETVAL=0
  fi
  if [[ ${RETVAL} -ne 0 ]]
  then
    echo "ERROR: ALMA-OT package extraction failed." >&2
    exit 2
  else
    PKGDIR=$(ls -1d ${WORKDIR}/ot_inst/ALMAOT-*/)
    RETVAL=${?}
    if [[ ${RETVAL} -ne 0 ]]
    then
      echo "ERROR: Cannot access ALMA-OT package directory '${PKGDIR}'." >&2
      exit 3
    else
      echo "Adding package directory '${PKGDIR}' to PATH..."
      export PATH="${PKGDIR}:${PATH}"
    fi
  fi
fi

# various parameter and variable settings depending on user selection
if [[ $TESTCOUNT -gt 1 ]]
then
  PREFIX="ALMA-OT"
fi
PREFIX="${PREFIX}_${OS}"
if [[ -n "${TELESCOPE}" ]]
then
  SUITE_VARIABLES="${SUITE_VARIABLES} -variable Telescope=${TELESCOPE}"
  PREFIX="${PREFIX}_${TELESCOPE}"
fi
if [[ -n "${REVISION}" ]]
then
  PREFIX="${PREFIX}_${REVISION}"
fi

#if [[ -n "${ALMA_OT_PKG}" || ${STANDALONE_OT_PKG} ]]
if [[ -n "${ALMA_OT_PKG}" ]]
then
  SUITE_VARIABLES="${SUITE_VARIABLES} -variable STANDALONE_OT_PKG=true"
fi

#if [[ ! ${STANDALONE_OT_PKG} && ${BATCHMODE} ]]
if [[ -z "${ALMA_OT_PKG}" && ${BATCHMODE} ]]
then
  if [[ -f "${ACSROOT}/BUILD_TAG" ]]
  then
    BUILD_TAG=$(head -1 "${ACSROOT}/BUILD_TAG")
  fi
else
  #if [[ ${BATCHMODE} && -n "${STANDALONE_BUILD_TAG}" ]]
  if [[ ${BATCHMODE} && -n "${PKGDIR}" ]]
  then
    #echo "Setting STANDALONE_BUILD_TAG '${STANDALONE_BUILD_TAG}' as report name."
    #OPTIONS="${OPTIONS} -report.name '${STANDALONE_BUILD_TAG}'"
    BUILD_TAG=$(head -1 "${WORKDIR}/ot_inst/BUILD_TAG")
  fi
fi
if [[ -n "${BUILD_TAG}" ]]
then
  echo "Setting BUILD_TAG '${BUILD_TAG}' as report name."
  OPTIONS="${OPTIONS} -report.name ${BUILD_TAG}"
fi

# define options for batch mode
if [[ ${BATCHMODE} ]]
then
  DATE=$(getDate)
  CHECK=$(ls -d ${LGODIR}/${DATE}_* 2>/dev/null)
  if [[ -z "${CHECK}" ]]
  then
    REPFORMAT="+Y-+M-+d"
  else
    # add time info when reports of the same day exist
    REPFORMAT="+Y-+M-+d_+h-+m"
  fi
  #OPTIONS="${OPTIONS} -batch -run -splitlog=false -runlog ${PASSLOGDIR}${PATHSEP}+Y-+M-+d_+h-+m_${PREFIX} -report ${PASSLOGDIR}${PATHSEP}+Y-+M-+d_+h-+m_${PREFIX} -nomessagewindow"
  OPTIONS="${OPTIONS} -batch -run -splitlog=false -runlog ${PASSLOGDIR}${PATHSEP}${REPFORMAT}_${PREFIX} -exitcodeignorewarning -report ${PASSLOGDIR}${PATHSEP}${REPFORMAT}_${PREFIX} -nomessagewindow"
fi

# Java command setting
OPTIONS="${OPTIONS} -java ${JAVA_COMMAND}"
echo -e "Using Java executable:\n   $(which ${JAVA_COMMAND})"

# provide info about used Java version
INFO=$(${JAVA_COMMAND} -version >"${JAVA_VERSION_FILE}" 2>&1)
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Getting Java version info failed."
else
  JAVA_VERSION=$(egrep 'java version "1\.[1-9][0-9]*[.0-9]*_[1-9][0-9]*"' "${JAVA_VERSION_FILE}" | sed -e 's#^.*"1\.##' -e 's#\..*$##')
  if [[ -z "${JAVA_VERSION}" ]]
  then
    echo "ERROR: Cannot determine current Java version."
  else
    echo "Found Java version '${JAVA_VERSION}'"
    if [[ ${JAVA_VERSION} -lt 6 ]]
    then
      echo "ERROR: ALMA-OT requires minimum Java version 6" >&2
      exit 4
    fi
    SUITE_VARIABLES="${SUITE_VARIABLES} -variable SYSTEM_JAVA_VERSION=${JAVA_VERSION}"
  fi
fi

# determine QF-Test executable
type -a ${EXEC} >/dev/null 2>&1
if [[ $? -ne 0 ]]
then
  echo "WARNING: QF-Test executable '${EXEC}' not found"
  echo "         Searching for QF-Test executable in HOME directory..."
  if [[ -x "${HOME}/bin/qftest" ]]
  then
    EXEC="${HOME}/bin/qftest"
  else
    echo "ERROR: No executable for QF-Test in \$HOME/bin" >&2
    exit 5
  fi
fi

# execute test suite with compiled parameters
echo "${LINE}"
echo "Identifying test suite..."
#ident "${TESTSUITE}"
grep -F '@(#)' "${TESTSUITE}"
echo

echo "JAVA_OPTIONS=${JAVA_OPTIONS}"

trap "terminateTestRun" 1 2 3 15
(
set -x
${EXEC} ${OPTIONS} ${TESTS} ${SUITE_VARIABLES} ${TESTSUITE}
RETVAL=$?
set +x
echo "${LINE}"

# in case of licence unavailability wait and retry
if [[ ${RETVAL} -eq -3 || ${RETVAL} -eq 253 ]]
then
  COUNT=0
  LIMIT=120
  while [[ ${RETVAL} -eq -3 || ${RETVAL} -eq 253 && ${COUNT} -lt ${LIMIT} ]]
  do
    echo "Waiting for licence..."
    sleep 30
    # execute test suite with compiled parameters
    ${EXEC} ${OPTIONS} ${TESTS} ${SUITE_VARIABLES} ${TESTSUITE}
    RETVAL=$?
    COUNT=$((${COUNT} + 1))
  done
fi
exit ${RETVAL}
) &
wait %1
RETVAL=$?
if [[ ${RETVAL} -ge 128 ]]
then
  RETVAL=$((${RETVAL} - 256))
fi
echo "QF-Test exit code: ${RETVAL}"
trap - 1 2 3 15

# provide readable output for exit code
if [[ ${RETVAL} -eq 0 ]]
then
  echo "No exceptions, errors or warnings occurred in the test run"
elif [[ ${RETVAL} -eq 1 ]]
then
  echo "Warnings occurred in the test run, but no exceptions or errors"
elif [[ ${RETVAL} -eq 2 ]]
then
  echo "Errors occurred in the test run, but no exceptions"
elif [[ ${RETVAL} -eq 3 ]]
then
  echo "Exceptions occurred in the test run"
elif [[ ${RETVAL} -eq -3 ]]
then
  echo "No QF-Test licence available"
else
  echo "See QF-Test documentation for return values: http://www.qfs.de/qftest/manual/en/tech_execution.html#sec_exitcodes"
fi

# required for batch mode and special NRI environment handling
HTMLREP=$(ls -1t ${LOGDIR}/*/report.html 2>/dev/null | head -1)

# add index link if report was created in batch mode
if [[ ${BATCHMODE} && -n "${HTMLREP}" ]]
then
  NEWREPDIR=$(dirname "${HTMLREP}")
  echo "Creating link index.html -> report.html..."
  if [[ ${WINDOWS} ]]
  then
    pushd "${NEWREPDIR}" && cp report.html index.html && popd
  else
    pushd "${NEWREPDIR}" && ln -fs report.html index.html && popd
  fi
fi

# additional handling for NRI environment
if [[ "${LOCATION}" == "NRI" ]]
then
  NRIRESULT="FAILED."
  if [[ -n "${HTMLREP}" ]]
  then
    #ln -sf ${HTMLREP} ./report.html
    echo '<html><head><meta http-equiv="Refresh" content="0; url='${HTMLREP}'" /></head><body><br />If you are not redirected automatically: <a href="'${HTMLREP}'">click here to open report</a></body></html>' >REPORT.html
  fi

  if [[ ${RETVAL} -eq 0 || ${RETVAL} -eq 1 ]]
  then
    NRIRESULT="PASSED."
  fi
  echo "${NRIRESULT}"

  echo "Copying work directory to test directory..."
  cp -r "${WORKDIR}" .
  echo "Done."
fi

exit ${RETVAL}
