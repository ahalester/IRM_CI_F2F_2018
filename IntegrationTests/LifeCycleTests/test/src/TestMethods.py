__author__ = 'scornejo'
import unittest
import myConfigParser
import dbConection
import comparator
import viewVsXMLQueries
import schedVsOnlineQueries

import sys
import datetime
from time import strftime, gmtime

import logging
import logging.config

##logger defined for jenkins
logging.config.fileConfig('../config/logging.conf', defaults=None, disable_existing_loggers=False)
logger = logging.getLogger(__name__)
stream_handler = logging.StreamHandler(sys.stdout)
logger.addHandler(stream_handler)


class TestMethods (unittest.TestCase):
    connectionDetails = myConfigParser.parseConfig('../config/LifeCycleTests.conf')
    filename = '../LOGS/myReport-' + strftime("%Y%m%dT%H%M%S ", gmtime()).replace(' ', '') + '.log'


    def test_SCOconnection(self):
        # SCO ALMA Strings
        dbDetail = 'SCO'
        SCOuser = TestMethods.connectionDetails[dbDetail]['user']
        SCOpassword = TestMethods.connectionDetails[dbDetail]['password']
        SCOhost = TestMethods.connectionDetails[dbDetail]['host']
        SCOport = TestMethods.connectionDetails[dbDetail]['port']
        SCOinstance = TestMethods.connectionDetails[dbDetail]['instance']

        t0 = datetime.datetime.utcnow()
        SCOdb = dbConection.dbConection(SCOuser, SCOpassword, SCOhost, SCOport, SCOinstance)
        t1 = datetime.datetime.utcnow()
        delta = (t1 - t0)
        logger.info('Get data from SCO took ' + str((delta.microseconds + 0.0 + (delta.seconds + delta.days *
                                                                                        24 * 3600) * 10 ** 6) / 10 ** 6)
                    + ' seconds.')
        assert str(SCOdb.version) == '11.2.0.4.0', 'Incorrect DB version. Either your code needs an update, or we' \
                                                    ' could not connect to ' + str(SCOdb)
        return SCOdb



    def test_OSFConnection(self):
        ## OSF  ALMA Strings
        dbDetail = 'OSF'
        OSFuser = TestMethods.connectionDetails[dbDetail]['user']
        OSFpassword = TestMethods.connectionDetails[dbDetail]['password']
        OSFhost = TestMethods.connectionDetails[dbDetail]['host']
        OSFport = TestMethods.connectionDetails[dbDetail]['port']
        OSFinstance = TestMethods.connectionDetails[dbDetail]['instance']

        t0 = datetime.datetime.utcnow()
        OSFdb = dbConection.dbConection(OSFuser, OSFpassword, OSFhost, OSFport, OSFinstance)
        t1 = datetime.datetime.utcnow()
        delta = (t1 - t0)
        logger.info('Get data from OSF took ' + str((delta.microseconds + 0.0 + (delta.seconds + delta.days *
                                                                    24 * 3600) * 10 ** 6) / 10 ** 6) + ' seconds.')

        assert str(OSFdb.version) == '11.2.0.4.0', 'Incorrect DB version. Either your code needs an update, or we' \
                                                    ' could not connect to ' + dbDetail
        return OSFdb


    def test_SCHEDConnection(self):
        ## OSF SCHEDULING Strings
        dbDetail = 'SCHED'
        SCHEDuser = TestMethods.connectionDetails[dbDetail]['user']
        SCHEDpassword = TestMethods.connectionDetails[dbDetail]['password']
        SCHEDhost = TestMethods.connectionDetails[dbDetail]['host']
        SCHEDport = TestMethods.connectionDetails[dbDetail]['port']
        SCHEDinstance = TestMethods.connectionDetails[dbDetail]['instance']
        SCHEDdb = dbConection.dbConection(SCHEDuser, SCHEDpassword, SCHEDhost, SCHEDport, SCHEDinstance)

        t0 = datetime.datetime.utcnow()
        assert str(SCHEDdb.version) == '11.2.0.4.0', 'Incorrect DB version. Either your code needs an update, or we' \
                                                    ' could not connect to ' + dbDetail
        t1 = datetime.datetime.utcnow()
        delta = (t1 - t0)
        logger.info('Get data from SCHED took ' + str((delta.microseconds + 0.0 + (delta.seconds + delta.days *
                                                                     24 * 3600) * 10 ** 6) / 10 ** 6) + ' seconds.')
        return SCHEDdb


    def test_viewVsXMLSCO(self):
        #Let's check at SCO
        dbDetail = 'SCO'
        SCOuser = TestMethods.connectionDetails[dbDetail]['user']
        SCOpassword = TestMethods.connectionDetails[dbDetail]['password']
        SCOhost = TestMethods.connectionDetails[dbDetail]['host']
        SCOport = TestMethods.connectionDetails[dbDetail]['port']
        SCOinstance = TestMethods.connectionDetails[dbDetail]['instance']
        viewVsXML_atSCO = viewVsXMLQueries.compareViewVsXML(SCOuser, SCOpassword, SCOhost, SCOport, SCOinstance)
        if len(viewVsXML_atSCO) > 0:
            target = open(TestMethods.filename, 'a+')
            target.write('##########################################################################################\n')
            target.write('##########################################################################################\n')
            target.write('                        Differences between view and XML at SCO                           \n')
            target.write('##########################################################################################\n')
            target.write('##########################################################################################\n')
            logger.warning('There are ' + str(len(viewVsXML_atSCO)) + ' inconsistencies, stored at ' +
                           TestMethods.filename)
            for inconsistency in viewVsXML_atSCO:
                target.write(str(inconsistency))
                target.write('\n')
        assert len(viewVsXML_atSCO) == 0, "@SCO: About inconsistencies: On the contrary. I'm possibly more or less" \
                                          " not definitely rejecting the idea that in no way with any amount " \
                                          "of uncertainty that I undeniably do or do not know where there shouldn't" \
                                          " probably be, if that indeed wasn't where there isn't any such" \
                                          " inconsistency: There are " + str(len(viewVsXML_atSCO)) + " inconsistencies"


    def test_viewVsXMLOSF(self):
        #Let's check at OSF
        dbDetail = 'OSF'
        OSFuser = TestMethods.connectionDetails[dbDetail]['user']
        OSFpassword = TestMethods.connectionDetails[dbDetail]['password']
        OSFhost = TestMethods.connectionDetails[dbDetail]['host']
        OSFport = TestMethods.connectionDetails[dbDetail]['port']
        OSFinstance = TestMethods.connectionDetails[dbDetail]['instance']
        viewVsXML_atOSF = viewVsXMLQueries.compareViewVsXML(OSFuser, OSFpassword, OSFhost, OSFport, OSFinstance)
        if len(viewVsXML_atOSF) > 0:
            target = open(TestMethods.filename, 'a+')
            target.write('##########################################################################################\n')
            target.write('##########################################################################################\n')
            target.write('                        Differences between view and XML at OSF                           \n')
            target.write('##########################################################################################\n')
            target.write('##########################################################################################\n')
            logger.warning('There are ' + str(len(viewVsXML_atOSF)) + ' inconsistencies, stored at ' +
                           TestMethods.filename)
            for inconsistency in viewVsXML_atOSF:
                target.write(str(inconsistency))
                target.write('\n')

        assert len(viewVsXML_atOSF) == 0, "@OSF: About inconsistencies: On the contrary. I'm possibly more or less" \
                                          " not definitely rejecting the idea that in no way with any amount " \
                                          "of uncertainty that I undeniably do or do not know where there shouldn't" \
                                          " probably be, if that indeed wasn't where there isn't any such" \
                                          " inconsistency: There are " + str(len(viewVsXML_atOSF)) + " inconsistencies"


    def test_schedVsOnline(self):

        ## OSF Data
        dbDetail = 'OSF'
        OSFuser = TestMethods.connectionDetails[dbDetail]['user']
        OSFpassword = TestMethods.connectionDetails[dbDetail]['password']
        OSFhost = TestMethods.connectionDetails[dbDetail]['host']
        OSFport = TestMethods.connectionDetails[dbDetail]['port']
        OSFinstance = TestMethods.connectionDetails[dbDetail]['instance']

        t0 = datetime.datetime.utcnow()
        OSFList = viewVsXMLQueries.getViewVsXML(OSFuser, OSFpassword, OSFhost, OSFport, OSFinstance)
        t1 = datetime.datetime.utcnow()
        delta = (t1 - t0)
        logger.info('Get data from OSF took ' + str((delta.microseconds + 0.0 + (delta.seconds + delta.days *
                                                                    24 * 3600) * 10 ** 6) / 10 ** 6) + ' seconds.')

        ## Sched Data
        ## OSF SCHEDULING Strings
        dbDetail = 'SCHED'
        SCHEDuser = TestMethods.connectionDetails[dbDetail]['user']
        SCHEDpassword = TestMethods.connectionDetails[dbDetail]['password']
        SCHEDhost = TestMethods.connectionDetails[dbDetail]['host']
        SCHEDport = TestMethods.connectionDetails[dbDetail]['port']
        SCHEDinstance = TestMethods.connectionDetails[dbDetail]['instance']

        t0 = datetime.datetime.utcnow()
        SchedulingList = schedVsOnlineQueries.getSchedStatus(SCHEDuser, SCHEDpassword, SCHEDhost, SCHEDport,SCHEDinstance)
        t1 = datetime.datetime.utcnow()
        delta = (t1 - t0)
        logger.info('Get data from Scheduling took ' + str((delta.microseconds + 0.0 + (delta.seconds + delta.days *
                                                                    24 * 3600) * 10 ** 6) / 10 ** 6) + ' seconds.')


        t0 = datetime.datetime.utcnow()
        myListOfInconsistenAlmaSched = comparator.XMLVsScheduling(OSFList, SchedulingList)
        t1 = datetime.datetime.utcnow()
        delta = (t1 - t0)
        logger.info('Comparation between ALMA and Sched took ' + str((delta.microseconds + 0.0
                                                                      + (delta.seconds + delta.days * 24 * 3600) * 10
                                                                      ** 6) / 10 ** 6) + ' seconds.')
        if len(myListOfInconsistenAlmaSched) > 0:
            # filename = '../LOGS/myReport-' + strftime("%Y%m%dT%H%M%S ", gmtime()).replace(' ', '') + '.log'
            target = open(TestMethods.filename, 'a+')
            logger.warning('There are ' + str(len(myListOfInconsistenAlmaSched)) + ' inconsistencies, stored at '
                           + TestMethods.filename)
            target.write('##########################################################################################\n')
            target.write('##########################################################################################\n')
            target.write('                        Differences between Scheduling DB and Online DB                   \n')
            target.write('##########################################################################################\n')
            target.write('##########################################################################################\n')
            for inconsistency in myListOfInconsistenAlmaSched:
                target.write(str(inconsistency))
                target.write('\n')
        assert len(myListOfInconsistenAlmaSched) == 0, 'Issues stored at ' + TestMethods.filename


if __name__ == '__main__':
    logging.config.fileConfig('../config/logging.conf', defaults=None, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    unittest.main()