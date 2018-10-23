# directory
DIR=rootca
CERTS_DIR=$DIR/certs
CRL_DIR=$DIR/crl
NEW_CERTS_DIR=$DIR/newcerts
PRIVATE_KEY_DIR=$DIR/private

# file
DATABASE=$DIR/index.txt
CERTIFICATE=$DIR/cacert.pem
CSR=$DIR/cacert.csr
SERIAL=$DIR/serial
CRLNUMBER=$DIR/crlnumber
CRL=$DIR/crl.pem
PRIVATE_KEY=$PRIVATE_KEY_DIR/cakey.pem
PRIVATE_KEY_NOPASS=$PRIVATE_KEY_DIR/cakey-nopass.pem
RANDFILE=$DIR/private/.rand
CONFIG=./openssl.cnf
EXTFILE=./san.ext

# parameters
PASSWORD=changeit
DAYS=3650

openssl x509 \
        -days $DAYS \
        -in $CSR \
        -passin pass:$PASSWORD \
        -req \
        -signkey $PRIVATE_KEY \
        -out $CERTIFICATE
