#!/usr/bin/env bash

echo $0 $1 $2 $3

DESTINATION_ROOT_DIR=$1
PROCS_PATH=$2
SYSTEMD_SERVICE=$3
PID=$(systemctl show --property MainPID "${SYSTEMD_SERVICE}" | sed -e "s/MainPID=//")

if [ "${PID}" = 0 ];then
    echo Good bye.
    exit 1
fi

echo "${SYSTEMD_SERVICE}\t${PID}" >> "${PROCS_PATH}"

DESTINATION_DIR="${DESTINATION_ROOT_DIR}/proc/${PID}"

mkdir -p "${DESTINATION_DIR}"

cp "/proc/${PID}/cmdline" "${DESTINATION_DIR}"
cp "/proc/${PID}/status" "${DESTINATION_DIR}"

PROC_NUMBER_OF_FD=$(find "/proc/${PID}/fd/" -type l | wc -l)
echo "${PROC_NUMBER_OF_FD}" > "${DESTINATION_DIR}/fds.txt"
