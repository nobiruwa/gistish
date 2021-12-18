#!/usr/bin/env bash

openssl rsa \
        -in $INTERMEDIATECA_PRIVATE_KEY \
        -passin pass:$INTERMEDIATECA_PASSWORD \
        -out $INTERMEDIATECA_PRIVATE_KEY_NOPASS
