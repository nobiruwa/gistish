#!/usr/bin/env bash

echo $0 $1 $2 $3

DESTINATION_ROOT_DIR=$1
PROCS_PATH=$2
SYSTEMD_SERVICE=$3
MAIN_PID=$(systemctl show --property MainPID "${SYSTEMD_SERVICE}" | sed -e "s/MainPID=//")

if [ "${MAIN_PID}" = 0 ];then
    echo Good bye.
    exit 1
fi

SUB_PID=1

for PID in `cat /sys/fs/cgroup/system.slice/${SYSTEMD_SERVICE}/cgroup.procs`; do
    echo "${PID}"
    if [ "${PID}" == "${MAIN_PID}" ]; then
        echo -e "${SYSTEMD_SERVICE}\t${PID}" >> "${PROCS_PATH}"
    else
        echo -e "${SYSTEMD_SERVICE}_${SUB_PID}\t${PID}" >> "${PROCS_PATH}"
        SUB_PID=$((${SUB_PID} + 1))
    fi

    DESTINATION_DIR="${DESTINATION_ROOT_DIR}/proc/${PID}"

    mkdir -p "${DESTINATION_DIR}"

    cp "/proc/${PID}/cmdline" "${DESTINATION_DIR}"
    cp "/proc/${PID}/status" "${DESTINATION_DIR}"

    PROC_NUMBER_OF_FD=$(find "/proc/${PID}/fd/" -type l | wc -l)
    echo "${PROC_NUMBER_OF_FD}" > "${DESTINATION_DIR}/fds.txt"
done





