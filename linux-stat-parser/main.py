import os.path
import parser.ports
import parser.procs
import parser.ps
import parser.top
import sys

def main():
    destination_dir = sys.argv[1]
    ports_path = os.path.join(destination_dir, 'ports.txt')
    procs_path = os.path.join(destination_dir, 'procs.txt')
    ps_path = os.path.join(destination_dir, 'ps.txt')
    top_path = os.path.join(destination_dir, 'top.txt')

    with open(ports_path) as fp:
        ports = parser.ports.proc_stat(parser.ports.proc_parse(fp.read()))

    print(ports)

    with open(procs_path) as fp:
        procs = parser.procs.proc_stat(parser.procs.proc_parse(fp.read()))

    print(procs)

    with open(ps_path) as fp:
       ps = parser.ps.proc_stat(parser.ps.proc_parse(fp.read()))

    print(ps)

    with open(top_path) as fp:
        top = parser.top.proc_stat(parser.top.proc_parse(fp.read()))

    print(top)

    with open(top_path) as fp:
        top_stat = parser.top.stat(parser.top.parse(fp.read()))

    print(top_stat)

if __name__ == '__main__':
    main()
