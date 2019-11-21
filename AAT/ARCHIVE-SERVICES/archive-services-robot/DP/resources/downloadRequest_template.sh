#!/bin/bash

# This script runs on Linux and MaxOS and downloads all the selected files to the current working directory in up to 5 parallel download streams.
# Should a download be aborted just run the entire script again, as partial downloads will be resumed. Please play nice with the download systems
# at the ARCs and do not increase the number of parallel streams.

# connect / read timeout for wget / curl
export TIMEOUT_SECS=300
# how many times do we want to automatically resume an interrupted download?
export MAX_RETRIES=3
# after a timeout, before we retry, wait a bit. Maybe the servers were overloaded, or there was some scheduled downtime.
# with the default settings we have 15 minutes to bring the dataportal service back up.
export WAIT_SECS_BEFORE_RETRY=300
# the files to be downloaded

#Beginlist
LIST=("to-be-replaced")
#EndList

# quickly log in to the server (if required) and create a cookies file. This is because some users are using shared machines for long-running
# downloads where the credentials can be seen in plain-text in the task list.
function start_session {
  local username=ldoming
  export authentication_status=0
  if [ "${username}" != "anonymous" ]; then
    # only prompt for the password again if we haven't already. What could be going on here? The servers
    # have been restarted and the local cookie file is no longer valid.
    if [ -z ${password} ]; then
      echo ""
      echo -n "Please enter the password for ALMA account ${username}: "
      read -s password
      echo ""
      export password
    fi

    if command -v "curl" > /dev/null 2>&1; then
      login_command=(curl -s -k -o /dev/null -c alma-rh-cookie.txt --fail "-u" "${username}:${password}")
    elif command -v "wget" > /dev/null 2>&1; then
      login_command=(wget --quiet --delete-after --no-check-certificate --auth-no-challenge --keep-session-cookies --save-cookies alma-rh-cookie.txt "--http-user=${username}" "--http-password=${password}")
    fi
    # echo "${login_command[@]}" "https://2018aug.asa-test.alma.cl/dataPortal/api/login"
    $("${login_command[@]}" "https://2018aug.asa-test.alma.cl/dataPortal/api/login")
    authentication_status=$?
    if [ $authentication_status -eq 0 ]; then
      echo "            OK: credentials accepted."
    else
      echo "            ERROR: login failed. Error code is ${authentication_status}"
    fi
  fi
}
export -f start_session

# clean up the cookies file after downloading is complete
function end_session {
  rm -fr alma-rh-cookie.txt
}
export -f end_session

# download a single file.
# attempt the download up to N times
function download {
  local file=$1
  local filename=$(basename $file)
  # the nth attempt to download a single file
  local attempt_num=0

  # wait for some time before starting - this is to stagger the load on the server (download start-up is relatively expensive)
  sleep $[ ( $RANDOM % 10 ) + 2 ]s

  if command -v "curl" > /dev/null 2>&1; then
    local tool_name="curl"
    local download_command=(curl -C - -S -s -k -O -f -b alma-rh-cookie.txt --speed-limit 1 --speed-time ${TIMEOUT_SECS})
  elif command -v "wget" > /dev/null 2>&1; then
    local tool_name="wget"
    local download_command=(wget -c -q -nv --no-check-certificate --auth-no-challenge --load-cookies alma-rh-cookie.txt --timeout=${TIMEOUT_SECS} --tries=1)
  fi

  # manually retry downloads.
  # I know wget and curl can both do this, but curl (as of 10.04.2018) will not allow retry and resume. I want consistent behaviour so
  # we implement the retry mechanism ourselves.
  echo "starting download of $filename"
  until [ ${attempt_num} -ge ${MAX_RETRIES} ]
  do
    # echo "${download_command[@]}" "$file"
    $("${download_command[@]}" "$file")
    status=$?
    # echo "status ${status}"
    if [ ${status} -eq 0 ]; then
      echo "            succesfully downloaded $filename"
      break
    else
      # users requested a string instead of a simple exit code - attempt to make it look like the curl output
      if [ ${status} -eq 8 ] && [ ${tool_name} = "wget" ]; then
        echo "wget: (8) Server issued an error response."
      elif [ ${status} -eq 4 ] && [ ${tool_name} = "wget" ]; then
        echo "wget: (4) Network failure."
      elif { [ ${status} -eq 6 ] && [ ${tool_name} = "wget" ]; } || { [ ${status} -eq 22 ] && [ ${tool_name} = "curl" ]; }; then
        # if we restart the servers then our cookies are invalidated
        end_session
        echo "authentication error - retrying the login"
        start_session
        if [ $authentication_status -eq 0 ]; then
          # our retry attempt was hampered by a credentials issue. We shouldn't count this as an attempt. Give a bonus try...
          echo "                resuming download of $filename, still attempt $[${attempt_num}+1]"
	      $("${download_command[@]}" "$file")
        fi
      fi

      echo "                download $filename was interrupted with error code ${tool_name}/${status}"
      attempt_num=$[${attempt_num}+1]
      if [ ${attempt_num} -ge ${MAX_RETRIES} ]; then
        echo "          ERROR giving up on downloading $filename after ${MAX_RETRIES} attempts  - rerun the script manually to retry."
      else
        echo "                download $filename will automatically resume after ${WAIT_SECS_BEFORE_RETRY} seconds"
        sleep ${WAIT_SECS_BEFORE_RETRY}
        echo "                resuming download of $filename, attempt $[${attempt_num}+1]"
      fi
    fi
  done
}
export -f download

# Main body
# ---------

# check that we have one of the required download tools
if ! (command -v "wget" > /dev/null 2>&1 || command -v "curl" > /dev/null 2>&1); then
   echo "ERROR: neither 'wget' nor 'curl' are available on your computer. Please install one of them.";
   exit 1
fi

echo "Downloading the following files in up to 5 parallel streams. Total size is 54.9GB."
echo "${LIST}"
echo "In case of errors each download will be automatically resumed up to 3 times after a 5 minute delay"
echo "To manually resume interrupted downloads just re-run the script."
# tr converts spaces into newlines. Written legibly (spaces replaced by '_') we have: tr "\_"_"\\n"
# IMPORTANT. Please do not increase the parallelism. This may result in your downloads being throttled.
# Please do not split downloads of a single file into multiple parallel pieces.
start_session
if [ $authentication_status -eq 0 ]; then
	echo "your downloads will start shortly...."
	echo ${LIST} | tr \  \\n | xargs -P5 -n1 -I '{}' bash -c 'download {};'
fi
end_session
echo "Done."
