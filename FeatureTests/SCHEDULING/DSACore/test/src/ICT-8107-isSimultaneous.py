import unittest
import urllib2
import xmlrpclib
import json
from testconfig import config
import DsaWebServiceTestCase
    
class ICT8107isSimultaneous(DsaWebServiceTestCase.DsaWebServiceTestCase):
        
    def test_checkColumnPresent(self):
        '''
        ICT-8107: Check is a response from the server for a run() includes a column named isSimultaneous12m7m
        '''
        array_kind = "TWELVE-M"
        array_id = 'C40-1'
        bands = ('ALMA_RB_03', 'ALMA_RB_04', 'ALMA_RB_06',
                'ALMA_RB_07', 'ALMA_RB_08', 'ALMA_RB_09',
                'ALMA_RB_10')
        horizon = 30
        minha = -3
        maxha = 3
        pwv = 0.01
        freqrms30 = 900
        freqrms45 = 900
        timestring = '2017-04-10 23:00:00'
        response = self.server.run( array_kind, bands, array_id, horizon, minha, maxha, pwv, timestring, freqrms30, freqrms45)
        if response == None:
            self.fail("No response from DSACore service, please check that the service is running")
        else:
            j = json.loads(response)
            columnFound=False
            for sb in j.items():
                if 'isSimultaneous12m7m' in sb[1].keys():
                    columnFound = True
            if not columnFound:
                self.fail("Response from DSACore service does not include a isSimultaneous12m7m column. Please check that the correct version is deployed (MAY2017+)")

if __name__ == '__main__':
    unittest.main()
