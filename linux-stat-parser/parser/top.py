import csv
import re

re_first_line = re.compile(r'top *- *([0-9:]+) *up *([^ ]+), *([^ ]+) *users?, *load average: *(.+)')
re_second_line = re.compile(r'Tasks: *([0-9]+) *total, *([0-9]+) *running, *([0-9]+) *sleeping, *([0-9]+) *stopped, *([0-9]+) *zombie')
re_third_line = re.compile(r'%Cpu\(s\): *([0-9\.]+) *([a-z]+), *([0-9\.]+) *sy, *([0-9\.]+) *ni, *([0-9\.]+) *id, *([0-9\.]+) *wa, *([0-9\.]+) *hi, *([0-9\.]+) *si, *([0-9\.]+) *st *')
re_forth_line = re.compile(r'MiB *Mem *: *([0-9\.]+) *total, *([0-9\.]+) *free, *([0-9\.]+) *used, *([0-9\.]+) *buff/cache *')
re_fifth_line = re.compile(r'MiB *Swap: *([0-9\.]+) *total, *([0-9\.]+) *free, *([0-9\.]+) *used. *([0-9\.]+) *avail *Mem *')

re_spaces = re.compile(r' +')

def parse(lines):
    lines = lines.splitlines()
    (date, uptime, users, load_averages) = re_first_line.match(lines[0]).groups()
    (task_total, task_running, task_sleeping, task_stopped, task_zombie) = re_second_line.match(lines[1]).groups()
    (pcpu_unniced, pcpu_unniced_unit, pcpu_sy, pcpu_ni, pcpu_id, pcpu_wa, pcpu_hi, pcpu_si, pcpu_st) = re_third_line.match(lines[2]).groups()
    (mem_total, mem_free, mem_used, mem_buff_cache) = re_forth_line.match(lines[3]).groups()
    (swap_total, swap_free, swap_used, mem_avail) = re_fifth_line.match(lines[4]).groups()

    return {
        'date': date,
        'uptime': uptime,
        'users': users,
        'load_averages': load_averages,
        'task_total': task_total,
        'task_running': task_running,
        'task_sleeping': task_sleeping,
        'task_stopped': task_stopped,
        'task_zombie': task_zombie,
        'pcpu_unniced': pcpu_unniced,
        'pcpu_unniced_unit': pcpu_unniced_unit,
        'pcpu_sy': pcpu_sy,
        'pcpu_ni': pcpu_ni,
        'pcpu_id': pcpu_id,
        'pcpu_wa': pcpu_wa,
        'pcpu_hi': pcpu_hi,
        'pcpu_si': pcpu_si,
        'pcpu_st': pcpu_st,
        'mem_total': mem_total,
        'mem_free': mem_free,
        'mem_used': mem_used,
        'mem_buff_cache': mem_buff_cache,
        'swap_total': swap_total,
        'swap_free': swap_free,
        'swap_used': swap_used,
        'mem_avail': mem_avail,
    }

def stat(parsed):
    return parsed

def proc_parse(lines):
    normalized_lines = [re_spaces.sub('\t', line.strip()) for line in lines.splitlines()]

    reader = csv.DictReader(
        normalized_lines,
        fieldnames=[
            'PID',
            'USER',
            'PR',
            'NI',
            'VIRT',
            'RES',
            'SHR',
            'S',
            '%CPU',
            '%MEM',
            'TIME+',
            'nTH',
            'SWAP',
        ],
        delimiter='\t',
    )

    next(reader)
    next(reader)
    next(reader)
    next(reader)
    next(reader)
    next(reader)

    result = [line for line in reader]

    return result

def proc_stat(parsed_lines):
    stat = {}

    for line in parsed_lines:
        stat[line['PID']] = line

    return stat
