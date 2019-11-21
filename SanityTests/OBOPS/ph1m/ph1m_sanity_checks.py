#!/usr/bin/env python

import urllib
import unittest
import os.path
import socket
import subprocess
import multiprocessing
import time
from datetime import datetime
import utils
import xmlrunner

# To check if the proxy is redirecting
proxied_site = 'http://asa.alma.cl/ph1m'

# To check if the webapp is up
site = 'http://tergeste-dev.sco.alma.cl:8080/ph1m/'

# To check if config files are in place
config_files = { 'archiveConfig_properties' : '/etc/offline/config/archiveConfig.properties', 'obopsConfig_properties' : '/etc/offline/config/obopsConfig.properties', 'tnsnames_ora' : '/etc/offline/config/tnsnames.ora'}

# To check if the db is reachable
db_toTest = {}

# To ckeck if there's enough space on the disks
path ='/'

# To check if the files have the proper timestamps. Format is YYYY-mm-DD-THH:MM:SS
app_files = { 'war' : '/usr/local/tomcat/webapps/ph1m.war'}
deployment_date = '2016-08-11T01:22:24' 

class webappsTests(unittest.TestCase):
    
    def test_disk_usage(self):
        st = os.statvfs(path)
        free = st.f_bavail * st.f_frsize
        total = st.f_blocks * st.f_frsize
        used = (st.f_blocks - st.f_bfree) * st.f_frsize
        print 'Checking if disk usage is less than 90%: Total: ' + str(total) + ', used: ' + str(used) + '(' + str((100*used)/total) + '%), free: ' + str(free) + '(' + str((100*free)/total) +'%)'
        self.assertTrue(((100*used)/total)<90)

    def test_CPU(self):
        print 'Checking if the number of CPUs is at least 4: ' + str(multiprocessing.cpu_count())
        self.assertTrue(multiprocessing.cpu_count()>=4)

    def test_RAM(self):
        with open('/proc/meminfo', 'r') as mem:
            ret = {}
            tmp = 0
            for i in mem:
                sline = i.split()
                if str(sline[0]) == 'MemTotal:':
                    ret['total'] = int(sline[1])
                elif str(sline[0]) in ('MemFree:', 'Buffers:', 'Cached:'):
                    tmp += int(sline[1])
            ret['free'] = tmp
            ret['used'] = int(ret['total']) - int(ret['free'])
        print 'Checking if physical memory is at least 4 GB: ' + str(ret['total']/(1024*1024))
        self.assertTrue((ret['total']/(1024*1024))>=4)

    def test_app_files(self):
        for k in app_files:
            print 'Checking timestamps for ' + app_files[k]
            ctime = time.ctime(os.path.getctime(app_files[k]))
            mtime = time.ctime(os.path.getmtime(app_files[k]))
            print 'Created on ' + ctime
            print 'Modified on ' + mtime
            self.assertTrue(datetime.strptime(ctime,"%a %b %d %H:%M:%S %Y") >= datetime.strptime (deployment_date, "%Y-%m-%dT%H:%M:%S"))
        
    def test_site(self):
        print 'Testing for tomcat directly on the machine'
        self.assertEqual(urllib.urlopen(site).getcode(), 200)

    def test_proxy(self):
        print 'Testing for proxy redirection'
        self.assertEqual(urllib.urlopen(proxied_site).getcode(), 200)
    
    def test_config_files(self):
        '''
        Checks for existence of archiveConfig.properties, obopsConfig.properties and 
        tnsnames.ora.
        Also, checks for the different databases on archiveConfig.properties: it is
        expected that we use only one instance, and later we search for that instance
        on tnsnames.ora, and then try to see if the port on the DB host is open.
        '''
        global db_toTest
        for k in config_files:
            print 'Checking if ' + k.replace('_','.') + ' exists'
            self.assertTrue(os.path.exists(config_files[k]))
            if 'archiveConfig.properties' in config_files[k]:
                declared_db = ''
                changed = 0
                aux = utils.configFileParser(config_files[k])
                for m in aux:
                    if 'jdbc:oracle:thin:' in aux[m]:
                        if declared_db <> aux[m].replace('jdbc:oracle:thin:@',''):
                            declared_db = aux[m].replace('jdbc:oracle:thin:@','')
                            changed += 1
                self.assertTrue(changed == 1)
                db = declared_db
            if 'obopsConfig.properties' in config_files[k]:
                print ''
            if 'tnsnames.ora' in config_files[k]:
                db_dict = utils.tnsnameParser(config_files[k])
                db_toTest = db_dict[db]
    
    def test_database(self):
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        print 'Checking connection to ' + db_toTest['HOST'] + ' on PORT ' + db_toTest['PORT']
        s.connect((db_toTest['HOST'], int(db_toTest['PORT'])))
        connection = s.getpeername()
        s.shutdown(2)
        self.assertNotEqual(connection, None)

if __name__ == '__main__':
    unittest.main(testRunner=xmlrunner.XMLTestRunner(output='test-reports'))
#    suite = unittest.TestLoader().loadTestsFromTestCase(webappsTests)
#    unittest.TextTestRunner(verbosity=2).run(suite)
    #unittest.main()
