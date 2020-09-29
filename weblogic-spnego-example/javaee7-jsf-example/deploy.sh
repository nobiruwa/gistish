#!/usr/bin/env bash

./gradlew build && cd build/libs && smbclient //192.168.1.11/public -U Administrator -c 'mput javaee7-jsf-example.war' ; cd ../../ 
