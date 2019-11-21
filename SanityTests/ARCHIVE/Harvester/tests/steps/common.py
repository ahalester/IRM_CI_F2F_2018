from behave import *
from hamcrest import *

from utils.console import *

@when("I run the following command in a terminal")
def step_impl(context):
    context.stdout = term_exec(context.text)
