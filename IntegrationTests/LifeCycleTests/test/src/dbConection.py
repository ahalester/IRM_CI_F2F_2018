__author__ = 'scornejo'
import cx_Oracle
import sys
import logging
import logging.config
import myConfigParser

logger = logging.getLogger(__name__)


def handleException(exc):
    error, = exc.args
    print >> sys.stderr, "Oracle-Error-Code:", error.code
    print >> sys.stderr, "Oracle-Error-Message:", error.message
    logger.error("Oracle-Error-Code: " + error.code)
    logger.error("Oracle-Error-Message: " + error.message)


def dbConection(user, password, host, port, instance):
    """

    :param user: the username to use
    :param password: the password to use
    :param host: the host to connect to
    :param port: the port where the service is published
    :param instance: the name of the instance
    :return: a connection to the DB.
    """
    try:
        logger.info('Connecting to ' + user + '/' + len(password) * '*' + '@' + host + ':' + port + '/' + instance)
        db = cx_Oracle.connect(user + '/' + password + '@' + host + ':' + port + '/' + instance)

        logger.debug('We could connect: db version is ' + db.version)
        return db
    except cx_Oracle.DatabaseError, exc:
        handleException(exc)


if __name__ == '__main__':
    logging.config.fileConfig('../config/logging.conf', defaults=None, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)

    connectionDetails = myConfigParser.parseConfig('../config/LifeCycleTests.conf')
    for dbDetail in connectionDetails:
        print 'database:\t' + dbDetail
        user = connectionDetails[dbDetail]['user']
        print 'user:\t' + user
        password = connectionDetails[dbDetail]['password']
        print 'password:\t' + password
        host = connectionDetails[dbDetail]['host']
        print 'host:\t' + host
        port = connectionDetails[dbDetail]['port']
        print 'port:\t' + port
        instance = connectionDetails[dbDetail]['instance']
        print 'instance:\t' + instance
        print '############################################################'
        dbConection(user, password, host, port, instance).close()
        print '############################################################'
