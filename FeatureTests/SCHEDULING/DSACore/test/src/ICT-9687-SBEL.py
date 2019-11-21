import os
import DsaDataBase as Data
import DsaAlgorithm as Dsa
import DsaScorers as DsaScore

print os.environ['CON_STR']
apdm_location = os.environ['APDM_PREFIX']

#datas = Data.DsaDatabase(refresh_apdm=True, path=apdm_location, loadp1=True)
# First time download of APDMs
#datas = Data.DsaDatabase(path=apdm_location, cycles=("2017", "2016"), loadp1=True, load_nogrades=True, projects=("A", "S", "T", "L", "V"), refresh_apdm=True,nosbs=False)
#datas = Data.DsaDatabase(path=apdm_location, cycles=("2017", "2016"), loadp1=True, load_nogrades=True, projects=("A", "S", "T", "L", "V"), refresh_apdm=False,nosbs=False)
#datas = Data.DsaDatabase(path=apdm_location, loadp1=True, load_nogrades=True, projects=("A", "S", "T", "L", "V"), refresh_apdm=False,nosbs=True)

# from serv_c4
#try:
    #datas = Data.DsaDatabase(refresh_apdm=False, loadp1=False, cycles=("2015", "2016"),ignore_projects=("Canceled", "Rejected", "ObservingTimedOut","Completed", "Phase1Submitted", "Phase2Submitted","Approved", "NotObserved","PartiallyCompleted"),impar='c4new', active_only=True)
#except Exception as ex:
    #print "Error"
    datas = Data.DsaDatabase(refresh_apdm=True, loadp1=False,ignore_projects=("Canceled", "Rejected", "ObservingTimedOut","Completed", "Phase1Submitted", "Phase2Submitted", "Approved", "NotObserved", "PartiallyCompleted"),impar='c4new', cycles=("2015", "2016"), active_only=True)
    
#Ignacio Toledo: 
try:
    datas = Data.DsaDatabase(path=apdm_location, cycles=("2017", "2016"), loadp1=True, load_nogrades=True, projects=("A", "S", "T", "L", "V"), refresh_apdm=False,nosbs=False)
except Exception as ex:
    print "Full refresh needed"
    
    
print datas.schedblocks['isSimultaneous12m7m']

