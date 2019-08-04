import cors from 'cors';
import express from 'express';
import bodyParser from 'body-parser';
import path from 'path';

import configuration from './configuration';
import infrastructure from './infrastructure';
import svg from './svg';
import pdf from './pdf';

const config = configuration.prepare();
infrastructure.prepare(config);

const app = express();

app.use(cors());

app.use(bodyParser.urlencoded({
  extended: true,
}));

app.use(bodyParser.json());

app.post('/report', async (req, res) => {
  // 1. SVGを作成する
  const svgResult = svg.create(infrastructure, config, req.body);
  // 2. PDFを作成する
  const pdfResult = await pdf.create(infrastructure, config, svgResult.key);
  // 3. SVGとPDFへのアクセスURLをレスポンスに返す

  res.send({
    svg: svgResult,
    pdf: pdfResult,
  });
});

app.get('/report/:key.:format', (req, res) => {
  const { outputDirectory } = config;
  const filePath = path.join(
    outputDirectory,
    req.params.format,
    req.params.key,
    `report.${req.params.format}`,
  );

  infrastructure.throwFileNotFound(filePath);

  res.sendFile(path.resolve(filePath));
});

app.listen(3000);
