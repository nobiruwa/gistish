/* global require:false */
const cors = require('cors');
const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');

const configuration = require('./configuration');
const infrastructure = require('./infrastructure');
const svg = require('./svg');
const pdf = require('./pdf');
const html = require('./html');

const config = configuration.prepare();
infrastructure.prepare(config);

const app = express();

app.use(cors());

app.use(bodyParser.urlencoded({
  extended: true,
}));

app.use(bodyParser.json());

// /report/*.html にとっての../style/*.cssを解決するために用いる
// file:///での参照の場合には、stylesのシンボリックリンクをoutput/html/stylesに張ること
app.use('/style', express.static('style'));

app.post('/report', async (req, res, next) => {
  try {
    // 1. HTMLを作成する
    const htmlResult = html.create(infrastructure, config, req.body);
    // 2. PDFを作成する
    const pdfResult = await pdf.createFromHtml(infrastructure, config, htmlResult.key);
    // 3. HTMLとPDFへのアクセスURLをレスポンスに返す

    res.send({
      html: htmlResult,
      pdf: pdfResult,
    });
  } catch (e) {
    next(e);
  }
});

app.post('/svg/report', async (req, res, next) => {
  try {
    // 1. SVGを作成する
    const svgResult = svg.create(infrastructure, config, req.body);
    // 2. PDFを作成する
    const pdfResult = await pdf.createFromSvg(infrastructure, config, svgResult.key);
    // 3. SVGとPDFへのアクセスURLをレスポンスに返す

    res.send({
      svg: svgResult,
      pdf: pdfResult,
    });
  } catch (e) {
    next(e);
  }
});

app.get('/report/:key.:format', (req, res, next) => {
  try {
    const { outputDirectory } = config;
    const filePath = path.join(
      outputDirectory,
      req.params.format,
      req.params.key,
      `report.${req.params.format}`,
    );

    infrastructure.throwFileNotFound(filePath);

    res.sendFile(path.resolve(filePath));
  } catch (e) {
    next(e);
  }
});

app.listen(config.port);
