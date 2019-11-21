#!/usr/bin/env python
import requests
import json
import git
import argparse
from git import Git

username = 'almamgr'
password = 'c0blmf!'
host_bb = 'https://bitbucket.sco.alma.cl/'
host_jira = 'https://ictjira.alma.cl/'

class NotFoundException(Exception):
    pass

def get_open_pull_requests(project='ALMA', repo='almasw'):
    url = host_bb + 'rest/api/1.0/projects/' + project + '/repos/' + repo + '/pull-requests'
    params = {'state': 'OPEN', 'order': 'OLDEST', 'limit': 1000}
    r = requests.get(url, params=params, auth=(username, password))
    if r.status_code != 200:
        print r.text
        return False
    from StringIO import StringIO
    io = StringIO(r.text)
    return json.load(io)


def get_ictjira_ticket_associated(pull_request_id=None, project='ALMA', repo='almasw'):
    if pull_request_id is None:
        raise Exception("pull_request_id could not be None")
    url = host_bb + 'rest/jira/1.0/projects/' + project + '/repos/' + repo + '/pull-requests/' + pull_request_id + '/issues'
    r = requests.get(url, auth=(username, password))
    if r.status_code != 200:
        print r.text
        raise Exception(r.text)
    from StringIO import StringIO
    io = StringIO(r.text)
    #print 'io.key now:'
    #print io.key
    return json.load(io)


def get_jira_data(ticket_id=None):
    if ticket_id is None:
        raise Exception("ticket_id could not be None")
    url = host_jira + 'rest/api/latest/issue/' + ticket_id
    print url
    r = requests.get(url, auth=(username, password))
    if not (("ICT-" in r.text) or ("SCCB-" in r.text)):
        return False
    if r.status_code == 404:
        raise NotFoundException(url)
    if r.status_code != 200:
        print r.text
        raise Exception(str(r.status_code) + ' ' + r.text)
    from StringIO import StringIO
    io = StringIO(r.text)
    print 'el ticket is ' + ticket_id
    return json.load(io)


def get_pull_requests_to_merge():
    pull_requests = get_open_pull_requests()
    ret_val = {}
    for pr in pull_requests['values']:
        if pr['properties'].has_key('mergeResult'):
            print pr['id'], pr['properties']['mergeResult']['outcome'], pr['fromRef']['displayId']
        else:
            print pr['id'], pr['fromRef']['displayId']
        if pr['toRef']['displayId'] != 'master':
            continue
        ret_val[pr['id']] = {}
        ret_val[pr['id']]['branch'] = pr['fromRef']['displayId']
        ret_val[pr['id']]['fixVersion'] = []
        for ticket_id in get_ictjira_ticket_associated(str(pr['id'])):
            try:
                jira_info = get_jira_data(ticket_id['key'])
            except NotFoundException as ex:
                print 'No info found for', ex
            try:
                if ticket_id['key'].startswith('ICT'):
                    print jira_info['fields']['fixVersions'][0]['name'], jira_info['fields']['status']['name']
                    ret_val[pr['id']]['fixVersion'].append(jira_info['fields']['fixVersions'][0]['name'])
                elif ticket_id['key'].startswith('SCCB'):
                    try:
                        print jira_info['fields']['customfield_10500'][0]['name'], jira_info['fields']['status']['name']
                        ret_val[pr['id']]['fixVersion'].append(jira_info['fields']['customfield_10500'][0]['name'])
                    except TypeError:
                        print 'Ignoring', ticket_id['key'], 'given branch is null.'
            except IndexError:
                print "No info for", ticket_id['key']
        print "-------------------------------------------"
    return ret_val


def init():
    parser = argparse.ArgumentParser(description='Check for pull requests in bitbucket and merge the '
                                                 'branches to a given branch locally.')
    parser.add_argument('repo_path', help='The git repo path to use.', type=str)
    parser.add_argument('dest_branch', help='The destination branch on which the merge will be done.', type=str)
    parser.add_argument('fix_version', help='fixVersion reported in associated jira tickets to check.')

    return parser.parse_args()


def main():
    args = init()
    repo = git.Repo(args.repo_path)
    origin = repo.remotes['origin']
    origin.fetch()

    master = repo.branches.master
    #master = repo.heads[0]
    master.checkout()
    origin.pull()

    try:
        new_branch = repo.create_head(args.dest_branch, master)
        new_branch.checkout()
    except OSError:
        new_branch = repo.branches[args.dest_branch]
        new_branch.checkout()
        try:
            origin.pull()
        except git.exc.GitCommandError:
            pass  # Nothing to do, branch is not at upstream

    branches_dict = get_pull_requests_to_merge()
    g = Git(args.repo_path)
    g.merge('master')
    for b in branches_dict:
        merging = False
        if branches_dict[b].has_key('fixVersion'):
            for branch in branches_dict[b]['fixVersion']:
                if branch == args.fix_version:
                    merging = True
                    print 'buscar argumento del numero del ticket'
                    print 'Merging origin/' + branches_dict[b]['branch']
                    print 'we need to write in the the ' + str(branches_dict[b])
                    g.merge('origin/' + branches_dict[b]['branch'])
                    print 'we need to write in the the ' + str(branches_dict[b])
                    break
        if not merging:
            if branches_dict[b].has_key('fixVersion'):
                print 'Ignoring pull request', branches_dict[b]['branch'], 'given fix version is not',\
                    args.fix_version, ':', branches_dict[b]['fixVersion']
            else:
                print 'Ignoring pull request', branches_dict[b]['branch'], 'given fix version is None'

    print 'The merge seems to be ok. You are ready to push the branch'


main()
