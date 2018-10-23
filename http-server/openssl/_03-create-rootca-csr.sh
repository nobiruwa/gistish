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
PASSWORD=changeit
SUBJ_C=JP
SUBJ_ST=Tokyo
SUBJ_L=Kita
SUBJ_O="Private Company"
SUBJ_CN="Private Root CA"
SUBJ="/C=${SUBJ_C}/ST=${SUBJ_ST}/L=${SUBJ_L}/O=${SUBJ_O}/CN=${SUBJ_CN}"

openssl req \
        -config $CONFIG \
        -new \
        -key $PRIVATE_KEY \
        -passin pass:$PASSWORD \
        -out $CSR \
        -subj "$SUBJ"
