__author__ = 'scornejo'

from dbConection import dbConection
import logging
import myConfigParser

logger = logging.getLogger(__name__)

def rows_as_dicts(cursor):
    """

    :param cursor: the cursor to parse for results
    :return: returns cx_Oracle rows as dicts
    """
    try:
        colnames = [i[0] for i in cursor.description]
        for row in cursor:
            yield dict(zip(colnames, row))
    except AttributeError:
        print AttributeError.message


def compareViewVsXML (user, password, host, port, instance):
    """

    :param user: the username to use for connection
    :param password: the password to use
    :param host: the host to connect to
    :param port: the port where the service is published
    :param instance: the name of the instance
    :return: a list of dictionaries containing the name of each column and the value for those SBS where
    there are differences between the status stored at BMMV and XMLs.
    """
    logger.info('checking differences between BMMVs and XMLs for SB status')
    conn = dbConection(user, password, host, port, instance)
    cursor = conn.cursor()
    cursor.execute(
        "select "
        "   bmmv_obsproject.prj_code as code, "
        "   bmmv_obsproject.prj_archive_uid as prj_uid, "
        "   bmmv_schedblock.archive_uid as sb_uid, "
        "   bmmv_schedblock.status as view_status, "
        "   x.status as xml_status, "
        "   case "
        "     when "
        "       bmmv_schedblock.status = 'CSVRunning' "
        "         then 'RUNNING' "
        "     when "
        "       bmmv_schedblock.status = 'NewPhase1' "
        "         then '2' "
        "     when "
        "       bmmv_schedblock.status = 'Verified' "
        "         then '3' "
        "     when "
        "       bmmv_schedblock.status = 'Waiting' "
        "         then '4' "
        "     when "
        "       bmmv_schedblock.status = 'CSVSuspended' "
        "         then '5' "
        "     when "
        "       bmmv_schedblock.status = 'CSVReady' "
        "         then 'READY' "
        "     when "
        "       bmmv_schedblock.status = 'Suspended' "
        "         then '7' "
        "     when "
        "       bmmv_schedblock.status = 'CalibratorCheck' "
        "         then '8' "
        "     when "
        "       bmmv_schedblock.status = 'Processed' "
        "         then '9' "
        "     when "
        "       bmmv_schedblock.status = 'Running' "
        "         then 'RUNNING' "
        "     when "
        "       bmmv_schedblock.status = 'Phase2Submitted' "
        "         then 'READY' "
        "     when "
        "       bmmv_schedblock.status = 'Ready' "
        "         then 'READY' "
        "     when "
        "       bmmv_schedblock.status = 'NewPhase2' "
        "         then '13' "
        "     when "
        "       bmmv_schedblock.status = 'FullyObserved' "
        "         then 'FULLY_OBSERVED' "
        "     when "
        "       bmmv_schedblock.status = 'Canceled' "
        "         then 'CANCELED' "
        "   end as view_Status_for_SCHEDULINGDB "
        "from "
        "   alma.bmmv_obsproject, "
        "   alma.bmmv_schedblock, "
        "   alma.xml_schedblock_entities, "
        "   xmltable('/*:SchedBlock' "
        "                 passing xml_schedblock_entities.xml "
        "                 columns status varchar(40) path '/*:SchedBlock/@status' "
        "           ) x "
        "where "
        "   bmmv_obsproject.prj_archive_uid = bmmv_schedblock.prj_ref "
        "and "
        "   bmmv_schedblock.archive_uid = xml_schedblock_entities.archive_uid "
        "and "
        "   bmmv_schedblock.status <> x.status "
        "and "
        "   xml_schedblock_entities.deleted <> 1"
        "order by "
        "   bmmv_obsproject.prj_archive_uid, bmmv_schedblock.archive_uid "
    )
    issues = list(rows_as_dicts(cursor))
    cursor.close()
    conn.close()
    return issues


def getViewVsXML (user, password, host, port, instance):
    """

    :param user: the username to use for connection
    :param password: the password to use
    :param host: the host to connect to
    :param port: the port where the service is published
    :param instance: the name of the instance
    :return: a list of dictionaries containing the name of each column and the value for all SBS so that they can be
    compared against the SCHEDULING DB.
    """
    logger.info('getting list of SB status from BMMVs and XMLs')
    conn = dbConection(user, password, host, port, instance)
    cursor = conn.cursor()
    cursor.execute(
        "select "
        "   bmmv_obsproject.prj_code as code, "
        "   bmmv_obsproject.prj_archive_uid as prj_uid, "
        "   bmmv_schedblock.archive_uid as sb_uid, "
        "   bmmv_schedblock.status as view_status, "
        "   x.status as xml_status, "
        "   case "
        "       when "
        "           bmmv_schedblock.status = 'CSVRunning' "
        "               then 'RUNNING' "
        "       when "
        "           bmmv_schedblock.status = 'NewPhase1' "
        "               then '2' "
        "       when "
        "           bmmv_schedblock.status = 'Verified' "
        "               then '3' "
        "       when "
        "            bmmv_schedblock.status = 'Waiting' "
        "               then '4' "
        "       when "
        "           bmmv_schedblock.status = 'CSVSuspended' "
        "               then '5' "
        "       when "
        "           bmmv_schedblock.status = 'CSVReady' "
        "               then 'READY' "
        "       when "
        "           bmmv_schedblock.status = 'Suspended' "
        "               then '7' "
        "       when "
        "           bmmv_schedblock.status = 'CalibratorCheck' "
        "               then '8' "
        "       when "
        "           bmmv_schedblock.status = 'Processed' "
        "               then '9' "
        "       when "
        "           bmmv_schedblock.status = 'Running' "
        "               then 'RUNNING' "
        "       when "
        "           bmmv_schedblock.status = 'Phase2Submitted' "
        "               then 'READY' "
        "       when "
        "           bmmv_schedblock.status = 'Ready' "
        "               then 'READY' "
        "       when "
        "           bmmv_schedblock.status = 'NewPhase2' "
        "               then '13' "
        "       when "
        "           bmmv_schedblock.status = 'FullyObserved' "
        "               then 'FULLY_OBSERVED' "
        "       when "
        "           bmmv_schedblock.status = 'Canceled' "
        "               then 'CANCELED' "
        "   end as view_Status_for_SCHEDULINGDB "
        "from "
        "   alma.bmmv_obsproject, "
        "   alma.bmmv_schedblock, "
        "   alma.xml_schedblock_entities, "
        "   xmltable('/*:SchedBlock' "
        "                passing xml_schedblock_entities.xml "
        "                columns status varchar(40) path '/*:SchedBlock/@status' "
        "          ) x "
        "where "
        "   bmmv_obsproject.prj_archive_uid = bmmv_schedblock.prj_ref "
        "and "
        "   bmmv_schedblock.archive_uid = xml_schedblock_entities.archive_uid "
        "and "
        "   upper(bmmv_schedblock.status) in ('CSVREADY', 'CSVRUNNING', 'RUNNING', "
        "'PHASE2SUBMITTED', 'READY', 'FULLYOBSERVED' ) "
        "and "
        "   xml_schedblock_entities.deleted <> 1"
        "order by "
        "   bmmv_obsproject.prj_archive_uid, bmmv_schedblock.archive_uid "
    )
    issues = list(rows_as_dicts(cursor))
    cursor.close()
    conn.close()
    return issues


def getViewVsXMLTest (user, password, host, port, instance):
    """

    :param user: the username to use for connection
    :param password: the password to use
    :param host: the host to connect to
    :param port: the port where the service is published
    :param instance: the name of the instance
    :return: a list of dictionaries containing the name of each column and the value for all SBS so that they can be
    compared against the SCHEDULING DB.
    """
    logger.info('getting list of SB status from BMMVs and XMLs using TEST QUERY')
    conn = dbConection(user, password, host, port, instance)
    cursor = conn.cursor()
    cursor.execute(
        "select "
        "   bmmv_obsproject.prj_code as code, "
        "   bmmv_obsproject.prj_archive_uid as prj_uid, "
        "   bmmv_schedblock.archive_uid as sb_uid, "
        "   bmmv_schedblock.status as view_status, "
        "   x.status as xml_status, "
        "   case "
        "       when "
        "           bmmv_schedblock.status = 'CSVRunning' "
        "               then 'RUNNING' "
        "       when "
        "           bmmv_schedblock.status = 'NewPhase1' "
        "               then '2' "
        "       when "
        "           bmmv_schedblock.status = 'Verified' "
        "               then '3' "
        "       when "
        "           bmmv_schedblock.status = 'Waiting' "
        "               then '4' "
        "       when "
        "           bmmv_schedblock.status = 'CSVSuspended' "
        "               then '5' "
        "       when "
        "           bmmv_schedblock.status = 'CSVReady' "
        "               then 'READY' "
        "       when "
        "           bmmv_schedblock.status = 'Suspended' "
        "               then '7' "
        "       when "
        "           bmmv_schedblock.status = 'CalibratorCheck' "
        "               then '8' "
        "       when "
        "           bmmv_schedblock.status = 'Processed' "
        "               then '9' "
        "       when "
        "           bmmv_schedblock.status = 'Running' "
        "               then 'RUNNING' "
        "       when "
        "           bmmv_schedblock.status = 'Phase2Submitted' "
        "               then 'READY' "
        "       when "
        "           bmmv_schedblock.status = 'Ready' "
        "               then 'READY' "
        "       when "
        "           bmmv_schedblock.status = 'NewPhase2' "
        "               then '13' "
        "       when "
        "           bmmv_schedblock.status = 'FullyObserved' "
        "               then 'FULLYOBSERVED' "
        "       when "
        "           bmmv_schedblock.status = 'Canceled' "
        "               then 'CANCELED' "
        "   end as view_Status_for_SCHEDULINGDB "
        "from "
        "   alma.bmmv_obsproject, "
        "   alma.bmmv_schedblock, "
        "   alma.xml_schedblock_entities, "
        "   xmltable('/*:SchedBlock' "
        "                passing xml_schedblock_entities.xml "
        "                columns status varchar(40) path '/*:SchedBlock/@status' "
        "          ) x "
        "where "
        "   bmmv_obsproject.prj_archive_uid = bmmv_schedblock.prj_ref "
        "and "
        "   bmmv_schedblock.archive_uid = xml_schedblock_entities.archive_uid "
        "and "
        "   upper(bmmv_schedblock.status) in ('CSVREADY', 'CSVRUNNING', 'RUNNING', "
        "'PHASE2SUBMITTED', 'READY', 'FULLYOBSERVED' ) "
        "and "
        "  bmmv_obsproject.prj_code in ('2013.1.00001.S ') "
        "order by "
        "   bmmv_obsproject.prj_archive_uid, bmmv_schedblock.archive_uid "
    )
    issues = list(rows_as_dicts(cursor))
    cursor.close()
    conn.close()
    return issues


if __name__ == '__main__':
    logging.config.fileConfig('../config/logging.conf', defaults=None, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    logger.info('Starting comparison at OSF using TEST Query')
    connectionDetails = myConfigParser.parseConfig('../config/LifeCycleTests.conf')
    dbDetail = 'OSF'
    user = connectionDetails[dbDetail]['user']
    password = connectionDetails[dbDetail]['password']
    host = connectionDetails[dbDetail]['host']
    port = connectionDetails[dbDetail]['port']
    instance = connectionDetails[dbDetail]['instance']

    myIssues = getViewVsXMLTest(user, password, host, port, instance)
    logger.warning('So do you know if there\'s any inconsistency?')
    if len(myIssues) > 0:

        logger.warning('About inconsistencies: On the contrary. I\'m possibly more or less not definitely rejecting\n' \
              'the idea that in no way with any amount of uncertainty that I undeniably do or do not know where\n' \
              'there shouldn\'t probably be, if that indeed wasn\'t where there isn\'t any such inconsistency')
        logger.warning( 'There are ' + str(len(myIssues)) + ' issues:')
        for k in myIssues:
            logger.warning(k)
    else:
        logger.warning('There are zero differences between the views and the XMLs at OSF DB... and business is good ')

    SCHEDULINGdb = getViewVsXML(user, password, host, port, instance)
    logger.warning('Got ' + str(len(SCHEDULINGdb)) + ' SBs to compare')