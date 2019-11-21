import sys
import subprocess
from hamcrest import *

def term_exec(command):
    pipe = subprocess.Popen("%s 2>&1" % command, stdout=subprocess.PIPE, shell=True)
    output, error = pipe.communicate()
    if error is not None:
        print ("Error: %s" % error)
        sys.exit(1)
    return output.strip()