# directory
DIR=rootca
CERTS_DIR=$DIR/certs
CRL_DIR=$DIR/crl
NEW_CERTS_DIR=$DIR/newcerts
PRIVATE_KEY_DIR=$DIR/private

# file
DATABASE=$DIR/index.txt
CSR=$DIR/cacert.csr
CERTIFICATE=$DIR/cacert.pem
SERIAL=$DIR/serial
CRLNUMBER=$DIR/crlnumber
CRL=$DIR/crl.pem
PRIVATE_KEY=$PRIVATE_KEY_DIR/cakey.pem
PRIVATE_KEY_NOPASS=$PRIVATE_KEY_DIR/cakey-nopass.pem
RANDFILE=$DIR/private/.rand
CONFIG=./openssl.cnf
EXTFILE=./san.ext

# parameters
LENGTH=4096
PASSWORD=changeit

openssl genrsa \
        -aes256 \
        -out $PRIVATE_KEY \
        -passout pass:$PASSWORD \
        $LENGTH
