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
# "@(#) $Id: list.sh 193112 2013-07-29 12:50:56Z pkratzer $"
#
# List all test sets and test cases in ALMA-OT GUI test suite
#

SUITE="../test/ALMA-OT_Test_Suite.qft"

if [[ ${1} == "-d" ]]
then
  #CMD="awk '{print \$2}' | sort > /tmp/$$.1"
  CMD="cat > /tmp/$$.0"
else
  CMD="cat"
fi

cat ${SUITE} | tr "\n" " " | sed -e "s#^\( *\)<#\n\1<#g" -e "s#>#>\n#g" | \
  sed -e "s#^         #++++#" \
      -e "s#^       #+++#" \
      -e "s#^     #++#" \
      -e "s#^   #+#" \
      -e "s#  *# #g" | \
  sed -e "s#^++++#      #" \
      -e "s#^+++#    #" \
      -e "s#^++#  #" \
      -e "s#^+##" |\
  fgrep -v 'disabled="true"' | \
  egrep "<TestSet|<TestCase" | \
  sed -e "s#<TestSet#TEST-SET#" \
      -e "s#<TestCase#TEST-CASE#" \
      -e "s# id=\"\([^\"]*\)\"#\t\1#" \
      -e "s# name=\"\([^\"]*\)\"#\t\"\1\"#" \
      -e "s# uid=\"[^\"]*\"##" \
      -e "s# inherit=\"[^\"]*\"##" \
      -e "s# condition=\"[^\"]*\"##" \
      -e "s#>\$##" | \
  eval ${CMD}

if [[ ${1} == "-d" ]]
then
  awk '{print $2}' /tmp/$$.0 | sort > /tmp/$$.1  
  cat /tmp/$$.1 | sort -u >/tmp/$$.2
  DUPLICATES=$(diff /tmp/$$.2 /tmp/$$.1 | egrep -v "[0-9]+a[0-9]+" | sed -e "s#^> ##")
  if [[ -n "${DUPLICATES}" ]]
  then
    LAST=
    for ID in ${DUPLICATES}
    do
      if [[ -n ${LAST} && ${ID} != ${LAST} ]]
      then
        echo
      fi
      egrep "	${ID}	" /tmp/$$.0
      LAST=${ID}
    done
  fi
  rm -f /tmp/$$.*
fi
