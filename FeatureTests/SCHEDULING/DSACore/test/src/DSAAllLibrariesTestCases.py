import os
import time
import unittest
import cx_Oracle
import logging
import DsaDataBase as Data
import DsaAlgorithm as Dsa
import DsaAlgorithmTestCase
import ICT10916B6GridMonitoring
import ICT9718NewCode2016
import ICT11152OlderCycleProjects
import ICT8840AcaShadowing
import ICT9988NominalConfig
import ICT12088QA2Info

class DSAAllLibrariesTestCases(
    ICT10916B6GridMonitoring.ICT10916B6GridMonitoring, 
    ICT9718NewCode2016.ICT9718NewCode2016, 
    #ICT11152OlderCycleProjects.ICT11152OlderCycleProjects, 
    #ICT8840AcaShadowing.ICT8840AcaShadowing, 
    ICT9988NominalConfig.ICT9988NominalConfig, 
    ICT12088QA2Info.ICT12088QA2Info 
    ):
    pass

if __name__ == '__main__':
    unittest.main()
