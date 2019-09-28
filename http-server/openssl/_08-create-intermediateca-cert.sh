#!/usr/bin/env bash

openssl ca \
        -config $CONFIG \
        -days $INTERMEDIATECA_DAYS \
        -in $INTERMEDIATECA_CSR \
        -passin pass:$INTERMEDIATECA_PASSWORD \
        -extfile $INTERMEDIATECA_EXTFILE \
        -out $INTERMEDIATECA_CERTIFICATE
