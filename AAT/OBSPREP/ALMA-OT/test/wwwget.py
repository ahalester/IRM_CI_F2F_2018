#!/usr/bin/env python
#*******************************************************************************
# ALMA - Atacama Large Millimeter Array
# Copyright (c) ESO - European Southern Observatory, 2012
# (in the framework of the ALMA collaboration).
# All rights reserved.
#
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
#*******************************************************************************

import urllib2

secured = 'ObsprepSubmissionService/SubmissionService'

def initOpener(submissionService, user=None, passwd=None):
  if submissionService[-1] <> '/':
    submissionService += '/'
  # Create an OpenerDirector with support for Basic HTTP Authentication...
  password_mgr = urllib2.HTTPPasswordMgrWithDefaultRealm()
  auth_handler = urllib2.HTTPBasicAuthHandler(password_mgr)
  #auth_handler.add_password(realm='ALMA Submission Service',
  auth_handler.add_password(realm=None,
                            uri=submissionService + secured,
                            user=user,
                            passwd=passwd)

  opener = urllib2.build_opener(auth_handler)
  # ...and install it globally so it can be used with urlopen.
  urllib2.install_opener(opener)


def request(url):
  result = {'success': False, 'code': 0, 'data': '', 'url': '', 'info': ''}

  req = urllib2.Request(url)
  req.add_header('Referer', 'http://www.eso.org/')
  req.add_header('Content-Type', 'text/plain;charset=utf-8')
  req.add_header('User-Agent', 'ALMA-OT automated test')

  try:
    r = urllib2.urlopen(req)
  except urllib2.HTTPError, e:
    result['code'] = e.code
    result['info'] = e.msg
  except urllib2.URLError, e:
    result['info'] = e.reason
#  except RuntimeError, e:
#    # This exception handles a bug in the urllib2 in Python 2.6.5
#    # resulting in infinite recursion when web authentication fails
#    result['url'] = url
#    result['code'] = 401
#    result['info'] = 'Server authentication failed.'
  except Exception, e:
    result['url'] = url
    result['code'] = -1
    result['info'] = '%s' % e
  else:
    result['data'] = r.read()
    result['success'] = True
    result['code'] = r.code
    result['url'] = r.geturl()
    result['info'] = '%s' % r.info()

  return result


def getPage(submissionService, uri, user=None, passwd=None):
  initOpener(submissionService, user, passwd)
  result = request(uri)
  print  result 

  return result

