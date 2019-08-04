import puppeteer from 'puppeteer';
import path from 'path';

async function createPDF(url, outputFile, paperW, paperH, orientation) {
  // サンドボックスを無効化する前に、以下を試すこと
  // https://github.com/GoogleChrome/puppeteer/blob/master/docs/troubleshooting.md
  // sudo sysctl -w kernel.unprivileged_userns_clone=1
  // const browser = await puppeteer.launch({args: ['--no-sandbox', '--disable-setuid-sandbox']});

  const browser = await puppeteer.launch();
  const page = await browser.newPage();

  await page.goto(url, {
    waitUntil:['networkidle0'], timeout:10000,
  });

  await page.setViewport({
    width: Number(paperW),
    height: Number(paperH),
  });

  await page.pdf({
    path: outputFile,
    width: paperW,
    height: paperH,
    landscape: orientation=='portrait' ? false : true,
  });

  await browser.close();

  return {
  };
}

async function create(infra, serverConfig, key) {
  const svgLocalPath = path.resolve(path.join(serverConfig.outputDirectory, 'svg', key, 'report.svg')).replace('\\', '/');
  const svgUrl = `file://${svgLocalPath}`;
  const pdfLocalDir = path.join(serverConfig.outputDirectory, 'pdf', key);

  infra.createDirectoryIfNotExists(pdfLocalDir);

  const pdfLocalPath = path.join(pdfLocalDir, 'report.pdf');

  // TODO SVGのheightとwidth、クライアントかサーバーのDPIに合わせて引数を動的に決定
  // 以下はとりあえず72 DPIで固定している
  await createPDF(svgUrl, pdfLocalPath, 595, 842, 'portrait');

  return {
    key,
    filePath: pdfLocalPath,
  };
}

export default {
  create,
  createPDF,
};
