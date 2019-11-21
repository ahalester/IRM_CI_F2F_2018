import os
import time
import unittest
import cx_Oracle
import logging
import DsaDataBase as Data
import DsaAlgorithm as Dsa

class ICT10846NewAQUAPendingStatus(DsaAlgorithmTestCase.DsaAlgorithmTestCase):
    proxy = None
    con = None
    cur = None
    con_str = None
    
    def open_oracle_conn(self):    
        connection = cx_Oracle.connect(self.con_str, threaded=True)
        cursor = connection.cursor()
        self.con = connection
        self.cur = cursor
    
    def test_ICT10846NewAQUAPendingStatus(self):
        '''
        ICT-10846: New AQUA Pending Status needs to be parsed and handled by DSA. 
        This test case checks for the new statuses in the QA0 database, and checks 
        taht the have the expected status in the DSA Database internal representation.
        '''
        self.con_str =  os.environ['CON_STR']
        self.open_oracle_conn()
        
        # We obtain from AQUA the statuses
        pendingAQUAEBsSQL = str("select EXECBLOCKUID from AQUA_EXECBLOCK where QA0STATUS like 'Pending%';")
        try: 
            self.cur.execute(pendingAQUAEBsSQL)
        except Exception:
            logging.exception("Select operation failed with Please check the connnection string: " + self.con_str )
        ebs = self.cur.fetchall()
        if ebs = None:
            self.skip("No Pending EBs in the database, cannot continue testing")
        
        # From that, we prepara a string for the next sentence
        ebsString = ''
        for eb in ebs:
            ebsString = ebsString + "'" + eb[0] + "',"
        ebsString = ebsString[:-1] 
        
        # We obtain the Codes from shiftlog entries for the EBUID obtain previously
        pendingSLTEBsSQL = str('select SE_EB_UID, SE_PROJECT_CODE from SHIFTLOG_ENTRIES where SE_EB_UID in (' + ebsString + ')')
        try: 
            self.cur.execute(pendingSLTEBsSQL)
        except Exception:
            logging.exception("Select operation failed with Please check the connnection string: " + self.con_str )
        ebsSLT = self.cur.fetchall()

        # Traverse through results and check...
        for ebSLT in ebsSLT:
            # ... that is not a CSV project
            if( '.CSV' in ebSLT[1] ):
                continue            
            # ... that the EBUID is present in the DSA Database
            result = datas.aqua_execblock[datas.aqua_execblock['EXECBLOCKUID'] == ebSLT[0] ]
            if( result.count()[0] == 0):
                self.fail("EBUID not present in DSA: " + ebSLT[0] + " CODE: " + ebSLT[1] )

        

    def tearDown(self):
        try: 
            self.cur.close()
            self.con.close()
        except Exception:
            logging.exception("Connection was not opened during the test case. Something must have failed")    
        return
    
    def test_bla(self):
        logging.info("Testing")
    
if __name__ == '__main__':
    unittest.main()

