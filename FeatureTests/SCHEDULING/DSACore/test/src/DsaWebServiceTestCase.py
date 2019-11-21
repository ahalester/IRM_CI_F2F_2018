import sys
import unittest
import urllib2
import xmlrpclib
import logging
from testconfig import config


# TODO: Create ICT ticket: DSACore: All error codes and messages through the XMLRPC are the same

# Quick test from console: 
# curl -m 2 --connect-timeout 1 --data '<?xml version="1.0"?><methodCall><methodName>get_pwv</methodName></methodCall>' https://2017nov.asa-test.alma.cl/dsacore/XMLRPC

class DsaWebServiceTestCase(unittest.TestCase):
    proxy = None
    
    def setUp(self):
        self.logger = logging.getLogger(self.__class__.__name__)
        self.logger.info( "Command line: " + str(sys.argv) )
        self.url = config['dsacore_server']['url']
        self.server = xmlrpclib.Server( self.url, allow_none=True )

    def tearDown(self):
        return

    def rpc_call(self, method, *arguments):
        rpc_method = getattr(self.server, method)
        try:
            return rpc_method()
        except xmlrpclib.ProtocolError as err:
            if err.errcode == 404:
                self.skipTest('Error 404: Not Found. Check the server url, most likely it is misspelled.')
            if err.errcode == 502:
                self.skipTest('Error 502: Proxy Error. Please check the httpd containers, and the Apache Proxy servers.')
            if err.errcode == 503:
                self.skipTest('Error 503: Service unavailable. Most likely, DSACore has just been started. Please wait 13 minutes.')
        except xmlrpclib.Fault as fault:
            if fault.faultCode == 502:
                self.skipTest('Error 502: Proxy Error. Please check the httpd containers, and the Apache Proxy servers.')
            if fault.faultCode == 503:
                self.skipTest('Error 503: Service unavailable. Most likely, DSACore has just been started. Please wait 13 minutes.')
            if fault.faultCode == 8001:
                self.skipTest('Error 8001: Remote Procedure with name ' + method + ' not found.')
            self.fail('Method called responded with: FaultCode: ' + repr(fault.faultCode) + 
                      ' FaultString: ' + repr(fault.faultString))
    

    #def test_get_arrays_ar(self):
        ## Test 1: We get all the arrays, and get the Angular Resolution of each one
        #try:
            #self.TwelveMList = self.server.get_arrays('TWELVE-M')
            #self.SevenMList = self.server.get_arrays('SEVEN-M')
            #self.TPList = self.server.get_arrays('TP-Array')
        #except xmlrpclib.Fault as fault:
            #self.fail('get_arrays method in DSACore failed to be executed. ' + 'FC: ' + repr(fault.faultCode) + 'FS: ' + repr(fault.faultString) )
            
        #for i in self.TwelveMList:
            #try:
                #value = self.server.get_ar(i)
            #except xmlrpclib.Fault as fault:
                #self.fail('get_ar method in DSACore failed to be executed, for 12m array: ' + repr(i) + ' which was reported as valid' + 'FC: ' + repr(fault.faultCode) + ' FS: ' + repr(fault.faultString) )
                
        #for j in self.TPList:
            #try:
                #value = self.server.get_ar(j)
            #except xmlrpclib.Fault as fault:
                #self.fail('get_ar method in DSACore failed to be executed, for TP array: ' + repr(j) + ' which was reported as valid' + 'FC: ' + repr(fault.faultCode) + ' FS: ' + repr(fault.faultString) )
        
        #for k in self.SevenMList:
            #try:
                #value = self.server.get_ar(k)
            #except xmlrpclib.Fault as fault:
                #self.fail('get_ar method in DSACore failed to be executed, for 7m array: ' + repr(k) + ' which was reported as valid' + 'FC: ' + repr(fault.faultCode) + ' FS: ' + repr(fault.faultString) )
       
    #def test_get_wrong_arrays_ar(self):

        ## Test 2: We try to get arrays for a non-existent class of arrays.      
        #try:
            #returnValues = self.server.get_arrays('LALA')
            #self.assertEqual(returnValues, 'No Arrays')
            ##self.fail('get_arrays method with LALA as argument returned values')
        #except xmlrpclib.Fault as fault:
            #self.fail("Cannot execute remote method get_arrays()")
            
        ## Test 3: We try to get angular resolution for a non-existent array
        #try:
            #returnValues = self.server.get_arrays('ArrayLALA')
            #self.assertEqual(returnValues, 'No Arrays')
        #except xmlrpclib.Fault as fault:
            #self.fail("Cannot execute remote method get_arrays()")
                        
    #def test_run(self):
        #try:
            #returnValues = self.server.run()
            #print str(returnValues)
            ##self.server.run('TWELVE-M', 'ALMA_RB_03', 'C-34', False, 40, 'Array001', 20, -3, 3, 0.5, '')
        #except xmlrpclib.Fault as fault:
            #self.fail('update_apdm method in DSACore failed to be executed. ' + 'FC: ' + repr(fault.faultCode) + ' FS: ' + repr(fault.faultString) )

    #TODO: Load test for multiples arrays, in parallel

if __name__ == '__main__':
    unittest.main()
