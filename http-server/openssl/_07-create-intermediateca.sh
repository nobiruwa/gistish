#!/usr/bin/env bash

openssl genrsa \
        -aes256 \
        -out $INTERMEDIATECA_PRIVATE_KEY \
        -passout pass:$INTERMEDIATECA_PASSWORD \
        $INTERMEDIATECA_LENGTH
