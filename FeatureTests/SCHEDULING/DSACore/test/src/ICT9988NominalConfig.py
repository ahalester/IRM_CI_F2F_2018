import DsaAlgorithmTestCase
import unittest
    
class ICT9988NominalConfig(DsaAlgorithmTestCase.DsaAlgorithmTestCase):
    '''
    Test if the Nominal Configuration is being loaded. Checks for presence of nomicalConf in the database results from the Libraries, not the server.
    '''

    def test_nominalConf(self):
        if not any("nominalConf" in s for s in self.datas.schedblocks.columns.values):
            self.fail("Test fail because of absence of nominalConf column in data.schedbocks DataFrame")
            
if __name__ == '__main__':
    unittest.main()
