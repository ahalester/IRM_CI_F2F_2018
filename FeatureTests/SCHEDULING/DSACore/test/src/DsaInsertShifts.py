import os
import time
import unittest
import cx_Oracle
import logging
import DsaDataBase as Data
import DsaAlgorithm as Dsa

class DsaInsertShifts(unittest.TestCase):
    proxy = None
    con = None
    cur = None
    con_str = None
    
    def open_oracle_conn(self):       
        connection = cx_Oracle.connect(self.con_str, threaded=True)
        cursor = connection.cursor()
        return connection, cursor
    
    def setUp(self):
        self.con_str =  os.environ['CON_STR']
        self.con, self.cur = self.open_oracle_conn()
        
        insert_fsr = str(
            "insert into ALMA.SHIFTLOG_ENTRIES (SE_ID, SE_TYPE,SE_SUBJECT, SE_TIMESTAMP, SE_AUTHOR, SE_START,  SE_LOCATION) "
            "   select SHIFTLOG_ENTRIES_SEQ.nextval, 9, 'FullSystemRestart was executed at DATE and lasted 45.68 seconds, using ONLINE-CYCLE5-B-2017-08-24-00-00-00', "
            "       CURRENT_TIMESTAMP - 0.15, 'almaop', CURRENT_TIMESTAMP - 0.152, 'SCO-ACSE' "
            "   from dual")
        insert_shift = str(
            "insert into ALMA.SHIFTLOG_ENTRIES (SE_ID, SE_TYPE, SE_SUBJECT, SE_TIMESTAMP, SE_AUTHOR, SE_START,  SE_LOCATION, SE_ALMABUILD, SE_ACSVERSION, SE_SHIFTACTIVITY) "
            "   select SHIFTLOG_ENTRIES_SEQ.nextval, 1, 'Science', CURRENT_TIMESTAMP - 0.1, 'dsacore-sim', CURRENT_TIMESTAMP - 0.1, 'SCO-ACSE', "
            "       'ONLINE-CYCLE5-B-2017-08-24-00-00-00', 'ACS-DEC2016.', 'SciOps' "
            "   from dual")
        insert_12marray = str(
            "insert into ALMA.SHIFTLOG_ENTRIES (SE_ID, SE_TYPE, SE_TIMESTAMP, SE_AUTHOR, SE_START, SE_LOCATION, SE_ARRAYNAME, SE_ARRAYTYPE, SE_ARRAYFAMILY, SE_CORRELATORTYPE, SE_PHOTONICREFERENCENAME, SE_MAINACTIVITY) "
            "select SHIFTLOG_ENTRIES_SEQ.nextval, 7, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'SCO-ACSE', 'Array1-BLC', 'Automatic', '12 [m]', 'BL', 'PhotonicReference1', 'PI Observations' "
            "from dual")
        insert_7marray = str(
            "insert into ALMA.SHIFTLOG_ENTRIES (SE_ID, SE_TYPE, SE_TIMESTAMP, SE_AUTHOR, SE_START, SE_LOCATION, SE_ARRAYNAME, SE_ARRAYTYPE, SE_ARRAYFAMILY, SE_CORRELATORTYPE, SE_PHOTONICREFERENCENAME, SE_MAINACTIVITY)"
            "select SHIFTLOG_ENTRIES_SEQ.nextval, 7, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'SCO-ACSE', 'Array2-BLC', 'Automatic', '7 [m]', 'ACA', 'PhotonicReference2', 'PI Observations' "
            "from dual")
        insert_tparray = str(
            "insert into ALMA.SHIFTLOG_ENTRIES (SE_ID, SE_TYPE, SE_TIMESTAMP, SE_AUTHOR, SE_START, SE_LOCATION, SE_ARRAYNAME, SE_ARRAYTYPE, SE_ARRAYFAMILY, SE_CORRELATORTYPE, SE_PHOTONICREFERENCENAME, SE_MAINACTIVITY) "
            "select SHIFTLOG_ENTRIES_SEQ.nextval, 7, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'SCO-ACSE', 'Array3-BLC', 'Automatic', 'Total Power', 'ACA', 'PhotonicReference3', 'PI Observations' "
            "from dual")
        commit = str("commit");
        try: 
            self.cur.execute(insert_fsr)
            self.cur.execute(insert_shift)
            self.cur.execute(commit)
            self.cur.execute(insert_12marray)
            self.cur.execute(insert_7marray)
            self.cur.execute(insert_tparray)
        except Exception:
            logging.exception("Insert operation failed with Please check the connnection string: " + self.con_str )
        
        #TODO: Still needs to be parametrized
        select_shift = str("SELECT SE_ID FROM (SELECT a.*, max(SE_TIMESTAMP) OVER () AS latest FROM ALMA.SHIFTLOG_ENTRIES a WHERE SE_TYPE = 1 AND SE_TIMESTAMP > SYSDATE - 3. AND SE_SHIFTACTIVITY = 'SciOps'")
        select_seid_12marray = str(
            "select SE_ID from (select se.SE_ID from ALMA.SHIFTLOG_ENTRIES se where se.SE_TYPE=7 AND se.SE_ARRAYFAMILY='12 [m]' order by se.SE_TIMESTAMP desc) where ROWNUM <= 1")
        select_seid_7marray = str(
            "select SE_ID from (select se.SE_ID from ALMA.SHIFTLOG_ENTRIES se where se.SE_TYPE=7 AND se.SE_ARRAYFAMILY='7 [m]' order by se.SE_TIMESTAMP desc) where ROWNUM <= 1")
        select_seid_tparray = str(
            "select SE_ID from (select se.SE_ID from ALMA.SHIFTLOG_ENTRIES se where se.SE_TYPE=7 AND se.SE_ARRAYFAMILY='Total Power' order by se.SE_TIMESTAMP desc) where ROWNUM <= 1 ")
        
        try:
            self.cur.execute(select_seid_12marray)
            id12m_str = self.cur.fetchall()[0][0]
            id12m = int(id12m_str)
            
            self.cur.execute(select_seid_7marray)
            id7m_str = self.cur.fetchall()[0][0]
            id7m = int(id7m_str)
            
            self.cur.execute(select_seid_tparray)
            idtp_str = self.cur.fetchall()[0][0]
            idtp = int(idtp_str)
            
            self.cur.execute(select_seid_tparray)
            idshift_str = self.cur.fetchall()[0][0]
            idshift = int(idshift_str)
            logging.info("12m: " + str(id12m) + " 7m: " + str(id7m) + " TP: " + str(idtp))
            
        except IndexError:
            logging.exception("Could not get the ID for the latest arrays shiftlog entry")
        
        #TODO: Still needs to be parametrized
        insert_ref_antenna = str("insert into ALMA.SLOG_ENTRY_ATTR (SLOG_ATTR_ID, SLOG_ATTR_TYPE, SLOG_ATTR_VALUE, SLOG_SE_ID) values( SLOG_ENTRY_ATTR_SEQ.nextval, 12, 'DV19:A033', %d )" % ( idshift) );
        
        for i in range(1, 26):
            ant_dv = str("insert into ALMA.SLOG_ENTRY_ATTR (SLOG_ATTR_ID, SLOG_ATTR_TYPE, SLOG_ATTR_VALUE, SLOG_SE_ID) values( SLOG_ENTRY_ATTR_SEQ.nextval, 31, '%s', %d)" % ( str("DV%02d" % (i)), id12m ) )
            self.cur.execute(ant_dv)
        for i in range(41, 56):
            ant_da = str("insert into ALMA.SLOG_ENTRY_ATTR (SLOG_ATTR_ID, SLOG_ATTR_TYPE, SLOG_ATTR_VALUE, SLOG_SE_ID) values( SLOG_ENTRY_ATTR_SEQ.nextval, 31, '%s', %d)" % ( str("DA%02d" % (i)), id12m ) )
            self.cur.execute(ant_da)
        for i in range(1, 11):
            ant_cm = str("insert into ALMA.SLOG_ENTRY_ATTR (SLOG_ATTR_ID, SLOG_ATTR_TYPE, SLOG_ATTR_VALUE, SLOG_SE_ID) values( SLOG_ENTRY_ATTR_SEQ.nextval, 31, '%s', %d)" % ( str("CM%02d" % (i)), id7m ) )
            self.cur.execute(ant_cm)
        for i in range(1, 5):
            ant_pm = str("insert into ALMA.SLOG_ENTRY_ATTR (SLOG_ATTR_ID, SLOG_ATTR_TYPE, SLOG_ATTR_VALUE, SLOG_SE_ID) values( SLOG_ENTRY_ATTR_SEQ.nextval, 31, '%s', %d)" % ( str("PM%02d" % (i)), idtp ) )
            self.cur.execute(ant_pm)
            
        try:
            self.cur.execute(commit)
        except Exception:
            logging.exception("Insert operation failed with Please check the connnection string: " + self.con_str )

        
        


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

