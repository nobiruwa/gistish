#!/usr/bin/env bash

docker run -it --name intra-mart-resin-rocky --rm -p 6800:6800 -p 8080:8080 -p 8443:8443 -p 9009:9009 -p 5200:5200 -p 5701:5701 -p 40608:40608 intra-mart-resin-rocky
