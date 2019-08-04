import express from 'express';
import bodyParser from 'body-parser';

import configuration from './configuration';
import infrastructure from './infrastructure';
import svg from './svg';

const config = configuration.prepare();
infrastructure.prepare(config);

const app = express();

app.use(bodyParser.urlencoded({
  extended: true,
}));
app.use(bodyParser.json());

app.post('/', (req, res) => {
  // 1. SVGを作成する
  const svgResult = svg.createSVG(infrastructure, config, req.body);
  // 2. PDFを作成する
  // 3. SVGとPDFへのアクセスURLをレスポンスに返す

  res.send({
    svg: svgResult,
  });
});

app.listen(3000);
