#!/usr/bin/env bash

# ./run-template.sh OUTPUT_DIR DOMAIN_NAME INPUT_JSON
#   OUTPUT_DIR  出力ディレクトリ
#   DOMAIN_NAME ドメイン名
#   INPUT_DATA  入力データ(JSON)
OUTPUT_DIR=${1:-/tmp/example.org}
DOMAIN_NAME=${2:-example.org}
INPUT_JSON=${3:-example.org.json}
FORMAT=json

# Requirements: pip install -U jinja2-cli
# jinja2コマンドが存在しない場合は中断する
if ! command -v jinja2 2>&1 > /dev/null; then
    echo "jinja2 (jinja2-cli python package) not found."
    exit 1
fi


# ディレクトリが存在している場合は中断する
if [ -d "${OUTPUT_DIR}" ]; then
    echo "Output directory ${OUTPUT_DIR} already exists."
    exit 1
fi

echo "Output directory: ${OUTPUT_DIR}"
echo "Domain name: ${DOMAIN_NAME}"

mkdir -p "${OUTPUT_DIR}"
RESULT=$?

# ディレクトリが作成できなかった場合は中断する
if [ ! $RESULT -eq 0 ]; then
    echo "Output directory ${OUTPUT_DIR} cannot be created."
    exit 1
fi

#
for file_name in create.sh openssl_csr_san.cnf openssl_intermediate.cnf openssl_root.cnf; do
    template="templates/${file_name}.j2"
    output="${OUTPUT_DIR}/${file_name}"

    echo "Generate ${file_name}"
    jinja2 --strict --format="${FORMAT}" "${template}" "${INPUT_JSON}" > "${output}"
    echo "...done."
done
