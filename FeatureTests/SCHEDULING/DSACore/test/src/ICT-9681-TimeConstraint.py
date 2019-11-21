import sys
import unittest
import urllib2
import xmlrpclib
import logging
from datetime import datetime, timedelta
import DsaWebServiceTestCase



class ICT9681TimeConstraint(DsaWebServiceTestCase.DsaWebServiceTestCase):
    
    def test_presence(self):
        pass
    
    def test_elevations(self):
        array_kind = "TWELVE-M"
        array_id = 'C40-1'
        bands = ('ALMA_RB_03', 'ALMA_RB_04', 'ALMA_RB_06',
                 'ALMA_RB_07', 'ALMA_RB_08', 'ALMA_RB_09',
                 'ALMA_RB_10')
        horizon = 20
        minha = -3
        maxha = 3
        pwv = 0.2
        freqrms30 = 900
        freqrms45 = 900
        timenow = datetime.now()
        nine_hours_from_now = datetime.now() + timedelta(hours=28)
        timestring = nine_hours_from_now.strftime('%Y-%m-%d %H:%M:%S')

        response = self.server.run(array_kind, bands, array_id, horizon, minha, maxha, pwv, timestring, freqrms30, freqrms45)
        if response == None:
            self.fail(str(response))
        else:            
            print response


if __name__ == '__main__':
    unittest.main()
