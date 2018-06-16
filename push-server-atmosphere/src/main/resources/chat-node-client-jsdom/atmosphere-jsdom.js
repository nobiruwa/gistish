const fs = require("fs");
const jsdom = require('jsdom');
const virtualConsole = new jsdom.VirtualConsole();
const { window } = new jsdom.JSDOM(``, { virtualConsole, runScripts: "dangerously", resources: "usable" });

function appendScript(window, path) {
  const lib = fs.readFileSync(path, { encoding: "utf-8" });

  const script = window.document.createElement("script");
  script.textContent = lib;
  window.document.head.appendChild(script);
}

virtualConsole.sendTo(console);

appendScript(window, "node_modules/atmosphere.js/lib/atmosphere.js");
appendScript(window, "subscriber-browser.js");
