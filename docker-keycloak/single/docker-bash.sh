#!/usr/bin/env bash

cd $(pwd)

docker run -p 18080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -v $(pwd)/data:/opt/keycloak/data  -it --entrypoint bash quay.io/keycloak/keycloak:21.1
