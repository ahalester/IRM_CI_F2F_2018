import os
import time
import unittest
import cx_Oracle
import logging
import DsaDataBase as Data
import DsaAlgorithm as Dsa
import DsaAlgorithmTestCase

class ICT8840AcaShadowing(DsaAlgorithmTestCase.DsaAlgorithmTestCase):

    
    def test_ICT8840AcaShadowing(self):
        '''
        ICT-8840: 
        '''
        obj = Dsa.DsaAlgorithm(
            self.datas, array_kind="SEVEN-M", letterg=["A", "B", "C"], calc_string=True, use_grades=False)
        obj.write_ephem_coords()
        obj.static_param()
        obj.aggregate_dsa()
        obj.query_array()

        obj.selector(prj_status=["Ready", "InProgress", "Phase2Submitted"], sb_status=["Ready", "Phase2Submitted", "Running"], array_id='Array2-BLC')
        obj.master_seldsa_df[['sbName', 'ant_shadow', 'elev', 'bl_ratio', 'num_bl_use', 'HA', 'DEC', 'Exec. Frac']]

    
if __name__ == '__main__':
    unittest.main()
