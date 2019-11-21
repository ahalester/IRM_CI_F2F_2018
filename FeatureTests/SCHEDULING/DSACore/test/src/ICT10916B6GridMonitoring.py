import os
import time
import unittest
import cx_Oracle
import logging
import pandas as pd
import datetime
import DsaDataBase as Data
import DsaAlgorithm as Dsa
import DsaAlgorithmTestCase
import DsaCalibration as DCal
import DsaConstants as DsaCon

#GRID_ARR = [
    #['Grid Survey B3,7', '0000.0.00287.CSV', 'uid://A002/X9c637c/X52'],
    #['Grid Survey B3', '0000.0.00287.CSV', 'uid://A002/X9c637c/X53'],
    #['Grid Survey B7', '0000.0.00287.CSV', 'uid://A002/X9c637c/X54'],
    #['Grid Survey B6', '0000.0.00287.CSV', 'uid://A001/X2d6/X32a'],
    #['Grid Survey B3 for PIPELINE', '0000.0.00219.CSV', 'uid://A001/X33e/X19c'],
    #['Grid Survey B6 for PIPELINE', '0000.0.00219.CSV', 'uid://A001/X33e/X19d'],
    #['Grid Survey B7 for PIPELINE', '0000.0.00219.CSV', 'uid://A001/X33e/X19e']
#]


class ICT10916B6GridMonitoring(DsaAlgorithmTestCase.DsaAlgorithmTestCase):

    
    def test_grids(self):
        
        dsa = Dsa.DsaAlgorithm( None, array_kind='SEVEN-M', letterg=["A", "B", "C"], aprc=False )
        grid = DCal.get_latest_grid(days_back=0)
        g_entry = pd.DataFrame( [['uid://A001/X33e/X19c', 'uid://A001/X33e/X19cd', pd.Timestamp('2000-01-01 00:00'), pd.Timestamp('2000-01-01 01:00'), 'Unset', '0000.0.00219.CSV', 200, 'Array005']], columns=grid.columns)
        grid = pd.concat([g_entry, grid])
        #time_obs = pd.Timestamp.now()
        time_obs = pd.Timestamp('2017-12-01 00:00')
                
        for i in range(1,24*2*50):
            if( i < 24*2*12 ):
                freq = 200
            else:
                if( i >= 24*2*12 and i < 24*2*24 ):
                    freq = 300
                else:
                    if( i >= 24*2*24 ):
                        freq = 400
            time_obs = time_obs + datetime.timedelta(hours=0.5)
            dsa.set_time(time_obs)
            lst = pd.np.rad2deg(dsa.ALMA_ephem.sidereal_time()) / 15.
            grid_track = DCal.get_grid_track(grid, dateob=time_obs)
            dogrid = DCal.do_grid_cal(grid_track, lst, 0.0, freq)
            g_entry = pd.DataFrame([[dogrid[4], 'uid://dummyEB', time_obs, time_obs + pd.Timedelta(0.3, 'h'), 'Unset', dogrid[3], 9999, 'DummyArray']], columns=grid.columns, index=[grid.shape[0]])
            grid = pd.concat([g_entry, grid])
        self.logger.info(str(grid_track))

        
        
        
        

        
        
    #def test_b6grid(self):
        # We obtain the grid EB from previous run. We should delete them all, 
        #  and construct our own history, but we have to start somewhere :D
        #grid = DCal.get_latest_grid()
        #newGrid = grid[grid.SB_UID == 0]
        
        #newEntry = pd.DataFrame([['uid://A001/X33e/X19c', 'uid://A001/X33e/X19cd', pd.Timestamp('2000-01-01 00:00'), pd.Timestamp('2000-01-01 01:00'), 'Unset', '0000.0.00219.CSV', 200, 'Array005']], columns=grid.columns)
        #newGrid = pd.concat([newEntry, newGrid])

        
        
        #obj = Dsa.DsaAlgorithm(
            #self.datas, array_kind="SEVEN-M", letterg=["A", "B", "C"], calc_string=True, use_grades=False)
        #obj.write_ephem_coords()
        #obj.static_param()
        #obj.aggregate_dsa()
        #obj.query_array()

        #obj.selector(prj_status=["Ready", "InProgress", "Phase2Submitted"], sb_status=["Ready", "Phase2Submitted", "Running"], array_id='Array2-BLC')
        #obj.master_seldsa_df[['sbName', 'ant_shadow', 'elev', 'bl_ratio', 'num_bl_use', 'HA', 'DEC', 'Exec. Frac']]

    
if __name__ == '__main__':
    unittest.main()
    
