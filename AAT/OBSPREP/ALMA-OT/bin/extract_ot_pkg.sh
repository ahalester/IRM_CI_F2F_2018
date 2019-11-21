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
# "@(#) $Id: extract_ot_pkg.sh 195602 2013-09-26 17:09:02Z pkratzer $"
# 
# Helper script to extract the ALMA-OT from a package
# built with gen_ot_pkg.sh (using OBSPREP/UserInstall).
# 

#set -x

LINE="-----------------------------------------------------------------------"
DEFAULT_PKG_NAME="AlmaOT.tgz"

MYNAME=$(basename "${0}")
DIRNAME=$(dirname "${0}")
if [[ "${DIRNAME}" = "." ]]
then
  DIRNAME=$(pwd)
else
  BINDIR="${DIRNAME}"
fi
MODROOT=$(dirname "${BINDIR}")
WORKDIR="${MODROOT}/work"

echo "Sourcing common settings and functions..."
source "${BINDIR}/common"
echo "OK"

TIMESTAMP=$(getTimeStamp)

# default setting
SUBMISSION_SERVICE=$(getSubmissionService)

# check for command-line option (submission service)
if [[ ${#} -gt 0 ]]
then
  if [[ "${1}" == "-s" ]]
  then
    shift
    if [[ ${#} -eq 0 ]]
    then
      echo "ERROR: Missing parameter for option -s" >&2
      exit 1
    else
      SUBMISSION_SERVICE="${1}"
      shift
    fi
  fi
fi

# check for command-line parameter (ALMA-OT package file)
if [[ ${#} -gt 0 ]]
then
  ALMA_OT_PKG="${1}"
else
  ALMA_OT_PKG="$(pwd)/${DEFAULT_PKG_NAME}"
fi
echo "${LINE}"
echo "Using package: '${ALMA_OT_PKG}'"


# use working directory from test module
if [[ ! -d "${WORKDIR}" ]]
then
  mkdir -p "${WORKDIR}"
  if [[ ! -d "${WORKDIR}" ]]
  then
    echo "ERROR: Working directory '${WORKDIR}' not found." >&2
    exit 1
  fi
fi

cd "${WORKDIR}"
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Working directory '${WORKDIR}' not accessible." >&2
  exit 2
fi

# 
INSTDIR="${WORKDIR}/ot_inst"
if [[ -d "${INSTDIR}" ]]
then
  mv "${INSTDIR}" "${INSTDIR}.${TIMESTAMP}"
  if [[ -d "${INSTDIR}" ]]
  then
    echo "ERROR: Cannot rename old ALMA-OT installation directory '${INSTDIR}'." >&2
    exit 3
  fi
fi

mkdir -p "${INSTDIR}"
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Cannot create ALMA-OT installation directory '${INSTDIR}'." >&2
  exit 4
fi

cd "${INSTDIR}"
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Cannot access ALMA-OT installation directory '${INSTDIR}'." >&2
  exit 5
fi

if [[ ! -f "${ALMA_OT_PKG}" ]]
then
  echo "ERROR: ALMA-OT installation package '${ALMA_OT_PKG}' not found." >&2
  exit 6
fi

# extract standard package if nested within another package
tar tvf "${ALMA_OT_PKG}" "./${DEFAULT_PKG_NAME}" 2>/dev/null
RETVAL=${?}
if [[ ${RETVAL} -eq 0 ]]
then
  echo "ALMA-OT installation package '${DEFAULT_PKG_NAME}' nested within '${ALMA_OT_PKG}'."
  echo "Starting extraction..."
  tar xvzf "${ALMA_OT_PKG}" "./${DEFAULT_PKG_NAME}" ./SVN_TAG ./BUILD_TAG
  RETVAL=${?}
  if [[ ${RETVAL} -ne 0 ]]
  then
    echo "ERROR: Extracting ALMA-OT installation package '${DEFAULT_PKG_NAME}' from '${ALMA_OT_PKG}' failed." >&2
    exit 7
  else
    ALMA_OT_PKG="${DEFAULT_PKG_NAME}"
    if [[ ! -f "${ALMA_OT_PKG}" ]]
    then
      echo "ERROR: ALMA-OT installation package '${ALMA_OT_PKG}' not found." >&2
      exit 8
    fi
  fi
fi

# extract default ALMA-OT package from tarball
echo "Extracting default ALMA-OT package from tarball..."
tar xvzf "${ALMA_OT_PKG}"
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Extracting default ALMA-OT installation package failed." >&2
  exit 9
fi

# package directory should contain subdirectory "setup"
SETUPDIR=$(ls -1d ${INSTDIR}/ALMAOT-*/setup)
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Cannot access setup directory '${SETUPDIR}'." >&2
  exit 10
fi

# patch files for client startup with test submission service
echo "Patching ALMA-OT setup..." 
"${BINDIR}/patch_setup.sh" -d "${SETUPDIR}" "${SUBMISSION_SERVICE}"

# install ALMA-OT startup commands
cd "${SETUPDIR}"
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Cannot access setup directory '${SETUPDIR}'." >&2
  exit 11
else
  echo "Installing ALMA-OT scripts..." 
  ./Setup-Linux.sh
  RETVAL=${?}
  if [[ ${RETVAL} -ne 0 ]]
  then
    echo "ERROR: Installation failed." >&2
    exit 12
  else
    # copy MS Windows batch files for all telescope configurations
    # IMPORTANT: adaptations required for new telescope types/cycles
    # files are renamed for uniform handling between Linux and MS Windows
    cp ALMA-OT.cmd ../ALMA-OT-Cycle2.cmd
    cp ALMA-OT-ES.cmd ../ALMA-OT-ESALMA.cmd
    cp ALMA-OT-CSV.cmd ../ALMA-OT-CSVALMA.cmd

    # create Linux scripts for all telescope configurations
    cd ..
    SOURCE="ALMA-OT.sh"
    if [[ ! -f "${SOURCE}" ]]
    then
      echo "ERROR: Start script '${SOURCE}' not found." >&2
    else
      TEL_LIST=$(getTelescopeTypes)
      for TEL in ${TEL_LIST}
      do
        echo "Creating script for telescope type ${TEL}..."
        NEWNAME=$(echo "${SOURCE}" | sed -e "s#\.sh#-${TEL}.sh#")
        cat "${SOURCE}" | \
          sed -e "s# -DTelescope=[a-zA-Z0-9]* # -DTelescope=${TEL} #g" >"${NEWNAME}"
        chmod a+rx "${NEWNAME}"
      done
    fi
  fi
fi

SETUPPARENT=$(basename "${SETUPDIR}")
cd "${SETUPPARENT}"
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Cannot access setup parent directory '${SETUPPARENT}'." >&2
  exit 13
else
  # copy tag and files
  if [[ -f ../SVN_TAG ]]
  then
    echo "Copying SVN_TAG file..."
    cp -f ../SVN_TAG .
  fi
  if [[ -f ../BUILD_TAG ]]
  then
    echo "Copying BUILD_TAG file..."
    cp -f ../BUILD_TAG .
  fi
fi

echo "Extraction complete."
getNiceTimeStamp
echo "${LINE}"

