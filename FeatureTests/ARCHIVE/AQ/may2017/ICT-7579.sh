#!/bin/bash
SERVER=`basename $PWD`
FAIL=0
for i in `seq 20`; do
    wget -O data_${i}.dat \
    "https://${SERVER}.asa-test.alma.cl/aq/?source_name_resolver=M83&result_view=raw&science_observations=&download=true&format=CSV";
    #if [ ! -s data_${i}.dat ]; then
    #  FAIL=1
    #fi
    if [ $i == 1 ]; then continue; fi
    #size1=`wc -c < data_${i}.dat`; size0=`wc -c < data_$((${i}-1)).dat`
    #echo $size1 $size0
    if [ `wc -c < data_${i}.dat` != `wc -c < data_$((${i}-1)).dat` ]; then
        FAIL=1
    fi
done
ls -l *.dat
echo $FAIL
exit $FAIL

