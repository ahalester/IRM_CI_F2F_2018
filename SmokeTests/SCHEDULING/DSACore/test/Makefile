
all: dependencies test

dependencies:
	pip install -r ../requirements.txt

test: dependencies
	nosetests --verbosity=3 \
	--tc-file=config/remote.cfg \
	--tc=dsacore_server.url:https://${RELEASE}.asa-test.alma.cl/dsacore_c5/XMLRPC \
	--with-xunit \
	--where=src/ \
	SanityCheckTestCase.py \
	RemoteRPCMethodTestCase.py \
	Test04.py	
