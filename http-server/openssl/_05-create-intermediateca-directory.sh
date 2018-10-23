# directory
DIR=intermediateca
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
PRIVATE_KEY=$DIR/private/cakey.pem
PRIVATE_KEY_NOPASS=$PRIVATE_KEY_DIR/cakey-nopass.pem
RANDFILE=$DIR/private/.rand
CONFIG=./openssl.cnf
EXTFILE=./san.ext

# create directories
mkdir -p $DIR
mkdir -p $CERTS_DIR
mkdir -p $CRL_DIR
mkdir -p $NEW_CERTS_DIR
mkdir -p $PRIVATE_KEY_DIR

# # create empty files
# touch $DATABASE
# echo 00 > $SERIAL
# touch $CRLNUMBER
# touch $CRL
# touch $PRIVATE_KEY
# touch $RANDFILE

