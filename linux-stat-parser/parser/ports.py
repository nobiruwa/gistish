import csv
import re

from io import StringIO

re_spaces = re.compile(r' +')
re_end_spaces = re.compile(r' +$')
re_process = re.compile(r'(.+):\(\("(.+)",pid=(.+),fd=(.+)\)\)')

def proc_parse(lines):
    normalized_lines = StringIO(re_end_spaces.sub('', re_spaces.sub('\t', lines)))

    reader = csv.DictReader(
        normalized_lines,
        fieldnames=[
            'State',
            'Recv-Q',
            'Send-Q',
            'Local Address:Port',
            'Peer Address:Port',
            'Process',
        ],
        delimiter='\t',
    )

    next(reader)

    return [line for line in reader]

def proc_stat(parsed_lines):
    stat = {}

    for line in parsed_lines:
        process = line['Process']

        (user, exe, pid, fd) = re_process.match(process).groups()

        stat[pid] = stat[pid] + 1 if stat.get(pid) else 1

    return stat
