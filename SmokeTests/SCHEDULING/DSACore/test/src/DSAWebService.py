import unittest
import urllib2
import xmlrpclib
from testconfig import config


# TODO: Create ICT ticket: DSACore: All error codes and messages through the XMLRPC are the same

class Test04(unittest.TestCase):
  proxy = None
  
  def setUp(self):
    self.url = config['dsacore_server']['url']
    self.server = xmlrpclib.Server(self.url,allow_none=True)
    
  def tearDown(self):
    return

  def test_run(self):
    print('Executing run method in server')
    array_kind = "TWELVE-M"
    values = 'No results at the moment'
    
    try:
        values = self.server.run(array_kind)
      
    except xmlrpclib.Fault as fault:
        self.fail('run method in DSACore failed to be executed. ' + 'FC: ' + repr(fault.faultCode) + ' FS: ' + repr(fault.faultString) )
	pass
    
    self.assertNotEqual( values, 'No results at the moment' )
    print values
    print(repr(values))
    print('Finished executing run method')

  
  def test_run_12m(self):
    print('Executing run method in server')
    array_kind = "TWELVE-M"
    bands = ('ALMA_RB_03', 'ALMA_RB_04', 'ALMA_RB_06', 'ALMA_RB_07', 'ALMA_RB_08', 'ALMA_RB_09', 'ALMA_RB_10')
    array_id = 'C40-4'
    horizon = 30
    minha = -3
    maxha = 3
    pwv = 0.1
    freqrms30 = 300
    freqrms45 = 900
    timestring = ''
    values = 'No results at the moment'
    
    try:
        values = self.server.run(array_kind, bands, array_id, horizon, minha, maxha, pwv, timestring, freqrms30, freqrms45)
      
    except xmlrpclib.Fault as fault:
        self.fail('run method in DSACore failed to be executed. ' + 'FC: ' + repr(fault.faultCode) + ' FS: ' + repr(fault.faultString) )
    
    self.assertNotEqual( values, 'No results at the moment' )
    self.assertNotEqual( values, None )
    self.assertNotEqual( values, '' )
    print values
    print(repr(values))
    print('Finished executing run method')
    
  def test_run_7m(self):
    print('Executing run method in server')
    array_kind = "SEVEN-M"
    bands = ('ALMA_RB_03', 'ALMA_RB_04', 'ALMA_RB_06', 'ALMA_RB_07', 'ALMA_RB_08', 'ALMA_RB_09', 'ALMA_RB_10')
    array_id = 'C40-4'
    horizon = 30
    minha = -3
    maxha = 3
    pwv = 0.1
    freqrms30 = 300
    freqrms45 = 900
    timestring = ''
    values = 'No results at the moment'
    
    try:
        values = self.server.run(array_kind, bands, array_id, horizon, minha, maxha, pwv, timestring, freqrms30, freqrms45)
      
    except xmlrpclib.Fault as fault:
        self.fail('run method in DSACore failed to be executed. ' + 'FC: ' + repr(fault.faultCode) + ' FS: ' + repr(fault.faultString) )
    
    self.assertNotEqual( values, 'No results at the moment' )
    self.assertNotEqual( values, None )
    self.assertNotEqual( values, '' )
    print values
    print(repr(values))
    print('Finished executing run method')

  def test_run_tp(self):
    print('Executing run method in server')
    array_kind = "TP-Array"
    bands = ('ALMA_RB_03', 'ALMA_RB_04', 'ALMA_RB_06', 'ALMA_RB_07', 'ALMA_RB_08', 'ALMA_RB_09', 'ALMA_RB_10')
    array_id = 'C40-4'
    horizon = 30
    minha = -3
    maxha = 3
    pwv = 0.1
    freqrms30 = 300
    freqrms45 = 900
    timestring = ''
    values = 'No results at the moment'
    
    try:
        values = self.server.run(array_kind, bands, array_id, horizon, minha, maxha, pwv, timestring, freqrms30, freqrms45)
      
    except xmlrpclib.Fault as fault:
        self.fail('run method in DSACore failed to be executed. ' + 'FC: ' + repr(fault.faultCode) + ' FS: ' + repr(fault.faultString) )
    
    self.assertNotEqual( values, 'No results at the moment' )
    self.assertNotEqual( values, None )
    self.assertNotEqual( values, '' )
    print values
    print(repr(values))
    print('Finished executing run method')

	  
	  
