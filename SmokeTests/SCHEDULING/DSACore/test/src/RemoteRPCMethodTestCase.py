import sys
import unittest
import urllib2
import xmlrpclib
from testconfig import config


# TODO: Create ICT ticket: DSACore: All error codes and messages through the XMLRPC are the same

class RemoteRPCMethodTestCase(unittest.TestCase):
    proxy = None
    
    def setUp(self):

        print repr(sys.argv[0])
        self.url = config['dsacore_server']['url']
        self.server = xmlrpclib.Server(self.url,allow_none=True)

    def tearDown(self):
        return

    #TODO: Load test for PVW values, in parallel
    def test_get_pvw(self):
        values = self.server.get_pwv()
        self.assertTrue( float(values[0]) > 0.0, 'PWV value #1 is less than 0.0. ' + 'Current PWV value: ' + repr(values[0]))
        self.assertTrue( float(values[0]) < 150.0, 'PWV value #1 is greather than 150.0. ' + 'Current PWV value: ' + repr(values[0]))
        
        self.assertTrue( float(values[1]) > 0.0, 'PWV value #2 is less than 0.0. ' + 'Current PWV value: ' + repr(values[1]))
        self.assertTrue( float(values[1]) < 3000.0, 'PWV value #2 is greather than 3000.0. ' + 'Current PWV value: ' + repr(values[1]))


        self.assertTrue( float(values[2]) > 0.0, 'PWV value #3 is less than 0.0. ' + 'Current PWV value: ' + repr(values[2]))
        self.assertTrue( float(values[2]) < 5500.0, 'PWV value #3 is greather than 5500.0. ' + 'Current PWV value: ' + repr(values[2]))


            
    def test_get_arrays_ar(self):
        # Test 1: We get all the arrays, and get the Angular Resolution of each one
        try:
            self.TwelveMList = self.server.get_arrays('TWELVE-M')
            self.SevenMList = self.server.get_arrays('SEVEN-M')
            self.TPList = self.server.get_arrays('TP-Array')
        except xmlrpclib.Fault as fault:
            self.fail('get_arrays method in DSACore failed to be executed. ' + 'FC: ' + repr(fault.faultCode) + 'FS: ' + repr(fault.faultString) )
            
        for i in self.TwelveMList:
            try:
                value = self.server.get_ar(i)
            except xmlrpclib.Fault as fault:
                self.fail('get_ar method in DSACore failed to be executed, for 12m array: ' + repr(i) + ' which was reported as valid' + 'FC: ' + repr(fault.faultCode) + ' FS: ' + repr(fault.faultString) )
                
        for j in self.TPList:
            try:
                value = self.server.get_ar(j)
            except xmlrpclib.Fault as fault:
                self.fail('get_ar method in DSACore failed to be executed, for TP array: ' + repr(j) + ' which was reported as valid' + 'FC: ' + repr(fault.faultCode) + ' FS: ' + repr(fault.faultString) )
        
        for k in self.SevenMList:
            try:
                value = self.server.get_ar(k)
            except xmlrpclib.Fault as fault:
                self.fail('get_ar method in DSACore failed to be executed, for 7m array: ' + repr(k) + ' which was reported as valid' + 'FC: ' + repr(fault.faultCode) + ' FS: ' + repr(fault.faultString) )
       
    def test_get_wrong_arrays_ar(self):

        # Test 2: We try to get arrays for a non-existent class of arrays.      
        try:
            returnValues = self.server.get_arrays('LALA')
            noarrays = False
            if returnValues == 'No Arrays' or returnValues == []:
                noarrays = True
            self.assertTrue(noarrays)
            #self.fail('get_arrays method with LALA as argument returned values')
        except xmlrpclib.Fault as fault:
            self.fail("Cannot execute remote method get_arrays()")
            
        # Test 3: We try to get angular resolution for a non-existent array
        try:
            returnValues = self.server.get_arrays('ArrayLALA')
            if returnValues == 'No Arrays' or returnValues == []:
                noarrays = True
            self.assertTrue(noarrays)

        except xmlrpclib.Fault as fault:
            self.fail("Cannot execute remote method get_arrays()")
                        
    def test_run(self):
        try:
            returnValues = self.server.run()
            print str(returnValues)
            #self.server.run('TWELVE-M', 'ALMA_RB_03', 'C-34', False, 40, 'Array001', 20, -3, 3, 0.5, '')
        except xmlrpclib.Fault as fault:
            self.fail('update_apdm method in DSACore failed to be executed. ' + 'FC: ' + repr(fault.faultCode) + ' FS: ' + repr(fault.faultString) )

    #TODO: Load test for multiples arrays, in parallel

