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

if [ $# -gt 0 ]; then
    source usersettings.sh $@
else
    source usersettings.sh
fi


# Overwrite the executive definition for the creation of accounts because
# starting from ALMA106 the executive should be null in the account table
# while it should be defined in the institution table
EXEC_ACCOUNT_EU=
EXEC_ACCOUNT_EA=
EXEC_ACCOUNT_NA=
EXEC_ACCOUNT_TW=
EXEC_ACCOUNT_CL=
EXEC_ACCOUNT_OTHER=


#======================================================================================

echo
echo "Writing users to data base..."

#cat <<+++
sqlplus ${ARCHIVE_USER}/${ARCHIVE_PASSWD}${ARCHIVE_CONNECT} <<+++
WHENEVER OSERROR EXIT
WHENEVER SQLERROR EXIT SQL.SQLCODE

/* =============== Section 1 (EU) =============== */

/* standard users */


delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CZ1})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_CZ1})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_CZ1})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_CZ1})', '$(getLastName ${FULLNAME_CZ1})', null, null, null, '${ARC_EU}', '$(getAlmaID ${USER_CZ1})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_EU}', null, ${INST_CZ1}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CZ1})');


delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CZ2})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_CZ2})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_CZ2})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_CZ2})', '$(getLastName ${FULLNAME_CZ2})', null, null, null, '${ARC_EU}', '$(getAlmaID ${USER_CZ2})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_EU}', null, ${INST_CZ2}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CZ2})');

/* advanced users */

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_IRL1})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_IRL1})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_IRL1})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_IRL1})', '$(getLastName ${FULLNAME_IRL1})', null, null, null, '${ARC_EU}', '$(getAlmaID ${USER_IRL1})@${TOKEN_OBOPS_SWDEV}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_EU}', null, ${INST_IRL}, 'T','T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_IRL1})');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_OBOPS_SWDEV}, '$(getAlmaID ${USER_IRL1})');

/* PHT */

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_UK})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_UK})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_UK})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_UK})', '$(getLastName ${FULLNAME_UK})', null, null, null, '${ARC_EU}', '$(getAlmaID ${USER_UK})@${TOKEN_OBOPS_PHT}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_EU}', null, ${INST_UK}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_UK})');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_OBOPS_PHT}, '$(getAlmaID ${USER_UK})');





/* =============== Section 2 (EA) =============== */

/* standard users */

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_JP1})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_JP1})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_JP1})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_JP1})', '$(getLastName ${FULLNAME_JP1})', null, null, null, '${ARC_EA}', '$(getAlmaID ${USER_JP1})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_EA}', null, ${INST_JP1}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_JP1})');

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_JP2})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_JP2})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_JP2})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_JP2})', '$(getLastName ${FULLNAME_JP2})', null, null, null, '${ARC_EA}', '$(getAlmaID ${USER_JP2})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_EA}', null, ${INST_JP2}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_JP2})');

/* advanced users */

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_JP3})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_JP3})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_JP3})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_JP3})', '$(getLastName ${FULLNAME_JP3})', null, null, null, '${ARC_EA}', '$(getAlmaID ${USER_JP3})@${TOKEN_OBOPS_SWDEV}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_EA}', null, ${INST_JP3}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_JP3})');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_OBOPS_SWDEV}, '$(getAlmaID ${USER_JP3})');



/* =============== Section 3 (NA) =============== */

/* standard users */

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CDN1})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_CDN1})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_CDN1})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_CDN1})', '$(getLastName ${FULLNAME_CDN1})', null, null, null, '${ARC_NA}', '$(getAlmaID ${USER_CDN1})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_NA}', null, ${INST_CDN}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CDN1})');

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_USA})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_USA})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_USA})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_USA})', '$(getLastName ${FULLNAME_USA})', null, null, null, '${ARC_NA}', '$(getAlmaID ${USER_USA})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_NA}', null, ${INST_USA}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_USA})');

/* advanced users */

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CDN2})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_CDN2})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_CDN2})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_CDN2})', '$(getLastName ${FULLNAME_CDN2})', null, null, null, '${ARC_NA}', '$(getAlmaID ${USER_CDN2})@${TOKEN_OBOPS_SWDEV}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_NA}', null, ${INST_CDN}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CDN2})');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_OBOPS_SWDEV}, '$(getAlmaID ${USER_CDN2})');



/* =============== Section 4 (TW) =============== */

/* standard users */

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_TW1})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_TW1})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_TW1})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_TW1})', '$(getLastName ${FULLNAME_TW1})', null, null, null, '${ARC_EA}', '$(getAlmaID ${USER_TW1})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_TW}', null, ${INST_TW1}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_TW1})');

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_TW2})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_TW2})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_TW2})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_TW2})', '$(getLastName ${FULLNAME_TW2})', null, null, null, '${ARC_NA}', '$(getAlmaID ${USER_TW2})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_TW}', null, ${INST_TW2}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_TW2})');

/* advanced users */

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_TW3})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_TW3})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_TW3})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_TW3})', '$(getLastName ${FULLNAME_TW3})', null, null, null, '${ARC_EA}', '$(getAlmaID ${USER_TW3})@${TOKEN_OBOPS_SWDEV}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_TW}', null, ${INST_TW1}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_TW3})');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_OBOPS_SWDEV}, '$(getAlmaID ${USER_TW3})');




/* =============== Section 5 (CL) =============== */

/* standard users */

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CL1})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_CL1})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_CL1})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_CL1})', '$(getLastName ${FULLNAME_CL1})', null, null, null, '${ARC_EU}', '$(getAlmaID ${USER_CL1})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_CL}', null, ${INST_CL1}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CL1})');

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CL2})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_CL2})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_CL2})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_CL2})', '$(getLastName ${FULLNAME_CL2})', null, null, null, '${ARC_EA}', '$(getAlmaID ${USER_CL2})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_CL}', null, ${INST_CL2}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CL2})');

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CL3})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_CL3})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_CL3})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_CL3})', '$(getLastName ${FULLNAME_CL3})', null, null, null, '${ARC_NA}', '$(getAlmaID ${USER_CL3})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_CL}', null, ${INST_CL3}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CL3})');

/* advanced users */

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CL4})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_CL4})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_CL4})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_CL4})', '$(getLastName ${FULLNAME_CL4})', null, null, null, '${ARC_EU}', '$(getAlmaID ${USER_CL4})@${TOKEN_OBOPS_SWDEV}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_CL}', null, ${INST_CL1}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CL4})');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_OBOPS_SWDEV}, '$(getAlmaID ${USER_CL4})');




/* =============== Section 6 (Other) =============== */

/* standard users */

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_RSA})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_RSA})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_RSA})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_RSA})', '$(getLastName ${FULLNAME_RSA})', null, null, null, '${ARC_EU}', '$(getAlmaID ${USER_RSA})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_OTHER}', null, ${INST_RSA}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_RSA})');

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_AU})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_AU})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_AU})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_AU})', '$(getLastName ${FULLNAME_AU})', null, null, null, '${ARC_EA}', '$(getAlmaID ${USER_AU})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_OTHER}', null, ${INST_AU}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_AU})');

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_IND})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_IND})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_IND})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_IND})', '$(getLastName ${FULLNAME_IND})', null, null, null, '${ARC_NA}', '$(getAlmaID ${USER_IND})@${TOKEN_MASTER_USER}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_OTHER}', null, ${INST_IND}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_IND})');

/* advanced users */

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_IL})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_IL})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_IL})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_IL})', '$(getLastName ${FULLNAME_IL})', null, null, null, '${ARC_EA}', '$(getAlmaID ${USER_IL})@${TOKEN_OBOPS_SWDEV}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_OTHER}', null, ${INST_IL}, 'T', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_IL})');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_OBOPS_SWDEV}, '$(getAlmaID ${USER_IL})');



/* =============== Section 7 (Special Settings) =============== */

/* inactive users */

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_CZ3})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_CZ3})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_CZ3})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_CZ3})', '$(getLastName ${FULLNAME_CZ3})', null, null, null, '${ARC_EU}', '$(getAlmaID ${USER_CZ3})@${TOKEN_INACTIVE}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_OTHER}', null, ${INST_CZ2}, 'F','T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_CZ3})');

delete from ACCOUNT_ROLE where ACCOUNT_ID = '$(getAlmaID ${USER_IRL2})';
delete from ACCOUNT where ACCOUNT_ID = '$(getAlmaID ${USER_IRL2})';
insert into ACCOUNT (ACCOUNT_ID, VERSION, REQUEST_HANDLER_ID, PASSWORD_DIGEST, FIRSTNAME, LASTNAME, INITIALS, CREATIONTIMESTAMP, MODIFICATIONTIMESTAMP, PREFERREDARC, EMAIL, EXECUTIVE, ALIASES, INST_NO, ACTIVE, RECEIVEEMAILS) values ('$(getAlmaID ${USER_IRL2})', 0, null, '${MD5HASH}', '$(getFirstName ${FULLNAME_IRL2})', '$(getLastName ${FULLNAME_IRL2})', null, null, null, '${ARC_EU}', '$(getAlmaID ${USER_IRL2})@${TOKEN_INACTIVE}${TEST_MAIL_DOMAIN}', '${EXEC_ACCOUNT_OTHER}', null, ${INST_IRL}, 'F', 'T');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_MASTER_USER}, '$(getAlmaID ${USER_IRL2})');
insert into ACCOUNT_ROLE (ROLE_NO, ACCOUNT_ID) values (${ROLE_OBOPS_SWDEV}, '$(getAlmaID ${USER_IRL2})');



commit;

select 'Command executed successfully' from DUAL;
+++

RETVAL=${?}
echo "SQL command exit code: ${RETVAL}"
