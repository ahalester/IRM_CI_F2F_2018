import unittest
import urllib2
import xmlrpclib
import json
import ephem
import datetime
from testconfig import config
import DsaAlgorithmTestCase

class ICT9718NewCode2016(DsaAlgorithmTestCase.DsaAlgorithmTestCase):
    '''
    ICT-9718

    Currently DSA only recognize projects 2016.1 and 2016.A as being part of Cycle 4. A new set of projects that need to be observed (most probably in mid-May 2017) will be identified as 2016.2
    This can be corrected by modifying one line of the code.
    Once this is implemented, a patch request will be created to deploy this modification in production.

    To test, we query the libraries, for loading all projects, and checked if any project with code  2016.2 is present or not.
    
    '''
        
    def test_NewCode2016(self):
        if( self.datas.obsproject[self.datas.obsproject['CODE'].str.contains('2016.2')].count()[0] == 0 ):
            self.fail("No CYCLE5 2016.2 projects loaded")

