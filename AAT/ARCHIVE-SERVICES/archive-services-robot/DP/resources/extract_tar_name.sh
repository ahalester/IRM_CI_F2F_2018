#!/bin/sh

echo Extracting the tar file name from the download output into a file

cat output/dp_stdout.txt | grep succesfully | grep tar | head -1 | sed -e's/  */ /g' | cut -d " " -f4 > ./output/tar_name.txt

