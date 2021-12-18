#!/usr/bin/env bash

openssl req \
        -config $CONFIG \
        -new \
        -key $INTERMEDIATECA_PRIVATE_KEY \
        -passin pass:$INTERMEDIATECA_PASSWORD \
        -out $INTERMEDIATECA_CSR \
        -subj "$INTERMEDIATECA_SUBJ"
