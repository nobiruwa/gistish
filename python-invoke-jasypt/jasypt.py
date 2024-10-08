#!/usr/bin/env python
# -*- coding:utf-8 -*-

import os
import platform
import re
import subprocess
import sys

SCRIPT_PATH = os.path.dirname(os.path.realpath(__file__))
JASYPT_HOME = os.path.join(SCRIPT_PATH, 'jasypt-1.9.3')

if platform.system() == 'Windows':
    JASYPT_EXECUTABLE_ENCRYPT = os.path.join(JASYPT_HOME, 'bin', 'encrypt.bat')
    JASYPT_EXECUTABLE_DECRYPT = os.path.join(JASYPT_HOME, 'bin', 'decrypt.bat')
else:
    JASYPT_EXECUTABLE_ENCRYPT = os.path.join(JASYPT_HOME, 'bin', 'encrypt.sh')
    JASYPT_EXECUTABLE_DECRYPT = os.path.join(JASYPT_HOME, 'bin', 'decrypt.sh')

assert os.path.exists(JASYPT_EXECUTABLE_DECRYPT), 'jasypt-1.9.3を配置してください'
assert os.path.exists(JASYPT_EXECUTABLE_DECRYPT), 'jasypt-1.9.3を配置してください'

def encrypt(password, input_):
    result = subprocess.run(
        [
            JASYPT_EXECUTABLE_ENCRYPT,
            f'password={password}',
            f'input={input_}',
        ],
        capture_output=True,
    )

    if result.returncode != 0:
        stderr = result.stderr.decode('utf-8')
        print(stderr, file=sys.stderr)
        raise Exception("jasypt did not output the result.")

    stdout = result.stdout.decode('utf-8')
    stdout_lines = stdout.splitlines()

    output_header_line_index = None
    OUTPUT_HEADER_LINE_INCLUDES = '----OUTPUT---'
    for (i, line) in enumerate(stdout_lines):
        if OUTPUT_HEADER_LINE_INCLUDES in line:
            output_header_line_index = i

    if output_header_line_index is None:
        print(stdout, file=sys.stderr)
        raise Exception("jasypt did not output the result.")

    output = stdout_lines[output_header_line_index + 2]

    return output

RE_ENC=re.compile(r'ENC\((.+)\)')

def decrypt_enc(password, input_):
    groups = RE_ENC.search(input_).groups()

    if groups is None:
        return decrypt(password, input_)
    else:
        input_disclosed = groups[0]
        return decrypt(password, input_disclosed)

def decrypt(password, input_):
    result = subprocess.run(
        [
            JASYPT_EXECUTABLE_DECRYPT,
            f'password={password}',
            f'input={input_}',
        ],
        capture_output=True,
    )

    if result.returncode != 0:
        stderr = result.stderr.decode('utf-8')
        print(stderr, file=sys.stderr)
        raise Exception("jasypt did not output the result.")

    stdout = result.stdout.decode('utf-8')
    stdout_lines = stdout.splitlines()

    output_header_line_index = None
    OUTPUT_HEADER_LINE_INCLUDES = '----OUTPUT---'
    for (i, line) in enumerate(stdout_lines):
        if OUTPUT_HEADER_LINE_INCLUDES in line:
            output_header_line_index = i

    if output_header_line_index is None:
        print(stdout, file=sys.stderr)
        raise Exception("jasypt did not output the result.")

    output = stdout_lines[output_header_line_index + 2]

    return output
