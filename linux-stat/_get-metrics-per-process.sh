#!/usr/bin/env bash

echo $0 $1

TIME=$1

SYSTEMD_SERVICE=cron
PID=$(systemctl show --property MainPID "${SYSTEMD_SERVICE}" | sed -e "s/MainPID=//")

if [ "${PID}" = 0 ];then
    echo Good bye.
    exit 1
fi

DESTINATION_DIR="/tmp/stat/${TIME}/proc/${PID}"

mkdir -p "${DESTINATION_DIR}"

cp "/proc/${PID}/cmdline" "${DESTINATION_DIR}"
cp "/proc/${PID}/status" "${DESTINATION_DIR}"

PROC_NUMBER_OF_FD=$(find "/proc/${PID}/fd/" -type l | wc -l)
echo "${PROC_NUMBER_OF_FD}" > "${DESTINATION_DIR}/fds.txt"
