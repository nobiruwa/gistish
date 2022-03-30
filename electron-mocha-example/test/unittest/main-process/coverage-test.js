/*global __coverage__:false, __dirname:false, global:false*/
import { resolve, join } from 'path';
import process from 'process';
import { mkdirSync as mkdir, statSync as stat, writeFileSync as write } from 'fs';

const root = resolve(__dirname, '..', '..', '..');
const tmpd = resolve(root, '.nyc_output');

function report() {
  if (global.__coverage__) {
    if (!stat(tmpd).isDirectory()) {
      mkdir(tmpd);
    }
    write(join(tmpd, `${process.type}.json`), JSON.stringify(global.__coverage__));
  }
}

if (process.type === 'browser') {
  process.on('exit', report);
} else {
  window.addEventListener('unload', report);
}
