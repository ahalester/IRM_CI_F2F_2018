import unittest
import urllib2
import xmlrpclib
import json
import ephem
import datetime
from testconfig import config
import DsaWebServiceTestCase

    
class ICT9922EmptyAnswer(DsaWebServiceTestCase.DsaWebServiceTestCase):
    '''
    ICT-9922

    While helping Sergio Martin with the phase C testing, it was noticed that an error was returned whenever no SBs are available to be queued.
    The error was introduced when adding the temporal constraints and setting selectors.
    This was already fixed in the SPT-MAY2017 release.

    To test, we will force strong scheduling conditions so that the answers is empty.
    
    TODO: Find a way to force non scheduling of Focus and New Antenna SBs.
    '''
    def test_emptyanswer(self):
        array_kind = "TWELVE-M"
        array_id = 'C40-1'
        bands = ('ALMA_RB_10')
        horizon = 80
        minha = -1
        maxha = 1
        pwv = 10.0
        freqrms30 = 1600
        freqrms45 = 1600
        timenow = datetime.datetime.now()
        timestring = timenow.strftime('%Y-%m-%d %H:%M:%S')

        response = self.server.run( array_kind, bands, array_id, horizon, minha, maxha, pwv, timestring, freqrms30, freqrms45)
        if response == None:
            self.fail("The Response was empty. It should at least return an empty json.")
        try:
            j = json.loads(response)
        except ValueError as e:
            self.logger.debug( "Json response: " + j )
            self.fail("Json response could not be decoded")
        
            

