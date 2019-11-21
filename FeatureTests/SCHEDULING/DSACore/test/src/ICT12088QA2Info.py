import os
import time
import unittest
import cx_Oracle
import logging
import DsaDataBase as Data
import DsaAlgorithm as Dsa
import DsaAlgorithmTestCase
from DataTools import get_antennas_from_pads


class ICT12088QA2Info(DsaAlgorithmTestCase.DsaAlgorithmTestCase):

    
    def test_qa2info(self):
        upath = os.getenv('HOME') + '/'
        self.datas.create_ebarray_history(cache=False, user_path=upath)

        con, cur = self.datas.open_oracle_conn()
        eb_uid = self.datas.array_ebs.iloc[0].name[1]
        pad_list = self.datas.array_ebs.iloc[0].pads
        antennas = get_antennas_from_pads(eb_uid, pad_list, cur)
        cur.close()
        con.close()

        assert type(antennas) == list
        assert len(antennas) == len(pad_list)

    
if __name__ == '__main__':
    unittest.main()
