/* global module:false, require:false */
const process = require('process');

const defaultServerConfiguration = {
  outputDirectory: 'output',
  templateDirectory: 'template',
  styleDirectory: 'style',
  commonStyleName: 'common',
  port: 3000,
  // 固定値
  supportFormats: ['html', 'pdf'],
};

function update(config, envName, propName, parse = (x) => x) {
  if (process.env[envName]) {
    config[propName] = parse(process.env[envName]);
  }

  return config;
}

function updateOutputDirectory(config) {
  update(defaultServerConfiguration, 'OUTPUT_DIRECTORY', 'outputDirectory');
}

function updateTemplateDirectory(config) {
  update(defaultServerConfiguration, 'TEMPLATE_DIRECTORY', 'templateDirectory');
}

function updateStyleDirectory(config) {
  update(defaultServerConfiguration, 'STYLE_DIRECTORY', 'styleDirectory');
}

function updateCommonStyleName(config) {
  update(defaultServerConfiguration, 'COMMON_STYLE_NAME', 'commonStyleName');
}

function updateSupportFormats(config) {
  update(defaultServerConfiguration, 'SUPPORT_FORMATS', 'supportFormats', (x) => x.split(','));
}

function updatePort(config) {
  update(defaultServerConfiguration, 'PORT', 'port');
}

function prepare() {
  const config = defaultServerConfiguration;
  updateOutputDirectory(config);
  updateTemplateDirectory(config);
  updateStyleDirectory(config);
  updateCommonStyleName(config);

  updateSupportFormats(config);

  updatePort(config);

  return config;
}


module.exports = {
  prepare,
};
