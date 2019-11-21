#!/bin/bash

#*******************************************************************************
# ALMA - Atacama Large Millimeter Array
# Copyright (c) ESO - European Southern Observatory, 2011
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
FILENAME="ALMA-OT_Test_Suite.qft"

cd ../test
RETVAL=$?
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Cannot access config directory"
  exit 1
fi

if [[ ! -d ../bak ]]
then
  echo "ERROR: Cannot access bak directory"
  exit 1
fi

BASENAME=$(basename "${FILENAME}" .qft)
#echo ${BASENAME}
BACKUP_FILENAME="${BASENAME}_"$(date +"%Y%m%d_%H%M%S")".qft"
echo "Creating backup file '${BACKUP_FILENAME}' in directory 'bak'..."

cp -f "${FILENAME}" "../bak/${BACKUP_FILENAME}"
RETVAL=$?
if [[ ${RETVAL} -ne 0 ]]
then
  echo "ERROR: Cannot create backup file"
  exit 1
fi


