#!/usr/bin/env bash

TIME=$(date '+%Y%m%d_%H%M%S')

DESTINATION_DIR="/tmp/stat/${TIME}"

mkdir -p "${DESTINATION_DIR}"
cp "/proc/stat" "${DESTINATION_DIR}"

ss -antlp > "${DESTINATION_DIR}/ports.txt"

XDG_CONFIG_HOME_ORIG="${XDG_CONFIG_HOME}"
XDG_CONFIG_HOME="/tmp/stat/config"
mkdir -p "${XDG_CONFIG_HOME}/procps"
cp ./_toprc "${XDG_CONFIG_HOME}/procps/toprc"

COLUMNS=128 XDG_CONFIG_HOME="/tmp/stat/config" top -b -n 1 > "${DESTINATION_DIR}/top.txt"
COLUMNS=128 ps uax > "${DESTINATION_DIR}/ps.txt"

XDG_CONFIG_HOME="${XDG_CONFIG_HOME_ORIG}"

./_get-metrics-per-process.sh "${TIME}"
