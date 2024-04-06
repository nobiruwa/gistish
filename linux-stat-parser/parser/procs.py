import csv

def proc_parse(lines):
    reader = csv.DictReader(
        lines,
        fieldnames=[
            'service_name',
            'pid',
        ],
        delimiter='\t',
    )

    next(reader)

    return [line for line in reader]

def proc_stat(parsed_lines):
    stat = {}

    for line in parsed_lines:
        stat[line['pid']] = line['service_name']

    return stat
