#!/usr/bin/env bash

# common parameters
CONFIG=./openssl.cnf

# rootca parameters
ROOTCA_DIR=${ROOTCA_DIR:-rootca}
ROOTCA_DATABASE=$ROOTCA_DIR/index.txt
ROOTCA_DATABASE_ATTR=$ROOTCA_DIR/index.txt.attr
ROOTCA_CRL=$ROOTCA_DIR/crl.pem
ROOTCA_SERIAL=$ROOTCA_DIR/serial
ROOTCA_CRLNUMBER=$ROOTCA_DIR/crlnumber

ROOTCA_DAYS=36500
ROOTCA_LENGTH=4096
ROOTCA_PASSWORD=changeit
ROOTCA_SUBJ_C=JP
ROOTCA_SUBJ_ST=Tokyo
ROOTCA_SUBJ_L=Kita
ROOTCA_SUBJ_O="Private Company"
ROOTCA_SUBJ_CN="Private Root CA"
ROOTCA_SUBJ="/C=${ROOTCA_SUBJ_C}/ST=${ROOTCA_SUBJ_ST}/L=${ROOTCA_SUBJ_L}/O=${ROOTCA_SUBJ_O}/CN=${ROOTCA_SUBJ_CN}"

## create variables dynamically
PREFIX=ROOTCA DIR="${ROOTCA_DIR}" . ./_00-source-env.sh

# intermediateca parameters
INTERMEDIATECA_DIR=${INTERMEDIATECA_DIR:-intermediateca}
INTERMEDIATECA_EXTFILE=./san.ext

INTERMEDIATECA_DAYS=36500
INTERMEDIATECA_PASSWORD=changeit
INTERMEDIATECA_SUBJ_C=JP
INTERMEDIATECA_SUBJ_ST=Tokyo
INTERMEDIATECA_SUBJ_L=Kita
INTERMEDIATECA_SUBJ_O="Private Company"
INTERMEDIATECA_SUBJ_CN="letsrei.com"
INTERMEDIATECA_SUBJ="/C=${INTERMEDIATECA_SUBJ_C}/ST=${INTERMEDIATECA_SUBJ_ST}/L=${INTERMEDIATECA_SUBJ_L}/O=${INTERMEDIATECA_SUBJ_O}/CN=${INTERMEDIATECA_SUBJ_CN}"

## create variables dynamically
PREFIX=INTERMEDIATECA DIR="${INTERMEDIATECA_DIR}" . ./_00-source-env.sh

if [ ! -f "${CONFIG}" ]
then
    echo "You should create ${CONFIG}. Use openssl.cnf.sample."
    exit 1
fi

if [ ! -f "${INTERMEDIATECA_EXTFILE}" ]
then
    echo "You should create ${INTERMEDIATECA_EXTFILE}. Use san.ext.sample."
    exit 1
fi

# ROOTCA
. ./_01-create-rootca-directory.sh
. ./_02-create-rootca.sh
. ./_03-create-rootca-csr.sh
. ./_04-create-rootca-self-signing-cert.sh
. ./_05-create-rootca-pfx.sh

# INTERMEDIATECA
. ./_06-create-intermediateca-directory.sh
. ./_07-create-intermediateca.sh
. ./_08-create-intermediateca-csr.sh
. ./_09-create-intermediateca-cert.sh
. ./_10-create-intermediateca-cert-nopass.sh
. ./_11-create-intermediateca-pfx.sh
