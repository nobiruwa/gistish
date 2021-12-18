#!/usr/bin/env bash

openssl x509 \
        -days $ROOTCA_DAYS \
        -in $ROOTCA_CSR \
        -passin pass:$ROOTCA_PASSWORD \
        -req \
        -signkey $ROOTCA_PRIVATE_KEY \
        -out $ROOTCA_CERTIFICATE

cp "${ROOTCA_CERTIFICATE}" rootca.crt
