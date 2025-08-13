/* global require:false, global:false, module: false */
/* global describe:false, beforeEach:false, afterEach:false, it:false */
const Application = require('spectron').Application;
const chai = require('chai');
const chaiAsPromised = require('chai-as-promised');
const electron = require('electron');

global.before(() => {
  chai.should();
  chai.use(chaiAsPromised);
});

const config = {
  url: {
    // you can find iframe in https://jqueryui.com/draggable/.
    draggable: 'index.html',
  },
};

const hooks = {
  async startApp() {
    const app = await new Application({
      path: electron,
      args: ['.']
    }).start();
    chaiAsPromised.transferPromiseness = app.transferPromiseness;
    return app;
  },

  async stopApp(app) {
    if (app && app.isRunning()) {
      await app.stop();
    }
  }
};

async function resize(app, selector, dx, dy) {
  const initialMouseX = 1;
  const initialMouseY = 1;
  const handle = `${selector} .ui-resizable-se`;

  const beforeLoc = await app.client.element(selector)
        .then((obj) => app.client.elementIdSize(obj.value.ELEMENT));

  await app.client.moveToObject(
    handle,
    initialMouseX,
    initialMouseY,
  );

  await app.client.element(handle).buttonDown();

  await app.client.moveToObject(
    handle,
    initialMouseX + dx,
    initialMouseX + dy,
  );

  await app.client.element(selector).buttonUp();

  const afterLoc = await app.client.element(selector)
        .then((obj) => app.client.elementIdSize(obj.value.ELEMENT));

  // 引数のdx, dyと実際の移動量の確認
  console.log({
    dx,
    dy,
  });

  console.log({
    dx: afterLoc.value.width - beforeLoc.value.width,
    dy: afterLoc.value.height - beforeLoc.value.height,
  });
}

describe('Sample Test', () => {
  let app;

  beforeEach(async () => {
    app = await hooks.startApp();
  });

  afterEach(async() => {
    await hooks.stopApp(app);
  });

  it('should be draggable', async() => {

    await app.client.waitUntilWindowLoaded();

    await resize(app, '#resizable', 100, 200);
  });

});
