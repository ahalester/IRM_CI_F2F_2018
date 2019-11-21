#!/bin/sh


properties_file=$1
property_key=$2



function getProperty {
   PROP_KEY=$1
   PROP_VALUE=`cat $properties_file | grep "$PROP_KEY" | cut -d'=' -f2`
   echo $PROP_VALUE
}

echo "Reading property from $properties_file"

property_value=$(getProperty $property_key)

echo $property_value > ./output/$property_key.txt