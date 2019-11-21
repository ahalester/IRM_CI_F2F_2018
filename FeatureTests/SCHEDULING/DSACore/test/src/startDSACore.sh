#!/bin/bash

# This is the same script that runs in the jenkins server. Please keep
# compatibility between your development machine and jenkins deployments.
# BEGIN JENKINS PARAMETRIZATION
# The folowing section of export emulates parametrization of jenkins:
#export CON_STR="alma/almafordev@//SWDB.APOTEST.ALMA.CL/ALMAI1"
#export CON_STR="alma/almafordev@//SWDB.APOTEST.ALMA.CL/ALMAI2"
#export CON_STR="alma/almafordev@//SWDB.APOTEST.ALMA.CL/ALMAI3"
#export CON_STR="alma/alma\$dba@//oraosf.osf.alma.cl/ONLINE.OSF.CL"
if [ -z $CON_STR ]; then
    export CON_STR="alma/alma\$dba@//oraosf.osf.alma.cl/ONLINE.OSF.CL"
fi
if [ -z $POL_FILE_PATH ]; then
    export POL_FILE_PATH="/home/ahoffsta/git-repos/configurations/OFFLINE/DSA/"
fi
if [ -z $RELEASE ]; then
    export RELEASE="2018jul"
fi
#export CLEAN_CONDA_ENVIRONMENT="y"
# END JENKINS PARAMETRIZATION

# Reinstall Miniconda
if [ ! -z $CLEAN_CONDA_ENVIRONMENT ]; then 
	# Clean previous installation just in case
	rm -rf ~/miniconda ~/miniconda.sh
	rm -rf ~/.condarc ~/.conda ~/.continuum
	wget https://repo.continuum.io/miniconda/Miniconda2-latest-Linux-x86_64.sh -O ~/miniconda.sh
	bash ~/miniconda.sh -b -p $HOME/miniconda
fi
export PATH="$HOME/miniconda/bin:$PATH"

#!/bin/bash
if [ -z $WORKSPACE ]; then
    export REPOS_LOCATION="$HOME/git-repos/almasw"
else
    export REPOS_LOCATION="$WORKSPACE"
fi
export INSTALL_DIR="$REPOS_LOCATION/SCHEDULING-SERVICES/"
export DSA="$INSTALL_DIR/DSA/src/"
export APDM_C3="${DSA}/Documents/apdm_CYCLE3/"
export APDM_PREFIX="${DSA}/Documents/apdm/"
if [ -z $POL_FILE_PATH ]; then
    export POL_FILE_PATH="${DSA}/conf/"
fi
export PYTHONPATH="$PYTHON_PATH:$DSA"

mkdir -p $APDM_PREFIX
mkdir -p $APDM_C3
mkdir -p $REPOS_LOCATION/test-reports

# Reinstall DSACore env
if [ ! -z $CLEAN_CONDA_ENVIRONMENT ]; then 
    # WE REINSTALL
    if [ 1 -eq $(conda info --envs | grep dsacore | wc -l) ]; then
    	conda env remove -y -n dsacore
    fi
    conda env create -f $DSA/minipy.yml -n dsacore
    source activate dsacore
    conda install -y ipython
    conda install -y pip
    mkdir -p $APDM_C3
    mkdir -p $APDM_PREFIX
    pip install -r $REPOS_LOCATION/IRM/FeatureTests/SCHEDULING/DSACore/requirements.txt
else
    # WE DO NOT REINSTALL
    source activate dsacore 
fi

# Test cases execution
export PYTHONPATH=$REPOS_LOCATION/IRM/FeatureTests/SCHEDULING/DSACore/test/src/:$PYTHONPATH

