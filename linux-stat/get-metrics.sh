#!/usr/bin/env bash

SCRIPT_DIR=$(realpath $(dirname $0))
TIME=$(date '+%Y%m%d_%H%M%S')
DESTINATION_DIR="/tmp/stat/${TIME}"
PROCS_PATH="${DESTINATION_DIR}/procs.txt"
TOP_XDG_CONFIG_HOME="/tmp/stat/config"
TOP_COLUMNS=128
PS_COLUMNS=128

mkdir -p "${DESTINATION_DIR}"
cp "/proc/stat" "${DESTINATION_DIR}"

ss -antlp > "${DESTINATION_DIR}/ports.txt"

mkdir -p "${TOP_XDG_CONFIG_HOME}/procps"
cp ./_toprc "${TOP_XDG_CONFIG_HOME}/procps/toprc"


COLUMNS=${TOP_COLUMNS} XDG_CONFIG_HOME="${TOP_XDG_CONFIG_HOME}" top -b -n 1 > "${DESTINATION_DIR}/top.txt"
COLUMNS=${PS_COLUMNS} ps -e -o 'user,pid,pcpu,pmem,vsz,rss,tty,stat,lstart,time,command' --date-format '%Y-%m-%dT%H:%M:%S' > "${DESTINATION_DIR}/ps.txt"

source ${SCRIPT_DIR}/_source.sh
echo "${SERVICES[@]}"

for service in "${SERVICES[@]}"; do
    ${SCRIPT_DIR}/_get-metrics-per-process.sh "${DESTINATION_DIR}" "${PROCS_PATH}" "${service}"
done


