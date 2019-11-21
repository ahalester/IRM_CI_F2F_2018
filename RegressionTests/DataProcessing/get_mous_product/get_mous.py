"""
get_mous.py

Script to download a MOUS product from production NGAS

In its current implementation it uses dummy names for the project (with the timestamp of the
moment where the product was downloaded), the SOUS and the GOUS directories. Only the MOUS
directory name has the actual MOUS UID in it. On a future implementation of this script this
dummy names should be replaced for the actual project name, SOUS and GOUS UIDs; obtained, for
instance, from ProTrack.

Also, in its current implementation, this script only tries to download the products from a single
NGAS instance (i.e.: ngas04.sco.alma.cl:7778).

Requirements:

- Oracle Instant Client

http://www.oracle.com/technetwork/database/database-technologies/instant-client/overview/index.html

After installing remember to add the lib path to your $LD_LIBRARY_PATH environment variable
e.g.: export LD_LIBRARY_PATH=/usr/lib/oracle/12.2/client64/lib/:$LD_LIBRARY_PATH

- cx_Oracle module for Python

This can be installed via PIP
e.g.: pip install cx_Oracle
"""

import cx_Oracle
import subprocess
import os
from datetime import datetime
import argparse

class cd:
    """Context manager for changing the current working directory"""
    def __init__(self, newPath):
        self.newPath = os.path.expanduser(newPath)

    def __enter__(self):
        self.savedPath = os.getcwd()
        os.chdir(self.newPath)

    def __exit__(self, etype, value, traceback):
        os.chdir(self.savedPath)

def filter_products(filenames):
    """Function to filter a list of filenames and leave out those that are not products"""

    filtered_products = list()
    for filename, in filenames:
        if filename.rstrip().endswith('product_ingestor.xml') or filename.rstrip().endswith('product_rename.txt'):
            continue
        filtered_products.append(filename)
    return filtered_products

def main():

    parser = argparse.ArgumentParser()
    parser.add_argument("MOUS_UID", help="Status UID of the desired MOUS.")
    parser.add_argument('-d', '--directory', default='.', help="Directory where to save the MOUS product.")
    args = parser.parse_args()

    mous_uid = args.MOUS_UID
    # mous_uid = 'uid://A001/X1288/X264'

    current_datetime = datetime.utcnow().strftime('%Y_%m_%dT%H_%M_%S.000')
    base_dir = os.path.join(args.directory, '2001.1.00000.S_%s' % current_datetime)
    products_dir = os.path.join(base_dir, 'SOUS_uid___A000_X0000_X000', 'GOUS_uid___A000_X0000_X000', 'MOUS_%s' % mous_uid.replace(':','_').replace('/','_'), 'products')

    connection = cx_Oracle.connect('alma', 'alma$dba', "oraosf.osf.alma.cl/ONLINE.OSF.CL")

    cursor = connection.cursor()

    product_files_query = "select ngas_file_id from asa_product_files where ASA_OUS_ID=:mous_uid"
    cursor.execute(product_files_query, mous_uid=mous_uid)
    filenames = filter_products(cursor.fetchall())

    if filenames:
        os.makedirs(products_dir)

        curl_request = "curl 'http://ngas04.sco.alma.cl:7778/RETRIEVE?file_id=%s' -H 'Host: ngas04.sco.alma.cl:7778' -H 'User-Agent: Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0' -H 'Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8' -H 'Accept-Language: en-US,en;q=0.5' --compressed -H 'Referer: http://augusta.sco.alma.cl:5555/' -H 'Connection: keep-alive' -H 'Upgrade-Insecure-Requests: 1' --output %s"

        with cd(products_dir):
            for index, filename in enumerate(filenames):
                print "Downloading product file [%d/%d] %s" % (index+1, len(filenames), filename)
                process = subprocess.Popen(curl_request % (filename, filename), stdout=subprocess.PIPE, shell=True)
                while True:
                    line = process.stdout.readline().rstrip()
                    if not line:
                        break
                    print line

        chmod_command = "find %s -type %s -exec chmod %s {} \;"

        subprocess.call(chmod_command % (base_dir, 'd', '777'), shell=True)
        subprocess.call(chmod_command % (base_dir, 'f', '666'), shell=True)

    else:
        print "No product files found for MOUS %s" % mous_uid

   
    # TODO: copy product to testing environment??
    # rsync -aPv $base_dir root@apa-dev.apotest.alma.cl:/mnt/jaosco/arcs/2018apr/apa-staging-area/

if __name__ == '__main__':
    main()
