#! /bin/bash
#
# Fill out variables in Definitions.java
#
# Created: 2017-05-19
# Author: Kyoko Nakamura
#
# Usage: cat <original filename> | ./Setupvariables.sh <variables filename> > <new filename>
#
# Updated
# 2017-07-14 Replace words even if any texts are present, and added general params.
# 2017-11-16 Added parameters for Exec Inf. and Report.

# Get variables from a file. 
VARIABLES=$1
. ./$VARIABLES

# Change "/" to "\/"
# It's because sed cannot handle "/" directly.
testurl=`echo $testUrl | sed -e 's/\//\\\\\//g'` 
caslogout=`echo $casLogout | sed -e 's/\//\\\\\//g'` 

# Fill out data
sed -e "s/BASEURL = \".*\";/BASEURL = \"${testurl}\";/" | \
sed -e "s/ENFORCED_LOGOUT_URL = \".*\";/ENFORCED_LOGOUT_URL = \"${caslogout}\";/" | \
#
sed -e "s/USERNAME = \".*\";/USERNAME = \"${testUser}\";/" | \
sed -e "s/PASSWORD = \".*\";/PASSWORD = \"${testUserPW}\";/" | \
sed -e "s/USERNAME_WO_AUTH = \".*\";/USERNAME_WO_AUTH = \"${noauthUser}\";/" | \
sed -e "s/PASSWORD_WO_AUTH = \".*\";/PASSWORD_WO_AUTH = \"${noauthUserPW}\";/" | \
#
sed -e "s/OTHER_BEGIN = \".*\";/OTHER_BEGIN = \"${otherBegin}\";/" | \
sed -e "s/OTHER_END = \".*\";/OTHER_END = \"${otherEnd}\";/" | \
sed -e "s/ACT_BEGIN = \".*\";/ACT_BEGIN = \"${actBegin}\";/" | \
sed -e "s/ACT_END = \".*\";/ACT_END = \"${actEnd}\";/" | \
sed -e "s/INDEF_BEGIN = \".*\";/INDEF_BEGIN = \"${indefBegin}\";/" | \
#
sed -e "s/\"almaproc\".*Author\"/\"${genAuthor}\",\t\/\/\"Author\"/" | \
sed -e "s/\"test\",/\"${genSubject}\",/" | \
sed -e "s/\"ACA4\",/\"${genLocation}\",/" | \
sed -e "s/\"deleted\"/\"${genComment}\"/" | \
sed -e "s/\"9999.4\"/\"${execProCode}\"/" | \
sed -e "s/\"aca-fx\"/\"${execProName}\"/" | \
sed -e "s/\"almaproc\".*Project PI\"/\"${execProPI}\",\t\/\/\"Project PI\"/" | \
sed -e "s/\"11\".*Project Version\"/\"${execProVer}\",\t\/\/\"Project Version\"/" | \
sed -e "s/\"b0537\"/\"${execSBName}\"/" | \
sed -e "s/\"X11\".*SchedBlock UID\"/\"${execSBUid}\"/" | \
sed -e "s/\"X1\".*ExecBlock UID\"/\"${execEBUid}\"/" | \
sed -e "s/\"Array001\"/\"${execArrayName}\"/" | \
sed -e "s/\"CM01\"/\"${execAntName}\"/" | \
sed -e "s/\"PhotonicReference2\"/\"${execPRef}\"/" | \
sed -e "s/\"CL\".*Executive\(s\)\"/\"${execExecutive}\"/" | \
#
sed -e "s/OTHER_BEGINREP = \".*\";/OTHER_BEGINREP = \"${repBegin}\";/" | \
sed -e "s/OTHER_ENDREP = \".*\";/OTHER_ENDREP = \"${repEnd}\";/" | \
sed -e "s/PRJ_CODE = \".*\";/PRJ_CODE = \"${repPrjCode}\";/" | \
sed -e "s/MY_SITE = \".*\";/MY_SITE = \"${repSite}\";/" | \
sed -e "s/MY_LOCATION = \".*\";/MY_LOCATION = \"${repLocation}\";/"

