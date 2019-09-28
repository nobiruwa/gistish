#!/usr/bin/env bash

if [ x = x"${PREFIX}" ]
then
    echo "Please specify PREFIX."
    exit 1
fi

if [ "${PREFIX}" != "ROOTCA" -a "${PREFIX}" != "INTERMEDIATECA" ]
then
    echo "PREFIX should be either ROOTCA or INTERMEDIATECA."
    exit 2
fi


if [ x = x"${DIR}" ]
then
    echo "Please specify DIR."
    exit 3
fi

# dynamic directory
eval "${PREFIX}_CERTS_DIR=$DIR/certs"
eval "${PREFIX}_CRL_DIR=$DIR/crl"
eval "${PREFIX}_NEW_CERTS_DIR=$DIR/newcerts"
eval "${PREFIX}_PRIVATE_KEY_DIR=$DIR/private"
PRIVATE_KEY_DIR="$DIR/private"

# file
eval "${PREFIX}_CSR=$DIR/cacert.csr"
eval "${PREFIX}_CERTIFICATE=$DIR/cacert.pem"
eval "${PREFIX}_PRIVATE_KEY=$PRIVATE_KEY_DIR/cakey.pem"
eval "${PREFIX}_PRIVATE_KEY_NOPASS=$PRIVATE_KEY_DIR/cakey-nopass.pem"
eval "${PREFIX}_RANDFILE=$DIR/private/.rand"
