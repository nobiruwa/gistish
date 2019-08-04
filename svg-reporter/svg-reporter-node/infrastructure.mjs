import assert from 'assert';
import fs from 'fs';
import path from 'path';

function createDirectoryIfNotExists(path) {
  if (!fs.existsSync(path)) {
    fs.mkdirSync(path);
  }
}

function throwIfDirectoryAlreadyExists(path) {
  if (fs.existsSync(path)) {
    throw new Error(`A directory already exists, ${path}`);
  }
}

function throwFileNotFound(path) {
  if (!fs.existsSync(path)) {
    throw new Error(`File not found, ${path}`);
  }

  const stat = fs.lstatSync(path);
  if (stat.isDirectory()) {
    throw new Error(`Tried to get a file, but got a directory.`);
  }

}

function exists(path) {
  return fs.existsSync(path);
}


function prepare(serverConfiguration) {
  const {
    outputDirectory,
    templateDirectory,
    styleDirectory,
    supportFormats,
  } = serverConfiguration;
  createDirectoryIfNotExists(outputDirectory);
  assert(fs.existsSync(outputDirectory));

  createDirectoryIfNotExists(templateDirectory);
  assert(fs.existsSync(templateDirectory));

  createDirectoryIfNotExists(styleDirectory);
  assert(fs.existsSync(styleDirectory));

  supportFormats.forEach(format => {
    const formatDirectory = path.join(outputDirectory, format);
    createDirectoryIfNotExists(formatDirectory);

    assert(fs.existsSync(formatDirectory));
  });
}

export default {
  createDirectoryIfNotExists,
  prepare,
  throwIfDirectoryAlreadyExists,
  throwFileNotFound,
  exists,
};
