import sys
import os
import time
import unittest
import urllib2
import xmlrpclib
import logging
from testconfig import config
import DsaDataBase as Data
import DsaAlgorithm as Dsa


class DsaAlgorithmTestCase(unittest.TestCase):
    proxy = None
    datas = None

    def setUp(self):
        self.logger = logging.getLogger(self.__class__.__name__)
        self.logger.info( "Command line: " + str(sys.argv) )
        self.databasePopulated = False
        self.initializeDsaDatabase()

    def tearDown(self):
        return
    
    def initializeDsaDatabase(self):
        self.logger.debug( "initializeDsaDatabase()" )
        path = os.environ['APDM_PREFIX']
        refr = False # Use to force refreash at first try

        try:
            if time.time() - os.path.getmtime(path) > 360000.:
                refr = True # Refresh after 10 hours
        except OSError:
            os.mkdir(path) # Create the directory to save APDM data
            refr = True

        try:
            self.logger.info( "First try to populate DsaDatabase" )
            self.datas = Data.DsaDatabase(
                refresh_apdm=refr, loadp1=True, cycles=("2016", "2017"),
                ignore_prjstatus=(
                            "Canceled", "Rejected", "ObservingTimedOut",
                            "Completed", "NotObserved", "PartiallyCompleted"),
                projects=("A", "S", "T", "L"), correct_ot=False)
            self.databasePopulated = True
        except IOError:
            self.logger.info( "Second try to populate DsaDatabase, forcing refresh" )
            self.datas = Data.DsaDatabase(
                refresh_apdm=True, loadp1=True, cycles=("2016", "2017"),
                ignore_prjstatus=(
                            "Canceled", "Rejected", "ObservingTimedOut",
                            "Completed", "NotObserved", "PartiallyCompleted"),
                projects=("A", "S", "T", "L"), correct_ot=False)
            self.databasePopulated = True
        if self.databasePopulated:
            self.logger.info( "Database populated" )
        else:
            self.fail("Could not populate database")
        self.logger.debug( "initializeDsaDatabase() ended" )

