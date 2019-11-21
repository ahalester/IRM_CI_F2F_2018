#!/bin/bash

cd .. && java -cp 'lib/*' org.testng.TestNG config/testng.xml

if [ $? != 0 ] ; then
  echo "FAILED."
else
  echo "PASSED."
fi

sleep 30
mv test-output ./test

