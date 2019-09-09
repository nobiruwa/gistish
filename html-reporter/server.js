/* global require:false */
const cors = require('cors');
const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');

const configuration = require('./configuration');
const infrastructure = require('./infrastructure');
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
