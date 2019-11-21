import unittest
import urllib2
import xmlrpclib
import json
import ephem
import datetime
import math
from testconfig import config
import DsaWebServiceTestCase

    
class ICT8841Elevation(DsaWebServiceTestCase.DsaWebServiceTestCase):
    
    def test_all_sky(self):
        '''
        ICT-8841 and ICT-12107.
        The first ticket ensure lower elevation limit.
        The second ticket is for upper elevation limit.
        The test case goes around the day, every 2 LST, and runs a simulation.
        TODO: Program test cases to set apart two days by 6 month, on the solstice.
        '''
        var_time = datetime.datetime.utcnow()
        delta = datetime.timedelta(hours=2)        
        for i in range(1,24/2):
            self.run_simulation(var_time)
            var_time = (var_time + delta)
            
    
    def run_simulation(self, var_time):
        array_kind = "TWELVE-M"
        array_id = 'C40-1'
        bands = ('ALMA_RB_03', 'ALMA_RB_04', 'ALMA_RB_06',
                'ALMA_RB_07', 'ALMA_RB_08', 'ALMA_RB_09',
                'ALMA_RB_10')
        horizon = 20
        horizon_str = str(horizon) + ':00:00.0'
        elevation_limit=88
        minha = -3
        maxha = 3
        pwv = 0.1
        freqrms30 = 900
        freqrms45 = 900
        timenow = var_time
        timestring = timenow.strftime('%Y-%m-%d %H:%M:%S')
        response = self.server.run( array_kind, bands, array_id, horizon, minha, maxha, pwv, timestring, freqrms30, freqrms45)

        alma = ephem.Observer()
        alma.long = '-67.7551257'
        alma.lat = '-23.0262015'
        alma.elevation = 5060
        alma.date = timestring
        alma.horizon = horizon_str
        #elevation_limit_hours_int, elevation_limit_hours_decimal = math.modf(float(elevation_limit)*6/90)
        #elevation_limit_hours_decimal = 60 / elevation_limit_hours_decimal
        #elevation_limit_hours=str(elevation_limit_hours_int) + ':' + str(round(elevation_limit_hours_decimal,2)) + ':00.0'
        elevation_limit_hours = str(elevation_limit)+':00:00.0'
        
        self.logger.info('Lower Elevation limit: ' + str(ephem.degrees(horizon_str)) )
        self.logger.info('Upper Elevation limit: ' + str(ephem.degrees(elevation_limit_hours)) )

        
        if response == None:
            self.fail("Response empty")
        else:    
            j = json.loads(response)
            noteFound=False
            for sb in j.items():
                body = ephem.FixedBody()
                body._ra = ephem.degrees(float(sb[1]["RA"]) * ephem.degree)
                body._dec = ephem.degrees(float(sb[1]["DEC"]) * ephem.degree)
                body._epoch = '2000'
                body.compute(alma)
                if "Focus" in str(sb[1]["sbName"]):
                    continue
                if( float(sb[1]["estTimeOr"]) == 0 or float(sb[1]["EXECOUNT"]) == 0):
                    continue
                exectimePerEB = float(sb[1]["estTimeOr"]) / float(sb[1]["EXECOUNT"])
                delta = datetime.timedelta(hours=exectimePerEB)                
                
                # Source is never up
                if body.neverup:
                    self.fail( timenow.strftime('%Y-%m-%d %H:%M:%S') + 
                              " SB: " + str(sb[1]["sbName"]) + 
                              " with coordinates RA: " + str(body._ra)  + "  " + str(sb[1]["RA"]) + " and DEC: " + str(body._dec) + " " + str(sb[1]["DEC"]) +
                              " is always below the horizon (" + 
                              str(horizon) + ")" )
                # Source is below lower limit at the start of the observation
                if body.alt < ephem.degrees(horizon_str):
                    self.fail( timenow.strftime('%Y-%m-%d %H:%M:%S') + 
                              " At the start of observation, SB: " + 
                              str(sb[1]["sbName"]) + 
                              " with coordinates RA: " + str(body._ra)  + "  " + str(sb[1]["RA"]) + " and DEC: " + str(body._dec) + " " + str(sb[1]["DEC"]) +
                              " is below horizon (" + 
                              str(ephem.degrees(horizon_str)) + "): " + 
                              str(body.alt)  )
                # Source is above upper limit at the start of the observation
                # We only log, this, as the implementation of ICT-12107 does not exclude them from the selector
                if body.alt > ephem.degrees(elevation_limit_hours):
                    self.logger.warning( timenow.strftime('%Y-%m-%d %H:%M:%S') + 
                              " At the start of observation, SB: " + 
                              str(sb[1]["sbName"]) + 
                              " with coordinates RA: " + str(body._ra)  + "  " + str(sb[1]["RA"]) + " and DEC: " + str(body._dec) + " " + str(sb[1]["DEC"]) +
                              " is above elevation limit (" + 
                              str(ephem.degrees(elevation_limit_hours)) + "): " + 
                              str(body.alt)  )
                    if sb[1]["selAvoidZen"] == False:
                        self.fail("SB: " + str(sb[1]["sbName"]) + "fails due to being higher that the elevation upper limit and selAvoidZen column was False. Check logs.")
                alma.date = (timenow + delta).strftime('%Y-%m-%d %H:%M:%S')
                body.compute(alma)
                # Source is below lower limit at the end of the observation
                if body.alt < ephem.degrees(horizon_str):
                    self.fail( timenow.strftime('%Y-%m-%d %H:%M:%S') + 
                              " At the end of observation, SB: " + 
                              str(sb[1]["sbName"]) + 
                              " with coordinates RA: " + str(body._ra)  + "  " + str(sb[1]["RA"]) + " and DEC: " + str(body._dec) + " " + str(sb[1]["DEC"]) +
                              " is below horizon (" + 
                              str(ephem.degrees(horizon_str)) + "): " + 
                              str(body.alt)  )
                # Source is above upper limit at the end of the observation
                # We only log, this, as the implementation of ICT-12107 does not exclude them from the selector
                if body.alt > ephem.degrees(elevation_limit_hours):
                    self.logger.warning( timenow.strftime('%Y-%m-%d %H:%M:%S') + 
                              " At the end of observation, SB: " + 
                              str(sb[1]["sbName"]) + 
                              " with coordinates RA: " + str(body._ra)  + "  " + str(sb[1]["RA"]) + " and DEC: " + str(body._dec) + " " + str(sb[1]["DEC"]) +
                              " is above elevation limit (" + 
                              str(ephem.degrees(elevation_limit_hours)) + "): " + 
                              str(body.alt)  )
                    if sb[1]["selAvoidZen"] == False:
                        self.fail("SB: " + str(sb[1]["sbName"]) + "fails due to being higher that the elevation upper limit and selAvoidZen column was False. Check logs.")
            
if __name__ == '__main__':
    unittest.main()
