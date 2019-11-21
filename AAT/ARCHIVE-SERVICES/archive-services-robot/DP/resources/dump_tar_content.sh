#!/bin/bash


export fileName=./output/$1
export extracted_dir=$2

ls -lahR $extracted_dir | awk '{print $1,$5,$9}' | egrep  -v ' \.*$' > $fileName