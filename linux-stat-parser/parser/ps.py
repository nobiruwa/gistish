import csv
import re

from io import StringIO

re_line = re.compile(r'([^ ]+) +([^ ]+) +([^ ]+) +([^ ]+) +([^ ]+) +([^ ]+) +([^ ]+) +([^ ]+) +([^ ]+) +([^ ]+) +(.+)')

def proc_parse(lines):

    parsed_lines = []

    for line in lines.splitlines()[1:]:
        (user, pid, pcpu, pmem, vsz, rss, tt, stat, started, time, command) = re_line.match(line).groups()

        parsed_lines.append({
            'USER': user,
            'PID': pid,
            '%CPU': pcpu,
            '%MEM': pmem,
            'VSZ': vsz,
            'RSS': rss,
            'TT': tt,
            'STAT': stat,
            'STARTED': started,
            'TIME': time,
            'COMMAND': command,
        })

    return parsed_lines

def proc_stat(parsed_lines):
    stat = {}

    for line in parsed_lines:
        pid = line['PID']

        stat[pid] = line

    return stat
