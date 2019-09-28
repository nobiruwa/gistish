#!/usr/bin/env bash

openssl req \
        -config $CONFIG \
        -new \
        -key $ROOTCA_PRIVATE_KEY \
        -passin pass:$ROOTCA_PASSWORD \
        -out $ROOTCA_CSR \
        -subj "$ROOTCA_SUBJ"
