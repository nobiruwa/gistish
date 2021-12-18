#!/usr/bin/env bash

cp "${INTERMEDIATECA_CERTIFICATE}" intermediateca.crt

openssl pkcs12 -export -out intermediateca.pfx -inkey "${INTERMEDIATECA_PRIVATE_KEY}" -in "${INTERMEDIATECA_CERTIFICATE}" -passin "pass:${INTERMEDIATECA_PASSWORD}" -passout "pass:${INTERMEDIATECA_PASSWORD}"
