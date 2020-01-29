#!/usr/bin/env bash

INFILE=$1
OUTFILE_TYPE=$2
OUTFILE=$3

pandoc -f markdown+simple_tables --highlight-style pygments -s $INFILE -t $OUTFILE_TYPE -o $OUTFILE
