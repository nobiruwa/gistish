#!/usr/bin/env bash

tar zxvf /openjdk-11.0.2_linux-x64_bin.tar.gz
mkdir -p /usr/local/java
mv jdk-11.0.2 /usr/local/java/jdk-11.0.2
ln -s /usr/local/java/jdk-11.0.2 /usr/local/java/jdk
