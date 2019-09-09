/* global module:false, require:false */
const puppeteer = require('puppeteer');
const os = require('os');
const path = require('path');

function mmToPixel(mm, ppi, scale) {
  // 1 inch = 25.4 mm
  // [mm] * [pixel / inch] * [1] / [mm] => [pixel / inch]
  return Math.ceil(mm * ppi * scale / 25.4);
}

function getPPI() {
  // According to <https://web.dev/codelab-density-descriptors>,
  // Chrome use PPI = 96.
  return 96;
}

async function createPDF(url, outputFile, paperW, paperH, orientation) {
  // サンドボックスを無効化する前に、以下を試すこと
  // https://github.com/GoogleChrome/puppeteer/blob/master/docs/troubleshooting.md
  // sudo sysctl -w kernel.unprivileged_userns_clone=1
  // const browser = await puppeteer.launch({args: ['--no-sandbox', '--disable-setuid-sandbox']});

  const browser = await puppeteer.launch();
  const page = await browser.newPage();

  await page.goto(url, {
    waitUntil: ['networkidle0'],
    timeout: 10000,
  });

  await page.setViewport({
    width: Math.ceil(paperW),
    height: Math.ceil(paperH),
  });

  await page.pdf({
    path: outputFile,
    // width: Math.ceil(paperW),
    // height: Math.ceil(paperH),
    format: 'A4',
    landscape: orientation == 'portrait' ? false : true,
  });

  await browser.close();

  return {
  };
}

async function createFromSvg(infra, serverConfig, key) {
  const svgLocalPath = path.resolve(path.join(serverConfig.outputDirectory, 'svg', key, 'report.svg')).replace('\\', '/');
  const svgUrl = `file://${svgLocalPath}`;
  const pdfLocalDir = path.join(serverConfig.outputDirectory, 'pdf', key);

  infra.createDirectoryIfNotExists(pdfLocalDir);

  const pdfLocalPath = path.join(pdfLocalDir, 'report.pdf');

  const ppi = getPPI();

  // A4 = 210mm x 297mm
  await createPDF(svgUrl, pdfLocalPath, mmToPixel(210, ppi, 1.0), mmToPixel(297, ppi, 1.0), 'portrait');

  return {
    key,
    filePath: pdfLocalPath,
  };
}

async function createFromHtml(infra, serverConfig, key) {
  const htmlLocalPath = path.resolve(path.join(serverConfig.outputDirectory, 'html', key, 'report.html')).replace('\\', '/');
  const htmlUrl = `file://${htmlLocalPath}`;
  const pdfLocalDir = path.join(serverConfig.outputDirectory, 'pdf', key);

  infra.createDirectoryIfNotExists(pdfLocalDir);

  const pdfLocalPath = path.join(pdfLocalDir, 'report.pdf');

  const ppi = getPPI();

  // A4 = 210mm x 297mm
  await createPDF(htmlUrl, pdfLocalPath, mmToPixel(210, ppi, 1.0), mmToPixel(297, ppi, 1.0), 'portrait');

  return {
    key,
    filePath: pdfLocalPath,
  };
}

module.exports = {
  createPDF,
  createFromSvg,
  createFromHtml,
};
