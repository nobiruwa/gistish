#!/usr/bin/env bash

su postgres -c "pg_ctl -D /var/lib/pgsql/data start"

sleep 5

tail -f '/var/lib/pgsql/data/log/postgresql.log'

