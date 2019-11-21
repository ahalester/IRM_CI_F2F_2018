"""
Requirements:

- Oracle Instant Client

http://www.oracle.com/technetwork/database/database-technologies/instant-client/overview/index.html

After installing remember to add the lib path to your $LD_LIBRARY_PATH environment variable
e.g.: export LD_LIBRARY_PATH=/usr/lib/oracle/12.2/client64/lib/:$LD_LIBRARY_PATH

- cx_Oracle module for Python

This can be installed via PIP
e.g.: pip install cx_Oracle
"""

import cx_Oracle
import sys
from optparse import OptionParser

class reportQA0ExecBlocks():
    def __init__(self, database_instance):
        connection = cx_Oracle.connect('alma', 'almafordev', "ORASW.APOTEST.ALMA.CL/" + database_instance + ".SCO.CL")
        self.cursor = connection.cursor()

    def validate_number(self, value):
        if value:
            return value
        else:
            return 0

    def tc01(self):
        named_params = {"status":"SUCCESS", "qa0status":"Unset"}
        eb_success_query = "select * from AQUA_V_EXECBLOCK where STATUS=:bind and QA0STATUS=:bind and EXECFRACTION=1"
        self.cursor.execute(eb_success_query, (named_params["status"], named_params["qa0status"]))
        execblock_list = self.cursor.fetchall()
    
        for execblock in execblock_list:
            mous_query = "select count(ARCHIVE_UID) from MV_OBSUNITSET where OUSSTATUSREF=(select MOUS_STATUS_UID from MV_SCHEDBLOCK where SB_ARCHIVE_UID=(select SCHEDBLOCKUID from AQUA_V_EXECBLOCK where EXECBLOCKUID=:bind)) and NUMBERSCHEDBLOCKS=1"
            self.cursor.execute(mous_query, bind = execblock[1])
            mous_uid = self.cursor.fetchall()
    
            if mous_uid[0][0] == 1:
                execfraction_mv_sched = "select EXECUTION_COUNT from MV_SCHEDBLOCK where SB_ARCHIVE_UID=(select SCHEDBLOCKUID from AQUA_V_EXECBLOCK where EXECBLOCKUID=:bind)"
                self.cursor.execute(execfraction_mv_sched, bind = execblock[1])
                sb_expected_ec = self.cursor.fetchall()
    
                execfraction_eb = "select SUM(EXECFRACTION) from AQUA_V_EXECBLOCK where SCHEDBLOCKUID=(select SCHEDBLOCKUID from AQUA_V_EXECBLOCK where EXECBLOCKUID=:bind) AND QA0STATUS = 'Pass'"
                self.cursor.execute(execfraction_eb, bind = execblock[1])
                sb_ec = self.cursor.fetchall()
    
                if mous_uid[0][0] == 1:
                    print 'ExecBlock', execblock[1], '\tSB Expected EC:', sb_expected_ec[0][0], '\tSB EC', self.validate_number(sb_ec[0][0]), '\tCandidate TC01:', 'YES' if ((int(sb_expected_ec[0][0]) -  self.validate_number(sb_ec[0][0])) <= 1) else 'NO' 


    def tc02(self):
        named_params = {"status":"SUCCESS", "qa0status":"Pending"}
        eb_success_query = "select * from AQUA_V_EXECBLOCK where STATUS=:bind and QA0STATUS=:bind"
        self.cursor.execute(eb_success_query, (named_params["status"], named_params["qa0status"]))
        execblock_list = self.cursor.fetchall()
    
        for execblock in execblock_list:
            mous_query = "select count(ARCHIVE_UID) from MV_OBSUNITSET where OUSSTATUSREF=(select MOUS_STATUS_UID from MV_SCHEDBLOCK where SB_ARCHIVE_UID=(select SCHEDBLOCKUID from AQUA_V_EXECBLOCK where EXECBLOCKUID=:bind)) and NUMBERSCHEDBLOCKS=1"
            self.cursor.execute(mous_query, bind = execblock[1])
            mous_uid = self.cursor.fetchall()

            if mous_uid[0][0] == 1:    
                qa0statusreason_query = "select QA0STATUSREASON from AQUA_EXECBLOCK where EXECBLOCKUID=:bind"
                self.cursor.execute(qa0statusreason_query, bind = execblock[1])
                qa0statusreason = self.cursor.fetchall()
        
                print 'ExecBlock', execblock[1], '\tQA Status Reason:', qa0statusreason[0][0], '\tCandidate TC02:', 'YES' if qa0statusreason[0][0] == 'FLUX_CAL' else 'NOT'

    def tc03(self):
        named_params = {"status":"SUCCESS", "qa0status":"Unset"}
        eb_success_query = "select * from AQUA_V_EXECBLOCK where STATUS=:bind and QA0STATUS=:bind"
        self.cursor.execute(eb_success_query, (named_params["status"], named_params["qa0status"]))
        execblock_list = self.cursor.fetchall()

        counter = 0
        for execblock in execblock_list: #Removed because return a huge list
            mous_query = "select count(ARCHIVE_UID) from MV_OBSUNITSET where OUSSTATUSREF=(select MOUS_STATUS_UID from MV_SCHEDBLOCK where SB_ARCHIVE_UID=(select SCHEDBLOCKUID from AQUA_V_EXECBLOCK where EXECBLOCKUID=:bind)) and NUMBERSCHEDBLOCKS=1"
            self.cursor.execute(mous_query, bind = execblock[1])
            mous_uid = self.cursor.fetchall()

            if mous_uid[0][0] == 1:    
                print 'ExecBlock', execblock[1], '\tStatus:', named_params["status"], '\tQA0 Status', named_params["qa0status"]
                counter += 1

                if counter == 10:
                    sys.exit(1)

def main(): 
    """
    Main method to call the program.
    Added parameters:
        -tc01: Report all the EB for TC01. ICT-12125
        -tc02:
        -tc03:
    """
    parser = OptionParser(usage="usage: %prog [options]")
    parser.add_option("-i", "--instance",
                      dest="instance",
                      default=False,
                      help="Database instance. Allowed values: ALMAI1, ALMAI2, ALMAI3")
    parser.add_option("-1", "--tc01",
                      action="store_true",
                      dest="tc01",
                      default=False,
                      help="Report all the EB for TC01. ICT-12125")
    parser.add_option("-2", "--tc02",
                      action="store_true",
                      dest="tc02",
                      default=False,
                      help="Report all the EB for TC02")
    parser.add_option("-3", "--tc03",
                      action="store_true",
                      dest="tc03",
                      default=False,
                      help="Report all the EB for TC03")
    (options, args) = parser.parse_args()

    if options.instance:
        r = reportQA0ExecBlocks(options.instance);
    else:
        sys.exit(0)

    if options.tc01:
        print "---------------------------------------- TC01 ExecBlock Availables ----------------------------------------"
        r.tc01()
        print

    if options.tc02:
        print "---------------------------------------- TC02 ExecBlock Availables ----------------------------------------"
        r.tc02()
        print

    if options.tc03:
        print "---------------------------------------- TC03 ExecBlock Availables ----------------------------------------"
        r.tc03()
        print

if __name__ == '__main__':
    main()
