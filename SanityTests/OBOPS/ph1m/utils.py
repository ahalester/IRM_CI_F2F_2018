#!/usr/bin/env python

import re 


def configFileParser(myFile):
    properties = {}
    with open(myFile) as inputFile:
        for line in inputFile:
           if not line.startswith('#') and (not line.strip() in ['\n','\r\n', '']):
              key = line.split('=')[0]
              value = line.split('=')[1].replace('\n','').replace('\r','') 
              properties[key] = value
    return properties 


def tnsnameParser(myTnsname):
    DB ={'name' : ''}
    with open(myTnsname) as inputFile:
       text = inputFile.read()
    # Remove comments
    text = re.sub(r'#[^\n]*\n', '\n', text)
    #Remove empty lines
    text = re.sub(r'( *\n *)+', '\n', text.strip())
    # Split into database entries by balancing the parentheses.
    databases = []
    start = 0
    c = 0
    while c < len(text):
        parens = 0
        c = text.find('(')
        while c < len(text):
            if text[c] == '(':
                parens += 1
            elif text[c] == ')':
                parens -= 1
            c += 1
            if parens == 0:
                break
       
        databases.append(text[start:c].strip())
        text = text[c:]
        c = 0
    # Parse each database entry, building a dictionary of dictionaries
    # mapping from database name to database properties.
    database_dicts = {}
    for database in databases:
        # Get the database name and a set of (name, value) pairs.
        name = re.match(r'(\w+)', database).group(1)
        names_and_values = re.findall(r'(?i)([a-z]+)\s*=\s*([a-z0-9-\.]+)', database)
       
        # Build a dictionary from them
        database_dict = dict(names_and_values)
        database_dicts[name] = database_dict

    return database_dicts





if __name__ == '__main__':
    myFile = '/etc/offline/config/archiveConfig.properties'
    print (configFileParser(myFile))
    myFile = '/etc/offline/config/obopsConfig.properties'
    print configFileParser(myFile)
    myFile = '/etc/offline/config/tnsnames.ora'
    print str(tnsnameParser(myFile)['ALMA_OFFLINE'])
