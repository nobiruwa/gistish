#!/usr/bin/env bash

cd "$(dirname $0)"

OUTPUT_FILE="$(basename $0).txt"

. 00_configuration.sh

REQUEST=$(jq -c -n \
             --arg username "${REPO_USER_NAME}" \
             --arg password "${REPO_USER_PASSWORD}" \
             --arg email "${REPO_USER_EMAIL}" \
             '$ARGS.named')

curl -sS "${GITEA_URL_ADMIN_USERS}" \
     -X POST \
     -o "${OUTPUT_FILE}" \
     -w "%{http_code}\n" \
     -H "Accept: application/json" \
     -H "Authorization: token ${ADMIN_TOKEN}" \
     -H "Content-Type: application/json" \
     -d "${REQUEST}"

if [ -f "${OUTPUT_FILE}" ]; then
    cat "${OUTPUT_FILE}"
fi
