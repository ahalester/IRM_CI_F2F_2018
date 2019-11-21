
all: dependencies test

dependencies:
	pip install -r ../requirements.txt

test-webservice:
	nosetests --verbosity=3 \
	--with-xunit --xunit-file=./test-reports/webserviceFeatures.xml \
	--tc-file=./config/remote.cfg \
	--tc=dsacore_server.url:https://${RELEASE}.asa-test.alma.cl/dsacore_c5/XMLRPC \
	src/ICT-8107-isSimultaneous.py \
	src/ICT-8815-sbNote.py \
	src/ICT-8841-ElevationLimitDuringObservation.py \
	src/ICT-9681-TimeConstraint.py \
	src/ICT-9922-EmptyAnswer.py \
	src/ICT-11731-ExtraIsSimultaneousFields.py

test-libraries:
	nosetests --verbosity=3 \
	--with-xunit --xunit-file=./test-reports/librariesFeatures.xml \
	--tc-file=./config/remote.cfg \
	src/DSAAllLibrariesTestCases.py

test-libraries-coverage:
	nosetests --verbosity=3 \
	--with-coverage --cover-erase \
	--cover-html --cover-html-dir=test-reports/coverage-html \
	--cover-xml --cover-xml-file=test-reports/coverage.xml \
	--cover-package=DsaAlgorithm --cover-package=DsaArrayResolution --cover-package=DsaCalibration  \
	--cover-package=DsaConstants  --cover-package=DsaConverter  --cover-package=DsaDataBase  \
	--cover-package=DsaGetCycle --cover-package=DsaSelectors --cover-package=DsaTools \
	--cover-package=DsaXmlParsers --cover-package=arrayConfigurationTools --cover-package=shadowingCalculator \
	--cover-package=visibiltyTools \
	--with-xunit --xunit-file=./test-reports/librariesFeatures.xml \
	--tc-file=./config/remote.cfg \
	src/DSAAllLibrariesTestCases.py

test: test-webservice test-libraries
