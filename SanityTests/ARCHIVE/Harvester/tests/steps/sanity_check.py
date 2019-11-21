from behave       import *
from hamcrest     import *
from ConfigParser import *
from utils        import fs, console

import os
import re
import psutil
import requests
import xml.dom.minidom
import cx_Oracle

# --------------------------------
#             Given's
# --------------------------------

@given('I have installed "{program}"')
def step_impl(context, program):
    assert_that(console.term_exec("which %s" % program), not contains_string('no %s in' % program))

@given('logs are stored in "{logs_path}"')
def step_impl(context, logs_path):
    context.logs_path = logs_path

@given('I use "{url}" as bibliography API')
def step_impl(context, url):
    context.bib_api = url

@given('"{env_var}" environment variable is set')
def step_impl(context, env_var):
    assert_that(os.environ, has_key(env_var))
    context.acsdata_path = os.environ.get(env_var)
    
@given('the configuration file "{config_file}" has the property "{property_key}" set with value "{property_value}"')
def step_impl(context, config_file, property_key, property_value):
   assert_that(context.parsed_config[config_file].get('properties', property_key), property_value)

@given('I use tnsnames.ora')
def step_impl(context):
    tnsnames_file = file(os.path.join(context.acsdata_path, 'config/tnsnames.ora')).read()

    context.tnsnames = {}
    tns_re = "^(\w+?)\s?=.*?HOST\s?=\s?(.+?)\).*?PORT\s?=\s?(\d+?)\).*?SERVICE_NAME\s?=\s?(.+?)\)"
    for match in re.finditer(tns_re, tnsnames_file, re.M+re.S):
        t = match.groups()
        context.tnsnames[t[0]] = "%s:%s/%s" % t[1:]

@given('I use properties file "{properties_file}"')
def step_impl(context, properties_file):
    if not hasattr(context, 'parsed_config'): context.parsed_config = dict()
    print(context.parsed_config)
    with open(os.path.join(context.acsdata_path, 'config', properties_file)) as f:
        parsed_property = filter(lambda x: len(x) == 2, [line.strip().split('=') for line in f])
        defaults = dict(parsed_property)
    config = ConfigParser(defaults)
    config.add_section('properties')

    context.parsed_config[properties_file] = config

@given('I use the ASDM with uid "{asdm_uid}"')
def step_impl(context, asdm_uid):
    context.asdm_uid = asdm_uid

@given('I use "{db_type}" database defined in "{config_file}"')
def step_impl(context, db_type, config_file):
   context.db_name = context.parsed_config[config_file].get('properties', '%s.connection' % db_type).split('@')[1] 
   context.db_user = context.parsed_config[config_file].get('properties', '%s.user' % db_type)
   context.db_pass = context.parsed_config[config_file].get('properties', '%s.passwd' % db_type) 

# --------------------------------
#             When's
# --------------------------------

@when('I look for the free space on the mountpoint of "{path}"')
def step_impl(context, path):
    context.mountpoint_freespace_bytes = psutil.disk_usage(fs.find_mount_point(path)).free

@when('I look for total RAM size')
def step_impl(context):
    context.total_ram_size = psutil.virtual_memory().total

@when('I fetch bibliography for bibcode "{bibcode}"')
def step_impl(context, bibcode):
    context.telbib_response = requests.get(context.bib_api + "/api.php?bibcode=" + bibcode)
    
@when('I ping to defined database server')
def step_impl(context):
    context.ping_status = console.term_exec("ping -c 1 %s" % "orasw.apotest.alma.cl")
 
@when('I run harvester in dry-run mode for given ASDM')
def step_impl(context):
    context.stdout = console.term_exec('~/scripts/harvest -c -a %s -x -y ALL -d -uid %s' % (os.path.join(context.acsdata_path, 'config/', 'archiveConfig.properties'), context.asdm_uid))

@when('I connect to database server')
def step_impl(context):
    print(context.tnsnames)
    context.database_conn = cx_Oracle.connect('%s/%s@%s' % (context.db_user, context.db_pass, context.tnsnames[context.db_name]))

# --------------------------------
#             Then's
# --------------------------------

@then('total RAM size should be greater than "{size}"')
def step_impl(context, size):
    assert_that(context.total_ram_size, greater_than(fs.human2bytes(size)))

@then('mountpoint of "{path}" should has at least "{size}" free space')
def step_impl(context, path, size):
    assert_that(context.mountpoint_freespace_bytes, greater_than_or_equal_to(fs.human2bytes(size)))

@then('version should be "{version}"')
def step_impl(context, version):
    assert_that(version, equal_to(version))

@then('I should get a response with status code "{status_code:d}"')
def step_impl(context, status_code):
    assert_that(context.telbib_response.status_code, equal_to(status_code))
    
@then('I should get the bibliography in xml format')
def step_impl(context):
    xml.dom.minidom.parseString(context.telbib_response.content)
    
@then('I should get ping response')
def step_impl(context):
    assert_that(context.ping_status, not contains_string('cannot resolve'))

@then('I should not get errors in stdout')
def step_impl(context):
    assert_that(context.stdout, is_not(any_of(contains_string('Error'), contains_string('ERROR'), contains_string('SEVERE'), contains_string('CRITICAL'))))

@then('ASA tables should exist')
def step_impl(context):
    asa_tables = ['ASA_BIBLIOGRAPHY', 'ASA_COLUMNS', 'ASA_ENERGY', 'ASA_MAIL_TEMPLATES', 'ASA_OUS', 'ASA_OUS_REDSHIFT', 'ASA_OUS_TEMP', 'ASA_PRODUCT_FILES', 'ASA_PROJECT', 'ASA_PROJECT_BIBLIOGRAPHY', 'ASA_SCIENCE', 'ASA_TABLES', 'ASA_TAP_SCHEMAS']
    
    asa_tables_sql_list = ",".join(map(lambda x: "'" + x + "'", asa_tables))
    sql_statement = "select count(*) from all_objects where object_type in ('TABLE','VIEW') and object_name in (%s)" % asa_tables_sql_list
    print('sql:', sql_statement)
    tables_num = int(context.database_conn.cursor().execute(sql_statement).fetchone()[0])
    assert_that(tables_num, equal_to(len(asa_tables)))
