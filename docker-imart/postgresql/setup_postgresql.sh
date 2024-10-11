#!/usr/bin/env bash

echo "listen_addresses = '*'" >> /etc//postgresql/15/main/postgresql.conf
sed -i -e "s/ident$/trust/g" /etc/postgresql/15/main/pg_hba.conf
sed -i -e "s/127\.0\.0\.1\/32/0\.0\.0\.0\/0/g" /etc/postgresql/15/main/pg_hba.conf

echo "postgres:postgres" | chpasswd

service postgresql start
sleep 5

su postgres -c "psql -c \"ALTER USER postgres WITH PASSWORD 'postgres'\""
su postgres -c "psql -c \"CREATE ROLE imart WITH LOGIN PASSWORD 'imart'\""
su postgres -c "psql -c \"CREATE DATABASE imart OWNER=imart encoding 'utf8' TEMPLATE template0\""
su postgres -c "psql -c \"CREATE DATABASE iap_db OWNER=imart encoding 'utf8' TEMPLATE template0\""
su postgres -c "psql -c \"CREATE DATABASE \\\"default\\\" OWNER=imart encoding 'utf8' TEMPLATE template0\""
su postgres -c "psql -d imart -c \"CREATE SCHEMA acceldocuments AUTHORIZATION imart\""
su postgres -c "psql -d iap_db -c \"CREATE SCHEMA acceldocuments AUTHORIZATION imart\""
su postgres -c "psql -d default -c \"CREATE SCHEMA acceldocuments AUTHORIZATION imart\""

service postgresql stop

rm -rf /var/log/postgresql/*.log
