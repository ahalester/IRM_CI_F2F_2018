import json
import sys
import requests
import subprocess
import traceback
from optparse import OptionParser
import getpass

class ReportMousAPA():
    """
    Simple client to gather information of MOUS products in staging-area in different states. Also, it provides information of MOUS status in testing environment
    """

    def __init__(self, release='default', state='ReadyForReview', flag = 'default', copy=False, debug=False):
        self.prod_xtss_url = 'https://asa.alma.cl/xtss-rest-server/service/api/obs-unit-sets?state='
        self.state = state

        self.user_name = raw_input('User Name: ')
        self.user_password = getpass.getpass('Password: ')

        self.mous_states = ['FullyObserved', 'ReadyForProcessing', 'Processing', 'ReadyForReview', 'Reviewing', 'Verified']

        if flag == 'default':
            self.flags = ['PipelineSingleDish', 'PipelineCalibration', 'PipelineCalAndImg', 'ManualImaging', 'ManualCalibration', 'ManualSingleDish']
        else:
            self.flags = [flag]
        
        self.debug = debug
        self.copy = copy

        # Create MOUS status list
        self.release = release
        if self.release != 'default':
            self.testing_xtss_url = 'https://' + self.release + '.asa-test.alma.cl/xtss-rest-server/service/api/obs-unit-sets?state='
            self.mous_summary = {}
            self.mous_summary_uid = {}

            for status in self.mous_states:
                self.mous_summary[status] = []
                self.mous_summary_uid[status] = []
                r = requests.get(self.testing_xtss_url+status, auth=(self.user_name, self.user_password))
                mous_json = r.json()
                for mous in mous_json:
                    self.mous_summary[status].append(mous)
                    self.mous_summary_uid[status].append(mous['ousStatusEntityId'])

    def testing_mous_state(self, mous):
        """
        Return the state of a MOUS in the test instance selected
        """
        for state in self.mous_states:
            if mous in self.mous_summary_uid[state]:
                return state

    def disk_usage(self, path):
        """
        Return the disk usage of a MOUS directory
        """
        return subprocess.check_output(['du','-sh', path]).split()[0].decode('utf-8')

    def find_product(self, mous):
        """
        Find all the directories of a MOUS
        """
        return subprocess.check_output(['find','/mnt/jaosco/arcs/apa-staging-area/', '-type', 'd', '-name', '*'+mous+'*']).split()

    def convert_uid_name(self, uid):
        """
        Convert UID to use underscore
        """
        uid_convert = uid.replace(':', '_') 
        uid_convert = uid_convert.replace('/', '_') 
        return uid_convert

    def print_mous(self, mous):
        """
        Print relevant information of a MOUS
        """
        print 'Project Code:', mous['projectCode']
        print 'MOUS Status Entity Id:', self.convert_uid_name(mous['ousStatusEntityId'])
        print 'State:', mous['state']
        print 'Substate:', mous['substate']
        print 'SB Names:', mous['sbNames']
        print 'Number of SchedBlocks:', mous['nrSchedBlocks']
        print

    def filter_executive(self, mous, arc='JAO'):
        """
        Return a boolean depending if the processing executive is JAO or not. For the moment this functionality is disabled. 
        """
        #for ousFlag in mous['ousFlags']:
        #    if (ousFlag['flagName'] == 'PL_PROCESSING_EXECUTIVE') and (ousFlag['flagValue'] == 'JAO'):
        #        return True
    
        return True

    def find_mous(self, mous):
        """
        Return the path list of a MOUS. It also print the relevant information to describe the current status of the apa-staging-area
        """
        path_list = []
        try:
            if self.release == 'default':
                print 'MOUS:', mous['ousStatusEntityId'], '\tProduction:', self.state, '\tType:', mous['requestedArray']
            else:
                print 'MOUS:', mous['ousStatusEntityId'], '\tProduction:', self.state, '\tTesting:', self.testing_mous_state(mous['ousStatusEntityId']), '\tType:', mous['requestedArray']
                
            paths = self.find_product(self.convert_uid_name(mous['ousStatusEntityId']))
            for path in paths:
                path_list.append(path.decode('utf-8')[0:72])
                print 'Location: ', path.decode('utf-8')[0:72], '\t\tSize:', self.disk_usage(path.decode('utf-8'))
        except:
            print traceback.print_exc()

        print
    
        return path_list

    def mous_list(self, url='default', state='default'):
        """
        Retrieve all the MOUS from an specific XTSS in a given state. Iterate this list to build the mous_path dict
        """
        if url == 'default' and state == 'default':
            r = requests.get(self.prod_xtss_url+self.state, auth=(self.user_name, self.user_password))
        else:
            r = requests.get(url+state, auth=(self.user_name, self.user_password))

        mous_json = r.json()
        self.mous_path = {}
    
        for flag in self.flags:
            print '-------------', flag, '---------------'
            for mous in mous_json:
                if (mous['substate'] == flag) and self.filter_executive(mous):
                    self.mous_path[mous['ousStatusEntityId']] = self.find_mous(mous)

    def print_rsync(self):
        """
        Print rsync command to copy from the docker container. Not finished yet ICT-12146
        """
        print '------------- ICT-12146 rsync commands to run from testing stating-area -------------'
        for mous_uid in self.mous_path:
            print 'MOUS uid:', mous_uid
            for path in self.mous_path[mous_uid]:
                print 'rsync -ruva obops@aqua-agent.sco.alma.cl:' + path, '/var/product-ingestor/staging-area/'
            print
    
    def filter_qa2_decision(self, mous):
        """
        Return a boolean depending if the AWAITING_QA2_DECISION is active or not
        """
        for ousFlag in mous['ousFlags']:
            if (ousFlag['flagName'] == 'AWAITING_QA2_DECISION') and (ousFlag['flagValue'] == 'Active'):
                return True

        return False

    def tc04_mous(self):
        """
        Print the relevant information for use case described in ICT-12144
        """
        if self.release != 'default':
            print '---------------- ICT-12144 MOUS in processing state in testing DB ----------------'

            for mous in self.mous_summary['Processing']:
                if mous['ousStatusEntityId'] in self.mous_path.keys():
                    print 'MOUS:', mous['ousStatusEntityId']#, 'Paths:', self.mous_path[mous['ousStatusEntityId']]
                    for path in self.mous_path[mous['ousStatusEntityId']]:
                        print 'Location: ', path.decode('utf-8')[0:72], '\t\tSize:', self.disk_usage(path.decode('utf-8'))
                    print

    def tc08_mous(self):
        """
        Print the relevant information for use case described in ICT-12145
        """
        if self.release != 'default':
            print '---------------- ICT-12145 MOUS in Reviewing State with Awaiting decision by DRM set in testing database  ----------------'

            for mous in self.mous_summary['Reviewing']:
                if self.filter_qa2_decision(mous):
                    if mous['ousStatusEntityId'] in self.mous_path.keys():
                        print 'MOUS:', mous['ousStatusEntityId']#, 'Paths:', self.mous_path[mous['ousStatusEntityId']]
                        for path in self.mous_path[mous['ousStatusEntityId']]:
                            print 'Location: ', path.decode('utf-8')[0:72], '\t\tSize:', self.disk_usage(path.decode('utf-8'))
                        print


def main():
    """
    Main method to call the program.
    Added parameters:
        -s: MOUS state in production to be considered. For example ReadyForReview. Optional
        -r: Testing environment to compare the status. For example 2018apr. Optional
        -f: Select a single Processing Flag. Example: PipelineCalibration. Optional
        -c: Print rsync command to copy into the testing staging-area. In order to use this command, you have to be connected into aquapipeline docker service
        -v: Enable debug logs. To be used during development
    """
    parser = OptionParser(usage="usage: %prog [options]")
    parser.add_option("-s", "--state",
                      dest="state",
                      default=False,
                      help="MOUS State. Example: ReadyForReview")
    parser.add_option("-r", "--release",
                      dest="release",
                      default=False,
                      help="Testing environment release. Example: 2018apr")
    parser.add_option("-c", "--copy",
                      action="store_true",
                      dest="copy",
                      default=False,
                      help="Add rsync output. To copy into testing apa-staging-area")
    parser.add_option("-f", "--flag",
                      dest="flag",
                      default=False,
                      help="Select a single Processing Flag. Example: PipelineCalibration")
    parser.add_option("-v", "--verbose",
                      action="store_true",
                      dest="verbose",
                      default=False,
                      help="Print verbose information")
    (options, args) = parser.parse_args()

    state_option = 'ReadyForReview'
    release_option = 'default'
    flag_option = 'default'
    copy_option = False
    debug_option = False

    if options.state:
        state_option = options.state

    if options.release:
        release_option = options.release

    if options.verbose:
        debug_option = options.verbose

    if options.flag:
        flag_option = options.flag

    report = ReportMousAPA(release = release_option, state = state_option, copy = copy_option, debug = debug_option, flag = flag_option)
    report.mous_list()
    report.tc04_mous()
    report.tc08_mous()

    if options.copy:
        report.print_rsync()

if __name__ == "__main__":
    main()
