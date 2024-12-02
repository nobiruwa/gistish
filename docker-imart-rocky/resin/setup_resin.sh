#!/usr/bin/env bash

# curl -L -C - -O http://caucho.com/download/resin-pro-4.0.66.zip
unzip resin-pro-4.0.66.zip
rm resin-pro-4.0.66.zip

cd resin-pro-4.0.66
./configure
make
make install

cd ..
# rm -rf resin-pro-4.0.66

echo "app.https         : 8443" >> /etc/resin/resin.properties
echo "elastic_cloud_enable : false" >> /etc/resin/resin.properties
echo "jvm_args  : -Dfile.encoding=UTF-8 -Djava.io.tmpdir=tmp -Xmx4096m -Xms4096m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:InitiatingHeapOccupancyPercent=30 -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -Xdebug -Xrunjdwp:transport=dt_socket,address=9009,server=y,suspend=n --illegal-access=warn" >> /etc/resin/resin.properties
echo "admin_user : admin" >> /etc/resin/resin.properties
echo "admin_password : {SSHA}bTLnpGjSarNFd/EQe8W4d/IjAxfQdGe8" >> /etc/resin/resin.properties
echo "admin_external : true" >> /etc/resin/resin.properties
echo "admin_secure : true" >> /etc/resin/resin.properties
echo "web_admin_external : true" >> /etc/resin/resin.properties

mkdir /var/resin/storage
mkdir /var/resin/tmp
