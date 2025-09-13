#!/usr/bin/env bash

docker run -it --name intra-mart-postgresql-rocky --rm -p 5432:5432 -v intra-mart-postgresql-rocky-volume-data:/var/lib/pgsql/data intra-mart-postgresql-rocky
