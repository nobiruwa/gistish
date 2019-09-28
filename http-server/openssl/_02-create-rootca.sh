#!/usr/bin/env bash

openssl genrsa \
        -aes256 \
        -out $ROOTCA_PRIVATE_KEY \
        -passout pass:$ROOTCA_PASSWORD \
        $ROOTCA_LENGTH
