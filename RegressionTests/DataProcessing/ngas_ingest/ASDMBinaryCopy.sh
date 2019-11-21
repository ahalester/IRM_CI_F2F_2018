#$1: Servers
#$2: ASDM Directory
 
SERVERS=$1
ASDM_XML=$2
ASDM_BIN=${ASDM_XML}/ASDMBinary
 
echo "Servers: $SERVERS"
echo "ASDM Directory: $ASDM_XML"
for i in $(ls --color=never ${ASDM_XML} |grep "\.bin"); do
    $(ls ${ASDM_XML}/$i |grep missing > /dev/null)
    if [ $? -eq 0 ]; then
        echo "Binary $(echo $i |sed "s/\.missing//"): $(cat ${ASDM_XML}/$i)"
    else
        TYPE=$(head ${ASDM_XML}/$i -n 2 |tail -n 1 |awk '{print $2}' |tr -d ';')
        ngamsCClient -servers $SERVERS -cmd ARCHIVE -fileUri ${ASDM_XML}/$i -MIMETYPE "multipart/mixed"
    fi
done
for i in $(ls --color=never ${ASDM_BIN}); do
    $(ls ${ASDM_BIN}/$i |grep missing > /dev/null)
    if [ $? -eq 0 ]; then
        echo "Binary $(echo $i |sed "s/\.missing//"): $(cat ${ASDM_BIN}/$i)"
    else
        TYPE=$(head ${ASDM_BIN}/$i -n 2 |tail -n 1 |awk '{print $2}' |tr -d ';')
        ngamsCClient -servers $SERVERS -cmd ARCHIVE -fileUri ${ASDM_BIN}/$i -MIMETYPE $TYPE
    fi
done
