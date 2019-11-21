import re
import platform
import requests




cycle0_PHAB_List = "https://2018aug.asa-test.alma.cl/dataPortal/api/requests/anonymous/7923502703/ALMA/2011.0.00010.S_2012-05-07_001_of_011.tar/2011.0.00010.S_2012-05-07_001_of_011.tar "
cycle5_PHAA_List = "http://phase-a1.hq.eso.org/dataportal-ARCHIVE-2018AUG/api/requests/ldoming/2145092775654/ALMA/2016.1.00004.S_uid___A001_X879_Xea_001_of_001.tar/2016.1.00004.S_uid___A001_X879_Xea_001_of_001.tar"
URL_LISTS = {'cycle0_PHAB_List': cycle0_PHAB_List, 'cycle5_PHAA_List': cycle5_PHAA_List}



def set_test_urls_list(cycle, phase):
    with open ('./resources/downloadRequest.sh', 'r' ) as f:
        content = f.read()

    url_key = cycle + "_" + phase + "_List"

    content_new = re.sub('to-be-replaced', URL_LISTS[url_key], content, flags = re.M) #string1 = re.sub('#chop-begin.*?#chop-end', '', string0, flags=re.DOTALL)

    print  "Writing TC URL to download script, new content is:" + content_new

    replaced_file = open("current_downloadRequest.sh", "w")
    replaced_file.write(content_new)



def get_extracted_dir_name1(string):
    #https://thepythonguru.com/python-regular-expression/
    substring = re.search(r'x[^x/]*/', string).group(0)
    substring = re.sub("x ", "" , substring)
    substring = re.sub("/", "" , substring)
    return substring

def get_extracted_dir_name(string):
    substring = string.splitlines()[0]
    substring = re.sub("/.*", "" , substring)
    substring = re.sub("x ", "" , substring)
    return substring


print "================== main ======================"

substring=get_extracted_dir_name("x 2016.1.00004.S/"
                     "x 2016.1.00005.S/science_goal.uid___A001_X879_Xe8/"
                     "x 2016.1.00004.S/science_goal.uid___A001_X879_Xe8/group.uid___A001_X879_Xe9/"
                     "x 2016.1.00004.S/science_goal.uid___A001_X879_Xe8/group.uid___A001_X879_Xe9/member.uid___A001_X879_Xea/")

print(platform.python_version())

r=requests.get("http://www.google.com")
print r.content

print "================== end ======================="