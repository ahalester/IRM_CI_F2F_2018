import os
import time
import unittest
import cx_Oracle
import logging
import DsaDataBase as Data
import DsaAlgorithm as Dsa
import DsaAlgorithmTestCase

class ICT11152OlderCycleProjects(DsaAlgorithmTestCase.DsaAlgorithmTestCase):
    
    def test_OlderCycleProjects(self):
        if( self.datas.obsproject[self.datas.obsproject['CODE'].str.contains('2015.1')].count()[0] == 0 ):
            self.fail("No CYCLE3 projects loaded")
        if( self.datas.obsproject[self.datas.obsproject['CODE'].str.contains('2016.1')].count()[0] == 0 ):
            self.fail("No CYCLE4 projects loaded")
    
if __name__ == '__main__':
    unittest.main()
