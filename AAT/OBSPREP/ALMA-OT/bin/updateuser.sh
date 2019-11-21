#!/bin/bash
#*******************************************************************************
# ALMA - Atacama Large Millimeter Array
# Copyright (c) ESO - European Southern Observatory, 2012, 2013
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


if [[ $(dirname ${0}) != "." ]]
then
  echo "Please call this script only from its local directory"
  exit 1
fi

source usersettings.sh

# Overwrite the executive definition for the creation of accounts because 
# starting from ALMA106 the executive should be null in the account table 
# while it should be defined in the institution table
EXEC_EU=
EXEC_EA=
EXEC_NA=
EXEC_TW=
EXEC_CL=
EXEC_OTHER=


#======================================================================================

echo
echo "Updating users in data base..."

#cat <<+++
sqlplus ${ARCHIVE_USER}/${ARCHIVE_PASSWD}${ARCHIVE_CONNECT} <<+++
WHENEVER OSERROR EXIT
WHENEVER SQLERROR EXIT SQL.SQLCODE

/* =============== Section 1 (EU) =============== */

/* standard users */

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_CZ1})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_CZ1})', LASTNAME = '$(getLastName ${FULLNAME_CZ1})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EU}', EMAIL = '$(getAlmaID ${USER_CZ1})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_EU}', ALIASES = null, INST_NO = ${INST_CZ1}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_CZ1})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CZ1})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CZ1})');

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_CZ2})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_CZ2})', LASTNAME = '$(getLastName ${FULLNAME_CZ2})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EU}', EMAIL = '$(getAlmaID ${USER_CZ2})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_EU}', ALIASES = null, INST_NO = ${INST_CZ2}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_CZ2})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CZ2})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CZ2})');

/* advanced users */

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_IRL1})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_IRL1})', LASTNAME = '$(getLastName ${FULLNAME_IRL1})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EU}', EMAIL = '$(getAlmaID ${USER_IRL1})@${TOKEN_OBOPS_SWDEV}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_EU}', ALIASES = null, INST_NO = ${INST_IRL}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_IRL1})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_IRL1})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_IRL1})');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_OBOPS_SWDEV}, '$(getAlmaID ${USER_IRL1})');

/* PHT */

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_UK})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_UK})', LASTNAME = '$(getLastName ${FULLNAME_UK})',', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EU}', EMAIL = '$(getAlmaID ${USER_UK})@${TOKEN_OBOPS_SWDEV}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_EU}', ALIASES = null, INST_NO = ${INST_UK}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_UK})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_UK})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_UK})');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_OBOPS_PHT}, '$(getAlmaID ${USER_UK})');




/* =============== Section 2 (EA) =============== */

/* standard users */

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_JP1})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_JP1})', LASTNAME = '$(getLastName ${FULLNAME_JP1})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EA}', EMAIL = '$(getAlmaID ${USER_JP1})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_EA}', ALIASES = null, INST_NO = ${INST_JP1}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_JP1})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_JP1})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_JP1})');

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_JP2})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_JP2})', LASTNAME = '$(getLastName ${FULLNAME_JP2})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EA}', EMAIL = '$(getAlmaID ${USER_JP2})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_EA}', ALIASES = null, INST_NO = ${INST_JP2}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_JP2})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_JP2})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_JP2})');

/* advanced users */

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_JP3})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_JP3})', LASTNAME = '$(getLastName ${FULLNAME_JP3})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EA}', EMAIL = '$(getAlmaID ${USER_JP3})@${TOKEN_OBOPS_SWDEV}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_EA}', ALIASES = null, INST_NO = ${INST_JP3}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_JP3})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_JP3})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_JP3})');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_OBOPS_SWDEV}, '$(getAlmaID ${USER_JP3})');



/* =============== Section 3 (NA) =============== */

/* standard users */

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_CDN1})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_CDN1})', LASTNAME = '$(getLastName ${FULLNAME_CDN1})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_NA}', EMAIL = '$(getAlmaID ${USER_CDN1})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_NA}', ALIASES = null, INST_NO = ${INST_CDN}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_CDN1})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CDN1})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CDN1})');

update ACCOUNT set ACCOUNT_ID ='$(getAlmaID ${USER_USA})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_USA})', LASTNAME = '$(getLastName ${FULLNAME_USA})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_NA}', EMAIL = '$(getAlmaID ${USER_USA})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_NA}', ALIASES = null, INST_NO = ${INST_USA}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_USA})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_USA})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_USA})');

/* advanced users */

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_CDN2})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_CDN2})', LASTNAME = '$(getLastName ${FULLNAME_CDN2})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_NA}', EMAIL = '$(getAlmaID ${USER_CDN2})@${TOKEN_OBOPS_SWDEV}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_NA}', ALIASES = null, INST_NO = ${INST_CDN}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_CDN2})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CDN2})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CDN2})');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_OBOPS_SWDEV}, '$(getAlmaID ${USER_CDN2})');



/* =============== Section 4 (TW) =============== */

/* standard users */

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_TW1})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_TW1})', LASTNAME = '$(getLastName ${FULLNAME_TW1})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EA}', EMAIL = '$(getAlmaID ${USER_TW1})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_TW}', ALIASES = null, INST_NO = ${INST_TW1}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_TW1})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_TW1})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_TW1})');

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_TW2})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_TW2})', LASTNAME = '$(getLastName ${FULLNAME_TW2})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_NA}', EMAIL = '$(getAlmaID ${USER_TW2})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_TW}', ALIASES = null, INST_NO = ${INST_TW2}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_TW2})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_TW2})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_TW2})');

/* advanced users */

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_TW3})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_TW3})', LASTNAME = '$(getLastName ${FULLNAME_TW3})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EA}', EMAIL = '$(getAlmaID ${USER_TW3})@${TOKEN_OBOPS_SWDEV}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_TW}', ALIASES = null, INST_NO = ${INST_TW1}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_TW3})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_TW3})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_TW3})');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_OBOPS_SWDEV}, '$(getAlmaID ${USER_TW3})');




/* =============== Section 5 (CL) =============== */

/* standard users */

update ACCOUNT set ACCOUNT_ID ='$(getAlmaID ${USER_CL1})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_CL1})', LASTNAME = '$(getLastName ${FULLNAME_CL1})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EU}', EMAIL = '$(getAlmaID ${USER_CL1})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_CL}', ALIASES = null, INST_NO = ${INST_CL1}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_CL1})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CL1})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CL1})');

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_CL2})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_CL2})', LASTNAME = '$(getLastName ${FULLNAME_CL2})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EA}', EMAIL = '$(getAlmaID ${USER_CL2})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_CL}', ALIASES = null, INST_NO = ${INST_CL2}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_CL2})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CL2})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CL2})');

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_CL3})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_CL3})', LASTNAME = '$(getLastName ${FULLNAME_CL3})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_NA}', EMAIL = '$(getAlmaID ${USER_CL3})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_CL}', ALIASES = null, INST_NO = ${INST_CL3}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_CL3})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CL3})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CL3})');

/* advanced users */

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_CL4})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_CL4})', LASTNAME = '$(getLastName ${FULLNAME_CL4})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EU}', EMAIL = '$(getAlmaID ${USER_CL4})@${TOKEN_OBOPS_SWDEV}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_CL}', ALIASES = null, INST_NO = ${INST_CL1}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_CL4})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CL4})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CL4})');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_OBOPS_SWDEV}, '$(getAlmaID ${USER_CL4})');




/* =============== Section 6 (Other) =============== */

/* standard users */

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_RSA})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_RSA})', LASTNAME = '$(getLastName ${FULLNAME_RSA})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EU}', EMAIL = '$(getAlmaID ${USER_RSA})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_OTHER}', ALIASES = null, INST_NO = ${INST_RSA}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_RSA})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_RSA})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_RSA})');

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_AU})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_AU})', LASTNAME = '$(getLastName ${FULLNAME_AU})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EA}', EMAIL = '$(getAlmaID ${USER_AU})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_OTHER}', ALIASES = null, INST_NO = ${INST_AU}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_AU})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_AU})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_AU})');

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_IND})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_IND})', LASTNAME = '$(getLastName ${FULLNAME_IND})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_NA}', EMAIL = '$(getAlmaID ${USER_IND})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_OTHER}', ALIASES = null, INST_NO = ${INST_IND}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_IND})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_IND})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_IND})');

/* advanced users */

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_IL})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_IL})', LASTNAME = '$(getLastName ${FULLNAME_IL})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EA}', EMAIL = '$(getAlmaID ${USER_IL})@${TOKEN_OBOPS_SWDEV}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_OTHER}', ALIASES = null, INST_NO = ${INST_IL}, ACTIVE = 'T'  where ACCOUNT_ID = '$(getAlmaID ${USER_IL})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_IL})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_IL})');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_OBOPS_SWDEV}, '$(getAlmaID ${USER_IL})');



/* =============== Section 7 (Special Settings) =============== */

/* inactive users */

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_CZ3})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_CZ3})', LASTNAME = '$(getLastName ${FULLNAME_CZ3})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EU}', EMAIL = '$(getAlmaID ${USER_CZ3})@${TOKEN_INACTIVE}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_EU}', ALIASES = null, INST_NO = ${INST_CZ2}, ACTIVE = 'F' where ACCOUNT_ID = '$(getAlmaID ${USER_CZ3})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CZ3})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CZ3})');

update ACCOUNT set ACCOUNT_ID = '$(getAlmaID ${USER_IRL2})', VERSION = 0, REQUEST_HANDLER_ID = null, PASSWORD_DIGEST = '${MD5HASH}', FIRSTNAME = '$(getFirstName ${FULLNAME_IRL2})', LASTNAME = '$(getLastName ${FULLNAME_IRL2})', INITIALS = null, CREATIONTIMESTAMP = null, MODIFICATIONTIMESTAMP = null, PREFERREDARC = '${ARC_EU}', EMAIL = '$(getAlmaID ${USER_IRL2})@${TOKEN_INACTIVE}${TEST_MAIL_DOMAIN}', EXECUTIVE = '${EXEC_EU}', ALIASES = null, INST_NO = ${INST_IRL}, ACTIVE = 'F' where ACCOUNT_ID = '$(getAlmaID ${USER_IRL2})';
delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_IRL2})';
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_IRL2})');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_OBOPS_SWDEV}, '$(getAlmaID ${USER_IRL2})');




commit;

select 'Command executed successfully' from DUAL;
+++

RETVAL=${?}
echo "SQL command exit code: ${RETVAL}"

