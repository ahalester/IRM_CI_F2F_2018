#!/bin/bash
#*******************************************************************************
# ALMA - Atacama Large Millimeter Array
# Copyright (c) ESO - European Southern Observatory, 2012
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

ERRCOUNT=0

ARCHIVE_USER=alma_obops
ARCHIVE_PASSWD=alma_obops\$dba
ARCHIVE_CONNECT="@ora12c2.hq.eso.org:1521/ALMA" 

usage()
{
    echo "Usage parameters:"
    echo "-u ARCHIVE_USER -p ARCHIVE_PASSWD -c ARCHIVE_CONNECT"
    echo " OR"
    echo "-no parameters for default database information"

}   # end of usage

if [ $# -gt 0 ]; then
    while [ "$1" != "" ]; do
      case $1 in
          -u | --user )         shift
                                ARCHIVE_USER=$1
                                ;;
          -p | --password )     shift
                                ARCHIVE_PASSWD=$1
                                ;;
          -c | --connect )      shift
                                ARCHIVE_CONNECT=$1
                                ;;
          -h | --help )         usage
                                exit
                                ;;
          * )
                                usage
                                exit 1
          esac
          shift
    done
else
    echo "using default enviromnent:"
    echo ${ARCHIVE_USER}/"*******"${ARCHIVE_CONNECT}


fi

#ROLE_MASTER_USER=89
TOKEN_MASTER_USER="USER."

#ROLE_OBOPS_SWDEV=100
TOKEN_OBOPS_SWDEV="ADVANCED."

#ROLE_OBOPS_PHT=97
TOKEN_OBOPS_PHT="PHT."

TOKEN_INACTIVE="INACTIVE."

# md5hash is used to translate a clear text password
# echo -n Qwerty123 | md5sum
MD5HASHNEW="2af9b1ba42dc5eb01743e6b3759b6e4b"
# the following password was petrtest
MD5HASH="c6b80ec602b4b6280f2a96d42edb8f57"

ARC_EU=eu
ARC_EA=ea
ARC_NA=na

EXEC_EU=eu
EXEC_EA=ea
EXEC_NA=na
EXEC_TW=ea/na
EXEC_CL=cl
EXEC_OTHER=other

TEST_MAIL_DOMAIN="test.eso.org"

#=====================================================================================


function runSQLCommand () {
  COMMAND="${1}"
  #echo "COMMAND=${COMMAND}"
  OUTPUT=$(sqlplus ${ARCHIVE_USER}/${ARCHIVE_PASSWD}${ARCHIVE_CONNECT} <<+++
WHENEVER OSERROR EXIT
WHENEVER SQLERROR EXIT SQL.SQLCODE
${COMMAND}
select 'Command executed successfully' from DUAL;
+++
)
CHECK=$(echo "${OUTPUT}" | fgrep 'COMMANDEXECUTEDSUCCESSFULLY')
if [[ -z "${CHECK}" ]]
then
  echo -e "\nERROR in SQL command:\n>>>>>>>>>>>>>>>>>>>>>>>\n${OUTPUT}\n<<<<<<<<<<<<<<<<<<<<<<<\n" >&2
  RETVAL=1
else
  RETVAL=0
  echo "${OUTPUT}" | sed -e '1,/Connected to/d' -e '/COMMANDEXECUTEDSUCCESSFULLY/,$d' -e '/Oracle Database.*Release/d' -e 's#SQL> ##g' -e '/^$/d'
fi
  return ${RETVAL}
}

function getRoleID () {
  APP="${1}"
  ROLE="${2}"
  RESULT=$(runSQLCommand "select ROLE_NO, APPLICATION, NAME from ROLE where APPLICATION = '${APP}' and NAME = '${ROLE}';")
  RETVAL=$?
  if [[ ${RETVAL} -eq 0 ]]
  then
    if [[ -z "${RESULT}" ]]
    then
      echo "ERROR: Role not found: ${APP}, ${ROLE}" >&2
    else
      echo "${RESULT}" | egrep "${APP}.*${ROLE}" | awk '{print $1}'
    fi
  else
    echo "ERROR: Role not found: ${APP}, ${ROLE}" >&2
  fi
}

function getInstID () {
  INSTEXEC="${1}"
  INSTNAME="${2}"
  COUNTRY="${3}"
  #RESULT=$(runSQLCommand "select INST_NO, NAME1, CITY, STATE, EXECUTIVE from INSTITUTION where NAME1 LIKE '${INSTNAME}' and EXECUTIVE = '${INSTEXEC}' and CITY = '${CITY}' and COUNTRY_ID = (select COUNTRY_ID from COUNTRY where COUNTRY_NAME = '${COUNTRY}');")
  RESULT=$(runSQLCommand "select INST_NO from INSTITUTION where ( NAME1 LIKE '${INSTNAME}%' or NAME2 LIKE '${INSTNAME}%' or NAME3 LIKE '${INSTNAME}%' ) and EXECUTIVE = '${INSTEXEC}' and COUNTRY_ID = (select COUNTRY_ID from COUNTRY where COUNTRY_NAME LIKE '%${COUNTRY}%') and IGNORED = 'F';")
  RETVAL=$?
  if [[ ${RETVAL} -eq 0 ]]
  then
    RESULT=$(echo "${RESULT}" | egrep "[0-9]+" | sed -e "s#[ \t]##g" | head -1)
    if [[ -z "${RESULT}" ]]
    then
      echo "ERROR: Institution not found: ${INSTNAME}, ${COUNTRY} (${INSTEXEC})" >&2
      RETVAL=99
    else
      echo "${RESULT}"
    fi
  else
    echo "ERROR: Institution not found: ${INSTNAME}, ${COUNTRY} (${INSTEXEC})" >&2
  fi
  #echo "${INSTNAME}, ${COUNTRY} (${INSTEXEC}) =>" $(echo "${RESULT}" | egrep "[0-9]+" | sed -e "s# ##g" | head -1) >&2
  return ${RETVAL}
}

function execWithCheck () {
  COMMAND=${*}
  eval ${COMMAND}
  RETVAL=${?}
  if [[ ${RETVAL} -ne 0 ]]
  then
    ERRCOUNT=$((${ERRCOUNT}+1))
  fi
  return ${RETVAL}
}

function getFirstName () {
  FULLNAME="${1}"
  echo "${FULLNAME}" | cut -d "." -f 1
}

function getLastName () {
  FULLNAME="${1}"
  echo "${FULLNAME}" | cut -d "." -f 2
}

function getAlmaID () {
  FULLNAME="${1}"
  #echo "${FULLNAME}" | tr "A-Z." "a-z_"
  echo "${FULLNAME}"
}

#===============================================================================

echo
echo "Defining institutions..."

#TEST=$(execWithCheck 'getInstID "xx" "Unknown Institute" "Neverwhere"')
#echo "TEST=${TEST}"

INST_CZ1=$(execWithCheck 'getInstID "${EXEC_EU}" "Prague Czech Technical University" "Czech Republic"')
#INST_CZ1=$(execWithCheck 'getInstID "${EXEC_EU}" "Astronomical Institute" "Czech Republic"')
echo "INST_CZ1=${INST_CZ1}"

INST_CZ2=$(execWithCheck 'getInstID "${EXEC_EU}" "Masaryk University" "Czech Republic"')
echo "INST_CZ2=${INST_CZ2}"

INST_IRL=$(execWithCheck 'getInstID "${EXEC_OTHER}" "Dublin, University of" "Ireland"')
#INST_IRL=$(execWithCheck 'getInstID "${EXEC_OTHER}" "National University of Ireland" "Ireland"')
echo "INST_IRL=${INST_IRL}"

INST_UK=$(execWithCheck 'getInstID "${EXEC_EU}" "SKA Organisation" "United Kingdom"')
#INST_UK=$(execWithCheck 'getInstID "${EXEC_EU}" "UK Astronomy Technology Centre" "United Kingdom"')
echo "INST_UK=${INST_UK}"

INST_JP1=$(execWithCheck 'getInstID "${EXEC_EA}" "National Astronomical Observatory of Japan" "Japan"')
echo "INST_JP1=${INST_JP1}"
INST_JP2=$(execWithCheck 'getInstID "${EXEC_EA}" "National Institute for Fusion Science" "Japan"')
echo "INST_JP2=${INST_JP2}"
INST_JP3=$(execWithCheck 'getInstID "${EXEC_EA}" "Japan Aerospace Exploration Agency" "Japan"')
echo "INST_JP3=${INST_JP3}"

INST_CDN=$(execWithCheck 'getInstID "${EXEC_NA}" "Athabasca University" "Canada"')
#INST_CDN=$(execWithCheck 'getInstID "${EXEC_NA}" "Devon Astronomical Observatory" "Canada"')
echo "INST_CDN=${INST_CDN}"

INST_USA=$(execWithCheck 'getInstID "${EXEC_NA}" "Florida, University of" "United States"')
echo "INST_USA=${INST_USA}"

INST_TW1=$(execWithCheck 'getInstID "${EXEC_TW}" "National Taiwan University" "Taiwan"')
echo "INST_TW1=${INST_TW1}"
INST_TW2=$(execWithCheck 'getInstID "${EXEC_TW}" "National Cheng-Kung University" "Taiwan"')
echo "INST_TW2=${INST_TW2}"

INST_CL1=$(execWithCheck 'getInstID "${EXEC_CL}" "Chile, University of" "Chile"')
echo "INST_CL1=${INST_CL1}"
INST_CL2=$(execWithCheck 'getInstID "${EXEC_CL}" "Universidad Diego Portales" "Chile"')
#INST_CL2=$(execWithCheck 'getInstID "${EXEC_CL}" "Institute of Astronomy" "Chile"')
echo "INST_CL2=${INST_CL2}"
INST_CL3=$(execWithCheck 'getInstID "${EXEC_CL}" "Metropolitan University" "Chile"')
echo "INST_CL3=${INST_CL3}"

INST_RSA=$(execWithCheck 'getInstID "${EXEC_OTHER}" "South African Astronomical Observatory" "South Africa"')
echo "INST_RSA=${INST_RSA}"
#INST_AU=$(execWithCheck 'getInstID "${EXEC_OTHER}" "University of Western Australia" "Australia"')
INST_AU=$(execWithCheck 'getInstID "${EXEC_OTHER}" "Australia National University" "Australia"')
echo "INST_AU=${INST_AU}"
INST_IND=$(execWithCheck 'getInstID "${EXEC_OTHER}" "Indian Institute of Astrophysics" "India"')
echo "INST_IND=${INST_IND}"
INST_IL=$(execWithCheck 'getInstID "${EXEC_OTHER}" "Tel Aviv University" "Israel"')
echo "INST_IL=${INST_IL}"

#getInstID "" "" ""

echo
echo "Defining roles..."

# USER
ROLE_MASTER_USER=$(execWithCheck 'getRoleID MASTER USER')
echo "ROLE_MASTER_USER=${ROLE_MASTER_USER}"

# ADVANCED
ROLE_OBOPS_SWDEV=$(execWithCheck 'getRoleID OBSPREP SOFTWARE_DEVELOPER')
echo "ROLE_OBOPS_SWDEV=${ROLE_OBOPS_SWDEV}"

# PHT
ROLE_OBOPS_PHT=$(execWithCheck 'getRoleID OBOPS PHT')
echo "ROLE_OBOPS_PHT=${ROLE_OBOPS_PHT}"

#getFirstName Jaromir.Nohavica
#getLastName Jaromir.Nohavica
#getAlmaID Jaromir.Nohavica


echo
echo "Defining users..."

USER_CZ1=jnohavic
FULLNAME_CZ1=Jaromir.Nohavica
USER_CZ2=lfilipova
FULLNAME_CZ2=Lenka.Filipova
USER_CZ3=kgott
FULLNAME_CZ3=Karel.Gott

USER_IRL1=skeane
FULLNAME_IRL1=Sean.Keane
USER_IRL2=acorr
FULLNAME_IRL2=Andrea.Corr

USER_UK=smcdonald
FULLNAME_UK=Shelagh.McDonald

USER_JP1=uhikaru
FULLNAME_JP1=Utada.Hikaru
USER_JP2=hayumi
FULLNAME_JP2=Hamasaki.Ayumi
USER_JP3=hyakushi
FULLNAME_JP3=Hiroko.Yakushimaru

USER_CDN1=jhall
FULLNAME_CDN1=Jim.Hall
USER_CDN2=lmckennitt
FULLNAME_CDN2=Loreena.McKennitt

USER_USA=jcash
FULLNAME_USA=Johnny.Cash

USER_TW1=thsiao
FULLNAME_TW1=Tyzen.Hsiao
USER_TW2=cyinggit
FULLNAME_TW2=Chen.Ying-Git
USER_TW3=jchang
FULLNAME_TW3=Jing.Chang

USER_CL1=ksiegel
FULLNAME_CL1=Kamal.Siegel
USER_CL2=surbina
FULLNAME_CL2=Silvia.Urbina
USER_CL3=dtaub
FULLNAME_CL3=David.Rosenmann-Taub
USER_CL4=vparra
FULLNAME_CL4=Violeta.Parra

USER_RSA=bdlamini
FULLNAME_RSA=Bonginkosi.Dlamini
USER_AU=adobson
FULLNAME_AU=Abby.Dobson
USER_IND=rshankar
FULLNAME_IND=Ravi.Shankar
USER_IL=yravitz
FULLNAME_IL=Yehudit.Ravitz

USER_PHT=ph1mpht
FIRSTNAME_PHT=Ph1m
LASTNAME_PHT=Pht


echo
if [[ ${ERRCOUNT} -gt 0 ]]
then
  echo "ERROR: Exit due to previous error(s)" >&2
  echo "       Total number of errors: ${ERRCOUNT}"
  exit 2
else
  echo "Institutions and roles successfully defined"
fi
echo
