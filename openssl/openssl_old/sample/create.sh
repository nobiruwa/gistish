#!/usr/bin/env bash

DOMAIN_NAME="${CHANGE_HERE_DOMAIN_NAME}"

ORIG_DIR=`pwd`
SUBJ_O="Private CA for ${DOMAIN_NAME}"

ROOTCA_DIR="${ORIG_DIR}/rootca"
ROOTCA_DAYS=36500
ROOTCA_LENGTH=4096
ROOTCA_PASSWORD=changeit
ROOTCA_SUBJ_C=JP
ROOTCA_SUBJ_ST=Tokyo
ROOTCA_SUBJ_L=Kita
ROOTCA_SUBJ_O="${SUBJ_O}"
# Ensure that when filling out the “Common Name” variable that you use the CA server + Domain name of the network
ROOTCA_SUBJ_CN="rootca.${DOMAIN_NAME}" # CHANGE THIS
ROOTCA_SUBJ="/C=${ROOTCA_SUBJ_C}/ST=${ROOTCA_SUBJ_ST}/L=${ROOTCA_SUBJ_L}/O=${ROOTCA_SUBJ_O}/CN=${ROOTCA_SUBJ_CN}"

ROOTCA_CONFIG="${ORIG_DIR}/openssl_root.cnf"
ROOTCA_PRIVATE_KEY="${ROOTCA_DIR}/private/rootca.${DOMAIN_NAME}.key.pem"
ROOTCA_CERTIFICATE="${ROOTCA_DIR}/certs/rootca.${DOMAIN_NAME}.crt.pem"

INTERMEDIATECA_DIR="${ORIG_DIR}/intermediateca"
INTERMEDIATECA_DAYS=36500
INTERMEDIATECA_LENGTH=4096
INTERMEDIATECA_PASSWORD=changeit
INTERMEDIATECA_SUBJ_C=JP
INTERMEDIATECA_SUBJ_ST=Tokyo
INTERMEDIATECA_SUBJ_L=Kita
# ROOTCA_SUBJ_OとINTERMEDIATECA_SUBJ_Oは一致させる
INTERMEDIATECA_SUBJ_O="${SUBJ_O}"
# Ensure that when filling out the “Common Name” variable that you use the CA server + Domain name of the network
INTERMEDIATECA_SUBJ_CN="intermediateca.${DOMAIN_NAME}"
INTERMEDIATECA_SUBJ="/C=${INTERMEDIATECA_SUBJ_C}/ST=${INTERMEDIATECA_SUBJ_ST}/L=${INTERMEDIATECA_SUBJ_L}/O=${INTERMEDIATECA_SUBJ_O}/CN=${INTERMEDIATECA_SUBJ_CN}"

INTERMEDIATECA_CONFIG="${ORIG_DIR}/openssl_intermediate.cnf"
INTERMEDIATECA_SAN_CONFIG="${ORIG_DIR}/openssl_csr_san.cnf"
INTERMEDIATECA_PRIVATE_KEY="${INTERMEDIATECA_DIR}/private/intermediateca.${DOMAIN_NAME}.key.pem"
INTERMEDIATECA_CSR="${INTERMEDIATECA_DIR}/csr/intermediateca.${DOMAIN_NAME}.csr.pem"
INTERMEDIATECA_CERTIFICATE="${INTERMEDIATECA_DIR}/certs/intermediateca.${DOMAIN_NAME}.crt.pem"
INTERMEDIATECA_CHAIN_CERTIFICATE="${INTERMEDIATECA_DIR}/certs/chain.${DOMAIN_NAME}.crt.pem"

SERVER_SUBJ_CN="${DOMAIN_NAME}"
SERVER_SUBJ="/C=${INTERMEDIATECA_SUBJ_C}/ST=${INTERMEDIATECA_SUBJ_ST}/L=${INTERMEDIATECA_SUBJ_L}/O=${INTERMEDIATECA_SUBJ_O}/CN=${SERVER_SUBJ_CN}"

SERVER_PRIVATE_KEY="${INTERMEDIATECA_DIR}/private/${DOMAIN_NAME}.key.pem"
SERVER_CSR="${INTERMEDIATECA_DIR}/csr/${DOMAIN_NAME}.csr.pem"
SERVER_CERTIFICATE="${INTERMEDIATECA_DIR}/certs/${DOMAIN_NAME}.crt.pem"
SERVER_CHAIN_CERTIFICATE="${ORIG_DIR}/chain.${DOMAIN_NAME}.crt.pem"

# Creating the Root CA

mkdir "${ROOTCA_DIR}"

mkdir "${ROOTCA_DIR}/newcerts" "${ROOTCA_DIR}/certs" "${ROOTCA_DIR}/crl" "${ROOTCA_DIR}/private" "${ROOTCA_DIR}/requests"

touch "${ROOTCA_DIR}/index.txt"
touch "${ROOTCA_DIR}/index.txt.attr"
echo '1000' > "${ROOTCA_DIR}/serial"

# Generate the Root private key
openssl genrsa \
        -aes256 \
        -passout "pass:${ROOTCA_PASSWORD}" \
        -out "${ROOTCA_PRIVATE_KEY}"

# Signing the Root Certificate
openssl req \
        -new \
        -x509 \
        -sha512 \
        -config "${ROOTCA_CONFIG}" \
        -extensions v3_ca \
        -days "${ROOTCA_DAYS}" \
        -set_serial 0 \
        -key "${ROOTCA_PRIVATE_KEY}" \
        -passin "pass:${ROOTCA_PASSWORD}" \
        -subj "${ROOTCA_SUBJ}" \
        -out "${ROOTCA_CERTIFICATE}"

# Creating an Intermediate Certificate Authority

mkdir "${INTERMEDIATECA_DIR}"

mkdir "${INTERMEDIATECA_DIR}/certs" "${INTERMEDIATECA_DIR}/newcerts" "${INTERMEDIATECA_DIR}/crl" "${INTERMEDIATECA_DIR}/csr" "${INTERMEDIATECA_DIR}/private"

touch "${INTERMEDIATECA_DIR}/index.txt"
touch "${INTERMEDIATECA_DIR}/index.txt.attr"
echo 1000 > "${INTERMEDIATECA_DIR}/crlnumber"
echo 1234 > "${INTERMEDIATECA_DIR}/serial"

# Creating the private key and certificate signing request for the Intermediate CA
openssl req \
        -new \
        -newkey rsa:4096 \
        -config "${INTERMEDIATECA_CONFIG}" \
        -subj "${INTERMEDIATECA_SUBJ}" \
        -passout "pass:${INTERMEDIATECA_PASSWORD}" \
        -keyout "${INTERMEDIATECA_PRIVATE_KEY}" \
        -out "${INTERMEDIATECA_CSR}"

# Creating the intermediate certificate
# ** Notice that the root CA configurtion (openssl_root.cnf) is used.
openssl ca \
        -config "${ROOTCA_CONFIG}" \
        -extensions v3_intermediate_ca \
        -days 36500 \
        -notext \
        -md sha512 \
        -passin "pass:${INTERMEDIATECA_PASSWORD}" \
        -in "${INTERMEDIATECA_CSR}" \
        -out "${INTERMEDIATECA_CERTIFICATE}"

# Creating the certificate chain
cat "${INTERMEDIATECA_CERTIFICATE}" "${ROOTCA_CERTIFICATE}" > "${INTERMEDIATECA_CHAIN_CERTIFICATE}"

# Creating server certificates

# Creating the key and certificate signing request

openssl req \
        -newkey rsa:2048 \
        -nodes \
        -subj "${SERVER_SUBJ}"\
        -config "${INTERMEDIATECA_SAN_CONFIG}" \
        -keyout "${SERVER_PRIVATE_KEY}" \
        -out "${SERVER_CSR}"

# Creating the certificate by signing the signing request with the intermediate CA

openssl ca \
        -days 36500 \
        -notext \
        -md sha512 \
        -config "${INTERMEDIATECA_CONFIG}" \
        -extensions server_cert \
        -passin "pass:${INTERMEDIATECA_PASSWORD}" \
        -in "${SERVER_CSR}" \
        -out "${SERVER_CERTIFICATE}"

cat "${SERVER_CERTIFICATE}" "${INTERMEDIATECA_CHAIN_CERTIFICATE}" > "${SERVER_CHAIN_CERTIFICATE}"
cp "${SERVER_PRIVATE_KEY}" .
