/*global __coverage__:false, __dirname:false, global:false*/
import { resolve, join } from 'path';
import process from 'process';
import { existsSync as exists, mkdirSync as mkdir, lstatSync as lstat, writeFileSync as write } from 'fs';

const root = resolve(__dirname, '..', '..', '..');
const tmpd = resolve(root, '.nyc_output');

function report() {
  if (global.__coverage__) {
    if (!exists(tmpd) || !lstat(tmpd).isDirectory()) {
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
