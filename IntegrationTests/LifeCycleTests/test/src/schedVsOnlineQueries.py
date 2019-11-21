__author__ = 'scornejo'
from dbConection import dbConection
from viewVsXMLQueries import rows_as_dicts
import logging
import myConfigParser

logger = logging.getLogger(__name__)


def getSchedStatus(user, password, host, port, instance):
    logger.info('getting SB status from SCHEDULING DB')
    conn = dbConection(user, password, host, port, instance)
    cursor = conn.cursor()
    cursor.execute(
        "select "
        "  obsproject.code as prj_code, "
        "  obsunit.obsunit_project_uid as prj_uid, "
        "  schedblock.schedblock_uid as sb_uid, "
        "  schedblock.schedblock_ctrl_state  "
        "from  "
        "  schedblock, "
        "  obsunit, "
        "  obsproject "
        "where "
        "  obsunit.obsunitid = schedblock.schedblockid "
        "and "
        "  obsproject.obsproject_uid =obsunit.obsunit_project_uid "
        "order by "
        "   obsproject.code, schedblock.schedblock_uid "
    )
    issues = list(rows_as_dicts(cursor))
    cursor.close()
    conn.close()
    return issues


def getSchedStatusTEST(user, password, host, port, instance):
    logger.info('getting SB status from SCHEDULING DB using test query')
    conn = dbConection(user, password, host, port, instance)
    cursor = conn.cursor()
    cursor.execute(
        "select "
        "  obsproject.code, "
        "  obsunit.obsunit_project_uid as prj_uid, "
        "  schedblock.schedblock_uid as sb_uid, "
        "  schedblock.schedblock_ctrl_state  "
        "from  "
        "  schedblock, "
        "  obsunit, "
        "  obsproject "
        "where "
        "  obsunit.obsunitid = schedblock.schedblockid "
        "and "
        "  obsproject.obsproject_uid =obsunit.obsunit_project_uid "
        "and "
        "  code = '2013.1.00001.S' "
        "order by obsproject.code, schedblock.schedblock_uid"
    )
    issues = list(rows_as_dicts(cursor))
    cursor.close()
    conn.close()
    return issues

if __name__ == '__main__':
    logging.config.fileConfig('../config/logging.conf', defaults=None, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)
    logger.info('Starting comparison at OSF')

    connectionDetails = myConfigParser.parseConfig('../config/LifeCycleTests.conf')
    dbDetail = 'SCHED'
    user = connectionDetails[dbDetail]['user']
    password = connectionDetails[dbDetail]['password']
    host = connectionDetails[dbDetail]['host']
    port = connectionDetails[dbDetail]['port']
    instance = connectionDetails[dbDetail]['instance']

    myIssues = getSchedStatusTEST(user, password, host, port, instance)
    if len(myIssues) > 0:
        logger.info('There are ' + str(len(myIssues)) + ' issues:')
    for k in myIssues:
            logger.debug( k )
