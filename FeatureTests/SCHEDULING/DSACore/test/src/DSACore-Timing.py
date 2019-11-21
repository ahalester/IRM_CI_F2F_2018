import os
import time
import unittest
import cx_Oracle
import logging
import timeit
import DsaDataBase as Data
import DsaAlgorithm as Dsa
import DsaAlgorithmTestCase

class DSACoreTiming(DsaAlgorithmTestCase.DsaAlgorithmTestCase):

    
    def setUp(self):
        self.logger = logging.getLogger(self.__class__.__name__)
        self.logger.info( "Command line: " + str(sys.argv) )
        self.databasePopulated = False
    
    def testTiming(self):
        time = timeit.timeit('self.initializeDsaDatabase()', number=1)
        if (time > 0.0 ):
            self.fail("Time is: " + time )

    
if __name__ == '__main__':
    unittest.main()
