#!/usr/bin/env bash

su postgres -c "pg_ctl -D /var/lib/pgsql/data initdb"

echo "listen_addresses = '*'" >> /var/lib/pgsql/data/postgresql.conf
sed -i -e "s/log_filename = 'postgresql-%a.log'/log_filename = 'postgresql.log'/g" /var/lib/pgsql/data/postgresql.conf
sed -i -e "s/ident$/trust/g" /var/lib/pgsql/data/pg_hba.conf
sed -i -e "s/127\.0\.0\.1\/32/0\.0\.0\.0\/0/g" /var/lib/pgsql/data/pg_hba.conf


echo "postgres:postgres" | chpasswd

su postgres -c "pg_ctl -D /var/lib/pgsql/data start"
sleep 5

su postgres -c "psql -c \"ALTER USER postgres WITH PASSWORD 'postgres'\""
su postgres -c "psql -c \"CREATE ROLE imart WITH LOGIN PASSWORD 'imart'\""
su postgres -c "psql -c \"CREATE DATABASE imart OWNER=imart encoding 'utf8' TEMPLATE template0\""
su postgres -c "psql -c \"CREATE DATABASE iap_db OWNER=imart encoding 'utf8' TEMPLATE template0\""
su postgres -c "psql -c \"CREATE DATABASE \\\"default\\\" OWNER=imart encoding 'utf8' TEMPLATE template0\""
su postgres -c "psql -d imart -c \"CREATE SCHEMA acceldocuments AUTHORIZATION imart\""
su postgres -c "psql -d iap_db -c \"CREATE SCHEMA acceldocuments AUTHORIZATION imart\""
su postgres -c "psql -d default -c \"CREATE SCHEMA acceldocuments AUTHORIZATION imart\""

su postgres -c "pg_ctl -D /var/lib/pgsql/data stop"

rm -rf /var/lib/pgsql/data/log/*.log
