#!/usr/bin/env bash

cd $(pwd)

docker run -p 18080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -v $(pwd)/data:/opt/keycloak/data quay.io/keycloak/keycloak:21.1 start-dev
