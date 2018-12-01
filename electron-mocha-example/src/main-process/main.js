import electron from 'electron';

const app = electron.app;
const BrowserWindow = electron.BrowserWindow;

let win;
app.on('ready', onReady);

function onClose() {
  win = null;
}

function onReady() {
  win = new BrowserWindow({
    width: 800,
    height: 600
  });

  win.on('closed', onClose);
  win.loadURL(`file://${__dirname}/../renderer-process/index.html`);
}

export default {
  onClose: onClose,
  onReady: onReady
};
