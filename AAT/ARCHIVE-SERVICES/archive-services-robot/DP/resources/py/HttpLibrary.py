import re
import platform
import requests
import cookielib
from requests.auth import HTTPBasicAuth


def get_auth_cookie(url, user, password):
    r = requests.get(url, auth=HTTPBasicAuth(user, password), verify=True)
    # print r.headers
    return r.cookies


def post_url(url, data):
    r = requests.post(url, data)
    return r


def post_with_cookie_auth(url, user, password):
    session = requests.session()
    session.stream = False
    session.get(url, verify=False)

    fields = {
        'downloadType': 'script',
        'fileId': '2017.1.00079.S_readme /alma/requests/2145113108156/2017.1.00079.S/readme/2017.1.00079.S.readme.txt 0 ALMA',
        'fileId': '2017.1.00079.S_uid___A001_X1295_X37_001_of_001.tar /requests/ldoming/2145113108156/ALMA/2017.1.00079.S_uid___A001_X1295_X37_001_of_001.tar/2017.1.00079.S_uid___A001_X1295_X37_001_of_001.tar 8891973632 ALMA'
    }

    cj = get_auth_cookie("http://phase-a1.hq.eso.org/dataportal-ARCHIVE-2018OCT/api/login", user, password)

    response = session.post(url, data=fields, cookies=cj, verify=True)

    # response = session.post(url, cookies=headers)

    return response


print "================== main ======================"

# print(platform.python_version())

print get_auth_cookie("http://phase-a1.hq.eso.org/dataportal-ARCHIVE-2018OCT/api/login", "ldoming", "ldoming4CAS")

print post_with_cookie_auth("http://phase-a1.hq.eso.org/rh-ARCHIVE-2018OCT/requests/ldoming/2145111535396/downloadFile",
                            "ldoming", "ldoming4CAS")

print "================== end ======================="
