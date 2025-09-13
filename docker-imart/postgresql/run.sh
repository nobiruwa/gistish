#!/usr/bin/env bash

service postgresql restart

sleep 5

tail -f '/var/log/postgresql/postgresql-17-main.log'

