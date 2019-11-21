import unittest
import urllib2
import xmlrpclib
from testconfig import config
    
class SanityCheckTestCase(unittest.TestCase):
    proxy = None
    
    def setUp(self):
        self.url = config['dsacore_server']['url']

    def tearDown(self):
        return
    
    def test_00_internet_connection(self):
        try:
            urllib2.urlopen("http://google.com")
        except urllib2.URLError as e:
            self.fail(e.reason)
        except urllib2.HTTPError as e:
            self.fail(e.reason)
        except Exception as e:
            self.fail("No internet connection, please check outboud connectivity")
    
    def test_01_webpage_existence(self):
        try:
            urllib2.urlopen(self.url)
#        except urllib2.URLError as e:
#            self.fail(e.reason)
        except urllib2.HTTPError as e:
            if e.code == 405:
                pass
            else:
                self.fail("Error code: " + str(e.code) + " from http request answer")
            pass
        except Exception as e:
            self.fail("Could not open the url, please check that the service is up")
