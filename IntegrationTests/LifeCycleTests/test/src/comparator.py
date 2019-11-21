__author__ = 'scornejo'
import datetime
import logging
import logging.config
import myConfigParser

from time import strftime, gmtime

from viewVsXMLQueries import getViewVsXML, compareViewVsXML, getViewVsXMLTest
from schedVsOnlineQueries import getSchedStatus, getSchedStatusTEST

logger = logging.getLogger(__name__)

def XMLVsScheduling(XMLlist, SchedulingList):
    """

    :param XMLlist: the list of SBs retrieved from XMLs (ALMA SCHEMA)
    :param SchedulingList:  the list of SBs retrieved from Scheduling DB
    :return:
    """
    AlmaSchedInconsistencies = []
    logger.info('comparing between XML statuses and Scheduling db Statuses')
    for fromXML in XMLlist:
        for fromScheduling in SchedulingList:
            if fromXML['PRJ_UID'] == fromScheduling['PRJ_UID']:
                if fromXML['SB_UID'] == fromScheduling['SB_UID']:
                    if fromXML['VIEW_STATUS_FOR_SCHEDULINGDB'] != fromScheduling['SCHEDBLOCK_CTRL_STATE']:
                        logger.warn('Project ' + fromXML['CODE'] + ' (' + fromXML['PRJ_UID'] +
                                    ') has an issue on SB ' + fromXML['SB_UID'] + ': Status from ALMA is ' +
                                    fromXML['VIEW_STATUS_FOR_SCHEDULINGDB'] + ' and status from scheduling is ' +
                                    fromScheduling['SCHEDBLOCK_CTRL_STATE']
                                    )
                        issue = {'CODE': fromXML['CODE'], 'PRJ_UID': fromXML['PRJ_UID'], 'SB_UID': fromXML['SB_UID'],
                                 'ALMA_STATUS_Map': fromXML['VIEW_STATUS_FOR_SCHEDULINGDB'],
                                 'XML_STATUS': fromXML['XML_STATUS'], 'VIEW_STATUS': fromXML['VIEW_STATUS'],
                                 'SCHEDBLOCK_CTRL_STATE': fromScheduling['SCHEDBLOCK_CTRL_STATE']}

                        AlmaSchedInconsistencies.append(issue)
    return AlmaSchedInconsistencies


def SCOVsOSF(SCOList, OSFList):
    """
    We assume both lists are ordered, so it's possible to optimize the search, otherwise, we'll end up with O**2
    :param SCOList: List of dictionaries containing data from SCO
    :param OSFList: List of dictionaries containing data from OSF
    :return: a list of those SB's with different statuses. Keep in mind that there could be some differences given the
    different times for each query
    """
    ScoOsfInconsistencies = []
    logger.info('Comparing SCO SB statuses (' + str(len(SCOList)) + ') and OSF SB statuses (' + str(len(OSFList)) + ')')
    for SCOproject in SCOList:
        for OSFproject in OSFList:
            if SCOproject['PRJ_UID'] == OSFproject['PRJ_UID']:
                if SCOproject['SB_UID'] == OSFproject['SB_UID']:
                    if SCOproject['VIEW_STATUS_FOR_SCHEDULINGDB'] != OSFproject['VIEW_STATUS_FOR_SCHEDULINGDB']:
                        logger.warning('Project ' + SCOproject['CODE'] + ' (' + SCOproject['PRJ_UID'] +
                                       ') has an issue on SB ' + SCOproject['SB_UID'] +
                                       ' with  inconsistent statuses: SCO:  ' +
                                       SCOproject['VIEW_STATUS_FOR_SCHEDULINGDB'] + ' and OSF: ' +
                                       OSFproject['VIEW_STATUS_FOR_SCHEDULINGDB']
                                       )
                        ScoOsfInconsistencies.append(SCOproject)

    return ScoOsfInconsistencies


def main():
    connectionDetails = myConfigParser.parseConfig('../config/LifeCycleTests.conf')

    ## OSF  ALMA Strings
    dbDetail = 'OSF'
    OSFALMAuser = connectionDetails[dbDetail]['user']
    OSFALMApassword = connectionDetails[dbDetail]['password']
    OSFALMAhost = connectionDetails[dbDetail]['host']
    OSFALMAport = connectionDetails[dbDetail]['port']
    OSFALMAinstance = connectionDetails[dbDetail]['instance']


    ## OSF SCHEDULING Strings
    dbDetail = 'SCHED'
    OSFSCHEDULINGuser = connectionDetails[dbDetail]['user']
    OSFSCHEDULINGpassword = connectionDetails[dbDetail]['password']
    OSFSCHEDULINGhost = connectionDetails[dbDetail]['host']
    OSFSCHEDULINGport = connectionDetails[dbDetail]['port']
    OSFSCHEDULINGinstance = connectionDetails[dbDetail]['instance']

    ## SCO ALMA Strings
    dbDetail = 'SCO'
    SCOALMAuser = connectionDetails[dbDetail]['user']
    SCOALMApassword = connectionDetails[dbDetail]['password']
    SCOALMAhost = connectionDetails[dbDetail]['host']
    SCOALMAport = connectionDetails[dbDetail]['port']
    SCOALMAinstance = connectionDetails[dbDetail]['instance']


    # get data
    t0 = datetime.datetime.utcnow()
    SCOList = getViewVsXML(SCOALMAuser, SCOALMApassword, SCOALMAhost, SCOALMAport, SCOALMAinstance)
    t1 = datetime.datetime.utcnow()
    delta = (t1 - t0)
    logger.info('Get data from SCO took ' + str((delta.microseconds + 0.0 + (delta.seconds + delta.days * 24 * 3600) *
                                                 10 ** 6) / 10 ** 6) + ' seconds.')

    t0 = datetime.datetime.utcnow()
    OSFList = getViewVsXML(OSFALMAuser, OSFALMApassword, OSFALMAhost, OSFALMAport, OSFALMAinstance)
    t1 = datetime.datetime.utcnow()
    delta = (t1 - t0)
    logger.info('Get data from OSF took ' + str((delta.microseconds + 0.0 + (delta.seconds + delta.days * 24 * 3600) *
                                                 10 ** 6) / 10 ** 6) + ' seconds.')

    t0 = datetime.datetime.utcnow()
    SchedulingList = getSchedStatus(OSFSCHEDULINGuser, OSFSCHEDULINGpassword, OSFSCHEDULINGhost, OSFSCHEDULINGport,
                                    OSFSCHEDULINGinstance)
    t1 = datetime.datetime.utcnow()
    delta = (t1 - t0)
    logger.info('Get data from Scheduling took ' + str((delta.microseconds + 0.0 + (delta.seconds + delta.days * 24 *
                                                                                    3600) * 10 ** 6) / 10 ** 6) + ' seconds.')

    # Compare view and xml at SCO
    myListofInconsistenViewXmlSCO = compareViewVsXML(SCOALMAuser, SCOALMApassword, SCOALMAhost, SCOALMAport, SCOALMAinstance)

    # Compare view and xml at OSF
    myListofInconsistenViewXmlOSF = compareViewVsXML(OSFALMAuser, OSFALMApassword, OSFALMAhost, OSFALMAport, OSFALMAinstance)

    # Compare SCO and OSF
    t0 = datetime.datetime.utcnow()
    myListOfInconsistenScoOsf = SCOVsOSF(SCOList, OSFList)
    t1 = datetime.datetime.utcnow()
    delta = (t1 - t0)
    logger.info('Comparation between SCO and OSF took ' + str((delta.microseconds + 0.0 + (delta.seconds + delta.days *
                                                                                           24 * 3600) * 10 ** 6) / 10 ** 6) + ' seconds.')

    if len(myListOfInconsistenScoOsf) > 0:
        print('There are ' + str(len(myListOfInconsistenScoOsf)) + ' inconsistencies between SCO and OSF:')
        for inconsistency in myListOfInconsistenScoOsf:
            print(inconsistency)

    # Compare ALMA Vs SCHEDULING
    t0 = datetime.datetime.utcnow()
    myListOfInconsistenAlmaSched = XMLVsScheduling(OSFList, SchedulingList)
    t1 = datetime.datetime.utcnow()
    delta = (t1 - t0)
    logger.info(
        'Comparation between ALMA and Sched took ' + str((delta.microseconds + 0.0 + (delta.seconds + delta.days *
                                                                                      24 * 3600) * 10 ** 6) / 10 ** 6) + ' seconds.')

    if len(myListOfInconsistenAlmaSched) > 0:
        filename = '../LOGS/myReport-' + strftime("%Y%m%dT%H%M%S ", gmtime()).replace(' ', '') + '.log'
        logger.debug('If there\'s a problem, it\'ll be stored at ' + filename)
        target = open(filename, 'w')
        print('There are ' + str(len(myListOfInconsistenAlmaSched)) + ' inconsistencies, stored at ' + filename)
        for inconsistency in myListOfInconsistenAlmaSched:
            target.write(str(inconsistency))
            target.write('\n')
    else:
        print('There are ' + str(len(myListOfInconsistenAlmaSched)) + ' inconsistencies')


if __name__ == '__main__':
    logging.config.fileConfig('../config/logging.conf', defaults=None, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    main()
