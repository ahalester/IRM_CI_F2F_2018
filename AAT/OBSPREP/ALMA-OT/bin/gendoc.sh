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

#QFTBASE=/software/qftest
QFTBASE=$(getQFTestBase)
if [[ ! -d "${QFTBASE}" ]]
then
  echo "QF-Test base directory '${QFTBASE}' not mounted!" >&2
  exit 1
fi

#EXEC=qftest
EXEC=${QFTBASE}/$(getQFTestVersion)/bin/qftest
# EXEC=${QFTBASE}/qftest-4.0.11/bin/qftest
#EXEC=${QFTBASE}/qftest-3.5.4/bin/qftest

PKGDOCDIR=../doc/pkgdoc/
TESTDOCDIR=../doc/testdoc/
OPTIONS="-batch -gendoc -noconsole -nomessagewindow -pkgdoc \"${PKGDOCDIR}\" -testdoc \"${TESTDOCDIR}\""

type -a ${EXEC} >/dev/null 2>&1
if [[ $? -ne 0 ]]
then
  echo "WARNING: QF-Test executable 'qftest' is not found in PATH"
  if [[ -x ${HOME}/bin/qftest ]]
  then
    EXEC=${HOME}/bin/qftest
  else
    echo "ERROR: No executable for QF-Test in \$HOME/bin"
    exit 1
  fi
fi

eval ${EXEC} ${OPTIONS} ../test/ALMA-OT_Test_Suite.qft
