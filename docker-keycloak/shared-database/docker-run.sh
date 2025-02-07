#!/usr/bin/env bash

cd $(pwd)

docker-compose up --remove-orphans
# Ctrl-C後に実行
docker-compose down --remove-orphans
