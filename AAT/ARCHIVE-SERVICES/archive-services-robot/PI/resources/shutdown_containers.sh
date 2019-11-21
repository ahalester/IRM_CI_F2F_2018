#!/bin/sh

# Change this path according to you setup
DOCKERS_FILES_PATH=/diskb/lidia_workspace/almasw/ARCHIVE-SERVICES/Support/docker/oraclexe_ngas

cd $DOCKERS_FILES_PATH

echo "Shutting down containers"

docker-compose down