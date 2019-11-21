__author__ = 'scornejo'
import logging
import logging.config

logger = logging.getLogger(__name__)

def parseConfig(configFile):
    logger.info('opening ' + configFile)
    with open(configFile, 'r') as openedFile:
        lines = openedFile.readlines()
        #search for declared databases
        dbs ={}
        for line in lines:
            if len(line.strip().replace('\n', '')) > 0:
                if line.strip()[0] != '#':
                    # Search for declaration of databases
                    if 'LifeCycleTests.DBs'.capitalize().strip() in line.capitalize().strip():
                        myDB = line.replace('LifeCycleTests.DBs', '').replace('=', '').replace(',', '').split()
                        for eachDB in myDB:
                            dbs[eachDB] = {'user': '', 'password': '', 'host': '', 'port': '', 'instance': ''}
                        logger.info('Found ' + str(len(dbs)) + ' DBs: ' + str(dbs.keys()))

        #search for details
        logger.info('searching for connection details')

        for db in dbs:
            logger.info('Processing for ' + db)
            for line in lines:
                if (db + '.user') in line:
                    dbs[db]['user'] = line.replace((db + '.user'), '').replace('=', '').replace("'", '').strip()
                if (db + '.password') in line:
                    dbs[db]['password'] = line.replace((db + '.password'), '').replace('=', '').replace("'", '').strip()
                if (db + '.host') in line:
                    dbs[db]['host'] = line.replace((db + '.host'), '').replace('=', '').replace("'", '').strip()
                if (db + '.port') in line:
                    dbs[db]['port'] = line.replace((db + '.port'), '').replace('=', '').replace("'", '').strip()
                if (db + '.instance') in line:
                    dbs[db]['instance'] = line.replace((db + '.instance'), '').replace('=', '').replace("'", '').strip()
    return dbs


if __name__ == '__main__':
    logging.config.fileConfig('../config/logging.conf', defaults=None, disable_existing_loggers=False)
    logger = logging.getLogger(__name__)

    connectiondetails = parseConfig('../config/LifeCycleTests.conf')

    for dbDetail in connectiondetails:
        print dbDetail + ': ' + str(connectiondetails[dbDetail])
