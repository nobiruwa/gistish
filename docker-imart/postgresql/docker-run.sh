#!/usr/bin/env bash

docker run -it --rm -p 5432:5432 -v intra-mart-postgresql-volume-data:/var/lib/postgresql/17/main intra-mart-postgresql $@
