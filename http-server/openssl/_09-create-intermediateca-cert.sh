#!/usr/bin/env bash

openssl ca \
        -config $CONFIG \
        -days $INTERMEDIATECA_DAYS \
        -in $INTERMEDIATECA_CSR \
        -cert $ROOTCA_CERTIFICATE \
        -keyfile $ROOTCA_PRIVATE_KEY \
        -passin pass:$ROOTCA_PASSWORD \
        -extfile $INTERMEDIATECA_EXTFILE \
        -out $INTERMEDIATECA_CERTIFICATE

# 余分な文字列を取り除く
openssl x509 \
        -in $INTERMEDIATECA_CERTIFICATE \
        -out $INTERMEDIATECA_CERTIFICATE

cp "${INTERMEDIATECA_CERTIFICATE}" intermediateca.crt
