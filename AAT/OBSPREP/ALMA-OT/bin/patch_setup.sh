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
# "@(#) $Id: patch_setup.sh 195291 2013-09-20 11:55:16Z pkratzer $"
# 
# Helper script to patch the ALMA-OT startup scripts with the
# test submission service.
# 

DIRNAME=$(dirname "${0}")
MYNAME=$(basename "${0}")
if [[ "${DIRNAME}" == "." ]]
then
  BINDIR=$(pwd)
else
  BINDIR="${DIRNAME}"
fi
MODROOT=$(dirname "${BINDIR}")

echo "Sourcing common settings and functions..."
source "${BINDIR}/common"
echo "OK"


if [[ ${#} -gt 0 ]]
then
  if [[ "${1}" == "-d" ]]
  then
    shift
    if [[ ${#} -gt 0 ]]
    then
      SETUPDIR="${1}"
      shift
    else
      echo "ERROR: Missing parameter for option -d" >&2
      exit 1
    fi
  fi
fi

if [[ ${#} -gt 0 ]]
then
  SUBMISSION_SERVICE="${1}"
else
  SUBMISSION_SERVICE=$(getSubmissionService)
fi
echo "Submission service: '${SUBMISSION_SERVICE}'"

# default setting
if [[ -z "${SETUPDIR}" ]]
then
  SETUPDIR="${MODROOT}/work/ot_inst/setup"
fi

cd "${SETUPDIR}"
RETVAL=${?}
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Cannot access setup directory '${SETUPDIR}'." >&2
else
  FILES=$(grep -F -e '-Dot.submission.host' ALMA-OT* | cut -d: -f1)
  for ONE in ${FILES}
  do
    echo "Processing file '${ONE}'..."
    ORIGFILE="${ONE}.orig"
    mv "${ONE}" "${ORIGFILE}"
    cat "${ORIGFILE}" | sed -e "s#-Dot.submission.host=\"[^\"]*\"#-Dot.submission.host=\"${SUBMISSION_SERVICE}\"#g" >"${ONE}"
    RETVAL=${?}
    if [[ ${RETVAL} -ne 0 ]]
    then
      echo "ERROR: Adaptation failed for file '${ONE}'." >&2
      mv -f "${ORIGFILE}" "${ONE}"
    else
      rm -f "${ORIGFILE}"
      chmod a+rx "${ONE}"
    fi
  done
fi

