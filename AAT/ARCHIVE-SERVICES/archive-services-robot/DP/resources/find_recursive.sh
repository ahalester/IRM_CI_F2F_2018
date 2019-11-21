#!/bin/bash

export dir=$1
export string_to_find=$2

ls -ld $(find $1) | grep $2