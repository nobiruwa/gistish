#!/usr/bin/env bash

openssl pkcs12 -export -out rootca.pfx -inkey "${ROOTCA_PRIVATE_KEY}" -in "${ROOTCA_CERTIFICATE}" -passin "pass:${ROOTCA_PASSWORD}" -passout "pass:${ROOTCA_PASSWORD}"
