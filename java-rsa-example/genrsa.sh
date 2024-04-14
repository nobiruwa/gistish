#!/usr/bin/env bash

openssl req -newkey rsa:2048 -x509 -keyout 01_key.pem -out 01_cert.pem -days 3650
# Enter PEM pass phrase: test
# Verifying - Enter PEM pass phrase: test
# -----
# You are about to be asked to enter information that will be incorporated
# into your certificate request.
# What you are about to enter is what is called a Distinguished Name or a DN.
# There are quite a few fields but you can leave some blank
# For some fields there will be a default value,
# If you enter '.', the field will be left blank.
# -----
# Country Name (2 letter code) [AU]:JP
# State or Province Name (full name) [Some-State]:Tokyo
# Locality Name (eg, city) []:
# Organization Name (eg, company) [Internet Widgits Pty Ltd]:Paper Company
# Organizational Unit Name (eg, section) []:
# Common Name (e.g. server FQDN or YOUR name) []:rsa.co.jp
# Email Address []:

openssl pkcs12 -export -in 01_cert.pem -inkey 01_key.pem -out 02_certificate.p12 -name "certificate"
# Enter pass phrase for 01_key.pem: test
# Enter Export Password: test2
# Verifying - Enter Export Password: test2

keytool -importkeystore -srckeystore 02_certificate.p12 -srcstoretype pkcs12 -destkeystore 03_cert.jks
# Importing keystore 02_certificate.p12 to 03_cert.jks...
# Enter destination keystore password:  Password3
# Re-enter new password: Password3
# Enter source keystore password:  test2
# Entry for alias certificate successfully imported.
# Import command completed:  1 entries successfully imported, 0 entries failed or cancelled

openssl rsa -inform pem -in 01_key.pem -outform der -out 04_key.der
# Enter pass phrase for 01_key.pem:test
# writing RSA key

openssl rsa -in 01_key.pem -pubout -out 05_public.pem
# Enter pass phrase for 01_key.pem:test
# writing RSA key

openssl rsa -in 01_key.pem -pubout -outform der -out 06_public.der
# Enter pass phrase for 01_key.pem:test
# writing RSA key
