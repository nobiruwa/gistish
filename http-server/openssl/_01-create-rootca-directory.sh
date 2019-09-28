#!/usr/bin/env bash

# create directories
mkdir -p "$ROOTCA_DIR"
mkdir -p "$ROOTCA_CERTS_DIR"
mkdir -p "$ROOTCA_CRL_DIR"
mkdir -p "$ROOTCA_NEW_CERTS_DIR"
mkdir -p "$ROOTCA_PRIVATE_KEY_DIR"

# # create empty files
touch "$ROOTCA_DATABASE"
touch "$ROOTCA_DATABASE_ATTR"
echo 00 > "$ROOTCA_SERIAL"
# touch $CRLNUMBER
# touch $CRL
# touch $PRIVATE_KEY
# touch $RANDFILE
