#!/usr/bin/env bash

IP=$(ip addr show eth0 | perl -n -e 'if (m/inet ([\d\.]+)/g) { print $1 }')

resinctl -server app-0 start -Djgroups.bind_addr=${IP}
touch /var/log/resin/jvm-app-0.log
tail -F /var/log/resin/jvm-app-0.log
