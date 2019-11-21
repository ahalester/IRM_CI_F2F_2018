#!/bin/bash

DATE=`date +%F`

if [ -d "$INTROOT" ]
then
  INSTALLROOT=$INTROOT
else
  INSTALLROOT=$ACSROOT
fi

LASTWORD=`awk 'END {print $NF}'  $INSTALLROOT/bin/ALMA-OT`
LASTBUTONELINE=`tail -2 $INSTALLROOT/bin/ALMA-OT | head -1`

if [ "$LASTWORD" = "fi" ]
then
  echo "\$cmd" >> $INSTALLROOT/bin/ALMA-OT
elif [ "$LASTWORD" = "\$cmd" ]
then 
  echo "$INSTALLROOT/bin/ALMA-OT already patched"
else
  echo "tests cannot be run: did not find fi statement"
  exit 1
fi

TESTGOAL=$1
YEAR=2018
MONCH=oct

case $TESTGOAL in 
    LOCAL) cd ../bin && ./run_test.sh -b -c6 2018OCT 2018AUG  2018JUL 2018JUN 2018MAY 2018APR TestSetConfigurationSanityCheck TestSetCycle4Phase1 TestSetGUISanityCheck 
    ;;
    REGRESSION) cd ../bin && ./run_test.sh -b -c6 -s http://phase-a.hq.eso.org/ 2018OCT 2018AUG  2018JUL 2018JUN 2018MAY 2018APR TestSetConfigurationSanityCheck TestSetCycle4Phase1 TestSetGUISanityCheck 
    ;; 
    PHASEB) cd ../bin && ./run_test.sh -b -c6 -s https://$YEAR$MONCH.asa-test.alma.cl/ 2018OCT 2018AUG 2018JUL 2018JUN 2018MAY 2018APR TestSetConfigurationSanityCheck TestSetCycle4Phase1 TestSetGUISanityCheck 
    ;; 
    SANITY) cd ../bin && ./run_test.sh -b -c6 TestSetConfigurationSanityCheck TestSetCycle4Phase1 TestSetGUISanityCheck 
    ;; 
    INCREMENTAL) cd ../bin && ./run_test.sh -b -c6 2018OCT
    ;; 
    ALL) cd ../bin && ./all_tests.sh
    ;;
    *) cd ../bin && ./run_test.sh -b -c6 TestSetConfigurationSanityCheck TestSetCycle4Phase1 TestSetGUISanityCheck
    ;;
esac


if [ $? != 0 ] ; then
  echo "FAILED."
else
  echo "PASSED."
fi

sleep 30

mkdir -p ../test/ALMA-OT-reports
cp -r ../log/$DATE* ../test/ALMA-OT-reports
echo "Reports copied under test directory"
# Note: following algorythm has been commented out because "cp" gives positive RC in any case 
# because of the usage of "*"
#if cp -r ../log/$DATE* ../test/ALMA-OT-reports
#then
#  echo "Reports copied under test directory"
#else
#  echo "Reports not found in log directory" 
#fi


