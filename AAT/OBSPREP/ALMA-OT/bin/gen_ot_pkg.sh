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
# "@(#) $Id: gen_ot_pkg.sh 197359 2013-11-07 12:09:35Z pkratzer $"
#

MYNAME=$(basename "${0}")
DIRNAME=$(dirname "${0}")
if [[ "${DIRNAME}" == "." ]]
then
  BINDIR=$(pwd)
else
  BINDIR="${DIRNAME}"
fi
MODROOT=$(dirname "${BINDIR}")
LOGDIR="${MODROOT}/log"

echo "${MYNAME}: Sourcing common settings and functions..."
source "${BINDIR}/common"
echo "OK"

#set -x

TIMESTAMP=$(getTimeStamp)
NICETIMESTAMP=$(getNiceTimeStamp)
LOGFILE="${LOGDIR}/BUILD_LOG_${TIMESTAMP}.log"

{

# static settings
BUILDDIRBASE="BUILD_ALMAOT"

SVNREPO=$(getSVNRepository)
UI_LOCAL="UserInstall"
SVN_USERINSTALL="${SVNREPO}/trunk/OBSPREP/${UI_LOCAL}"

# Subversion branch to build
SVN_TAG_FILE="svn_tag"
#********************************************
#SVN_TAG="branches/ALMA-10_1_0-B"
# default setting (if no other branch is selected by command-line option)
SVN_TAG="branches/ALMA-RELEASE-B"
ACSVERSION="12.1"
#********************************************

SUBMISSION_SERVICE=$(getSubmissionService)

# evaluate command-line options
RUNTEST=
DEBUG=
ADD_OPTIONS=
while [[ ${#} -gt 0 ]]
do
  # Limited fast mode for debugging
  if [[ "${1}" == "--debug" ]]
  then
    shift
    DEBUG=true
    ADD_OPTIONS="${ADD_OPTIONS} --debug"
    echo "${MYNAME}: WARNING! DEBUGGING MODE ACTIVE (NO RETRIEVE AND BUILD)"

  # ALMA-OT automated test execution
  elif [[ "${1}" == "-t" ]]
  then
    shift
    RUNTEST=true
    echo "Option selected: run test after build"

  # branch in repository
  elif [[ "${1}" == "-b" ]]
  then
    shift
    if [[ ${#} -eq 0 ]]
    then
      echo "ERROR: Missing parameter 'branch name' for option -b" >&2
      exit 1
    else
      SVN_TAG="branches/${1}"
      shift
    fi

  # submission service selection
  elif [[ "${1}" == "-s" ]]
  then
    shift
    if [[ ${#} -eq 0 ]]
    then
      echo "ERROR: Missing parameter 'submission service' for option -s" >&2
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
    if [[ ${#} -eq 0 ]]
    then
      echo "ERROR: Missing parameter 'Java command' for option -j" >&2
      exit 1
    else
      JAVA_COMMAND="${1}"
      if [[ ! -x ${JAVA_COMMAND} ]]
      then
        echo "ERROR: '${JAVA_COMMAND}' not found or missing execute permissions" >&2
        exit 1
      else
        ADD_OPTIONS="${ADD_OPTIONS} -j ${JAVA_COMMAND}"
      fi
      shift
    fi

  # ACS version selection
  elif [[ "${1}" == "-a" ]]
  then
    shift
    if [[ ${#} -eq 0 ]]
    then
      echo "ERROR: Missing parameter 'ACS version' for option -a" >&2
      exit 1
    else
      ACSVERSION="${1}"
      if [[ ! -f "/alma/ACS-${ACSVERSION}/ACSSW/config/.acs/.bash_profile.acs" ]]
      then
        echo "ERROR: bash profile for ACS version '${ACSVERSION}' not found:" >&2
        echo "       /alma/ACS-${ACSVERSION}/ACSSW/config/.acs/.bash_profile.acs" >&2
        exit 1
      fi
      shift
    fi

  fi
done

# set up option-dependent parameters

SHORT_TAG="$(basename ${SVN_TAG})"
VERSION_INFO_FILE="version_info"
VERSION_INFO="STANDALONE_${SHORT_TAG}_${TIMESTAMP}"
# add version info to build directory name
if [[ ${DEBUG} ]]
then
  BUILDDIR="${BUILDDIRBASE}_DEBUG"
else
  BUILDDIR="${BUILDDIRBASE}_${SHORT_TAG}_${TIMESTAMP}"
fi
# content for ALMA-OT build system's version info
VP_FILE="version.properties"
VP_USER="STANDALONE ${SHORT_TAG} ${NICETIMESTAMP}"
VP_ALMA="${SHORT_TAG}"
VP_PATCH=""      # remains empty to avoid problems with version enforcement

# dynamic settings

STAGE=$(getStageDir)
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: STAGE directory not defined." >&2
  exit 1
fi

echo "TARGETDIR=${STAGE}/${BUILDDIR}"

cd "${STAGE}"
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Cannot access directory '${STAGE}'." >&2
  exit 2
fi

if [[ -e "${BUILDDIR}" ]]
then
  if [[ ${DEBUG} ]]
  then
    echo "WARNING: DEBUG MODE - REUSING EXISTING DIRECTORY"
  else
    mv "${BUILDDIR}" "${BUILDDIR}.${TIMESTAMP}"
  fi
fi

if [[ -e "${BUILDDIR}" && ! ${DEBUG} ]]
then
  echo "ERROR: Renaming old build directory failed." >&2
  exit 3
fi

if [[ ! ${DEBUG} ]]
then
  mkdir "${BUILDDIR}"
  RETVAL=${?}
  if [[ ${RETVAL} -ne 0 ]]
  then
    echo "ERROR: Cannot create new build directory '${BUILDDIR}'." >&2
    exit 4
  fi
fi

cd "${BUILDDIR}"
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Cannot access build directory '${BUILDDIR}'." >&2
  exit 5
fi

LOCAL=$(df -l . | sed -e "1d" | wc -l)
if [[ ${LOCAL} == 0 ]]
then
  echo "ERROR: Local (not NFS mounted) directory required for building the software." >&2
  exit 6
fi

FREE=$(df -m . | sed -e "1d" | awk '{ print $4 }')
if [[ ${FREE} -lt 8000 && ! ${DEBUG} ]]
then
  echo "ERROR: Insufficient disk space (less than ~8 GB) for complete build." >&2
  exit 7
fi

# check if specified branch exists in repository
svn info "${SVNREPO}/${SVN_TAG}"
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Branch '${SVN_TAG}' not found in repository '${SVNREPO}'." >&2
  exit 8
fi

# get installation module from repository
if [[ ! ${DEBUG} ]]
then
  svn checkout "${SVN_USERINSTALL}"
  RETVAL=${?}
  if [[ ${RETVAL} -ne 0 ]]
  then
    echo "ERROR: Checking out '${SVN_USERINSTALL}' failed." >&2
    exit 9
  fi
fi

pushd "${UI_LOCAL}/src/"
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Cannot access directory '${UI_LOCAL}/src/'." >&2
  exit 10
fi

# patch ACS version in build prologue
PROLOGUE="utilPrologue.sh"
TEMPFILE="${PROLOGUE}.patched"
if [[ ! -f "${PROLOGUE}" ]]
then
  echo "ERROR: Build prologue file '${PROLOGUE}' not found." >&2
  exit 11
else
  # make sure required ACS settings are available before patching the build profile
  if [[ ! -f "/alma/ACS-${ACSVERSION}/ACSSW/config/.acs/.bash_profile.acs" ]]
  then
    echo "ERROR: bash profile for ACS version '${ACSVERSION}' not found." >&2
    exit 12
  fi

  # patch build profile with selected ACS version
  echo "Patching prologue '${PROLOGUE}' to use ACS version ''${ACSVERSION}'..."
  cat "${PROLOGUE}" | \
    sed -e "s#^\. /alma/ACS-[0-9]*\.[0-9]*/ACSSW/config/\.acs/\.bash_profile\.acs#. /alma/ACS-${ACSVERSION}/ACSSW/config/.acs/.bash_profile.acs#" |
    cat > "${TEMPFILE}"
    if [[ ! -f "${TEMPFILE}" ]]
    then
      echo "ERROR: Patching build prologue file '${PROLOGUE}' failed." >&2
      exit 13
    else
      rm -f "${PROLOGUE}.orig"
      mv "${PROLOGUE}" "${PROLOGUE}.orig" && mv "${TEMPFILE}" "${PROLOGUE}"
      CHECK=$(fgrep "/alma/ACS-${ACSVERSION}/ACSSW/config/.acs/.bash_profile.acs" "${PROLOGUE}")
      if [[ -z "${CHECK}" ]]
      then
        echo "ERROR: Consistency check for ACS version 'ACS-${ACSVERSION}' in patched prologue file '${PROLOGUE}' failed:" >&2
        echo "       Found string: '$(fgrep ".bash_profile" "${PROLOGUE}")'" >&2
        exit 14
      fi
    fi
  echo "OK"
fi
popd

cd "${UI_LOCAL}/src/settings"
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Cannot access directory '${UI_LOCAL}/src/settings'." >&2
  exit 15
fi

# create version files
echo "${SVN_TAG}" >"${SVN_TAG_FILE}"
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Cannot create '${SVN_TAG_FILE}'." >&2
  exit 16
fi

echo "${VERSION_INFO}" >"${VERSION_INFO_FILE}"
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Cannot create '${VERSION_INFO_FILE}'." >&2
  exit 17
fi

echo "version.user = ${VP_USER}" >"${VP_FILE}"
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Cannot create '${VP_FILE}'." >&2
  exit 18
else
  echo "version.alma = ${VP_ALMA}" >>"${VP_FILE}"
  echo "version.patch = ${VP_PATCH}" >>"${VP_FILE}"
fi

CHECK=$(wc -l "${VP_FILE}" | cut -d " " -f 1)
if [[ ${CHECK} -ne 3 ]]
then
  echo "ERROR: Creating version file '${VP_FILE}' failed." >&2
  exit 19
fi

# change to src directory and start build
cd ..
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Cannot access src directory '${UI_LOCAL}/src'." >&2
  exit 20
fi

echo
echo "======================================================================================"
echo

STARTTIME=$(getSystemTime)

#if [[ ! ${DEBUG} ]]
#then
#  ACTIONS="retrieve compile tarball"
#else
#  ACTIONS="tarball"
#fi
if [[ ! ${DEBUG} ]]
then
  time make retrieve compile tarball 2>&1 | tee "BUILD_ALMAOT_${TIMESTAMP}.log"
  MAKE_RETVAL=${?}
  if [[ ${MAKE_RETVAL} -eq 0 ]]
  then
    if [[ ! -d ../out ]]
    then
      echo "ERROR: Directory 'out' missing." >&2
      MAKE_RETVAL=99
    fi
  fi
else
  echo "WARNING: Skipping make command in DEBUG mode."
  MAKE_RETVAL=0
fi
#time make ${ACTIONS} 2>&1 | tee "BUILD_ALMAOT_${TIMESTAMP}.log"

ENDTIME=$(getSystemTime)

BUILDTIME=$(getTimeDiff ${STARTTIME} ${ENDTIME})


echo
echo "======================================================================================"
echo

if [[ ${MAKE_RETVAL} -ne 0 ]]
then
  echo "ERROR: Build failed." >&2
else
  
  # change to target directory and collect results in one archive
  ALMA_OT_PKG=
  cd ../out
  RETVAL=${?}
  if [[ ${RETVAL} -ne 0 ]]
  then
    echo "ERROR: Cannot access target directory '${UI_LOCAL}/out'." >&2
  else
    echo "${SHORT_TAG}" >SVN_TAG
    echo "${VERSION_INFO}" >BUILD_TAG
    echo "Packaging complete result in one archive..."
    ALMA_OT_PKG="${STAGE}/${BUILDDIR}/ALMA-OT_${SHORT_TAG}_${TIMESTAMP}.tgz"
    tar cvzf "${ALMA_OT_PKG}" .
    RETVAL=${?}
    if [[ ${RETVAL} -eq 0 ]]
    then
      echo -e "Complete archive created:\n\t${ALMA_OT_PKG}"
    else
      echo "ERROR: Creating complete archive failed." >&2
    fi
  fi
  
  echo "Build complete."
  
  # test execution (if requested)
  if [[ ${RUNTEST} ]]
  then
    getNiceTimeStamp
  
    echo
    echo
    echo "##############################################################################"
    echo
    echo "Running tests..."
    echo
  
    STARTTIME=$(getSystemTime)
    "${BINDIR}/all_tests.sh" ${ADD_OPTIONS} -s "${SUBMISSION_SERVICE}" -p "${ALMA_OT_PKG}"
    ENDTIME=$(getSystemTime)
    TESTTIME=$(getTimeDiff ${STARTTIME} ${ENDTIME})
  
    echo
    echo "-----------------------------------------------------------------------------"
    echo
  else
    echo "No test run selected."
  fi
  
  echo "Total build time (h:min): ${BUILDTIME}"
  if [[ ${RUNTEST} ]]
  then
    echo "Total test time (h:min): ${TESTTIME}"
  fi
  
  echo
  echo "Checking disk space usage..."
  SIZE=$(du -ms "${STAGE}/${BUILDDIR}" | cut -f1)
  echo "Space used by build directory: ${SIZE} MB"
fi

getNiceTimeStamp
echo

} 2>&1 | tee "${LOGFILE}"

