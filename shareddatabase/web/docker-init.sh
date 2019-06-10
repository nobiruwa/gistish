#!/usr/bin/env bash

# build a new image as docker-sid-jdk8
docker build -t shareddatabase-web --build-arg CONFIGURATION_SRC=. .
# list images
docker images
