#!/bin/bash

INTEGRATION_TESTING_DIR=$1
VERSION=$2

cd $INTEGRATION_TESTING_DIR

# Clean previous test generated data
rm -rf batch_test_2018*
rm -rf test_run_2018*

bash batch_test_product_ingestor.sh $VERSION

