import unittest
import urllib2
import xmlrpclib
import json
import logging
import sys
from datetime import datetime, timedelta
from testconfig import config
import DsaWebServiceTestCase
    
class ICT11731ExtraIsSimultaneousFields(DsaWebServiceTestCase.DsaWebServiceTestCase):
        
    def test_checkColumnsPresent(self):
        '''
        ICT-11731: Check is a response from the server for a run() includes a columns is_simultaneous and simultaneous_sbs
        '''
        
        self.logger.level = logging.DEBUG
        stream_handler = logging.StreamHandler(sys.stdout)
        self.logger.addHandler(stream_handler)

        array_kind = "TWELVE-M"
        array_id = 'C43-1'
        bands = ('ALMA_RB_03', 'ALMA_RB_04', 'ALMA_RB_06',
                'ALMA_RB_07', 'ALMA_RB_08', 'ALMA_RB_09',
                'ALMA_RB_10')
        horizon = 30
        minha = -3
        maxha = 3
        pwv = 0.01
        freqrms30 = 900
        freqrms45 = 900
        timenow = datetime.now()
        one_hour_from_now = datetime.now() + timedelta(hours=1)
        timestring = one_hour_from_now.strftime('%Y-%m-%d %H:%M:%S')
        response = self.server.run( array_kind, bands, array_id, horizon, minha, maxha, pwv, timestring, freqrms30, freqrms45)
        if response == None:
            self.fail("No response from DSACore service, please check that the service is running")
        else:
            self.logger.info(str(response))
            
            j = json.loads(response)
            column_is_simultaneous=False
            column_simultaneous_sbs=False
            for sb in j.items():
                if 'is_simultaneous' in sb[1].keys():
                    column_is_simultaneous = True
            for sb in j.items():
                if 'simultaneous_sbs' in sb[1].keys():
                    column_simultaneous_sbs = True

            if not column_is_simultaneous:
                self.fail("Response from DSACore service does not include a 'is_simultaneous' column. Please check that the correct version is deployed (SPT-2018MAR+)")
            if not column_simultaneous_sbs:
                self.fail("Response from DSACore service does not include a 'simultaneous_sbs' column. Please check that the correct version is deployed (SPT-2018MAR+)")

if __name__ == '__main__':
    unittest.main()


