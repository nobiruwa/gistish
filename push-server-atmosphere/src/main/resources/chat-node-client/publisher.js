/*global require:false, Buffer */
/**
 * chat-client.js
 * 
 * A client program to chat over atmosphare-samples/samples/websocket-chat or
 * atmosphare-samples/samples/chat.
 * 
 * SYNOPSIS
 *    node chat-client.js ATMOSPHERE_SERVICE_URL
 */

"use strict";

const fs = require('fs');
const process = require('process');
// npm install atmosphere.js
const atmosphere = require('atmosphere.js');

const USAGE = 'node chat-client.js --infile CONFIG_JSON_PATH';
const TRANSPORT_NAMES = ["websocket", "sse", "long-polling", "streaming", "ajax", "jsonp"];

var hostURL = 'http://localhost:8080/push-server-atmosphere/chat';

function parseArgument() {
  if (process.argv.length !== 4) {
    throw USAGE;
  }

  return JSON.parse(fs.readFileSync(process.argv[3]));
}

function createRequest(arg) {
  var request = {
    url: arg.hostURL,
    contentType : "application/json",
    transport : arg.transport,
    trackMessageLength: true,
    reconnectInterval : 5000
  };

  request.isopen = false;

  request.onOpen = function(response) {
    this.isopen = true;
    console.log('Connected using ' + response.transport);
  };

  request.onMessage = function (response) {
    var message = response.responseBody;
    try {
      var json = JSON.parse(message);
      console.log(message);
    } catch (e) {
      console.log('Invalid response: ', message);
      return;
    }
    if (json.author == json.message) {
      console.log(json.author + " joined the room");
    } else {
      console.log(json.author + " says '" + json.text + "'");
    }
  };

  request.onReconnect = function(response) {
    console.log('Reconnecting ...');
  };

  request.onReopen = function(response) {
    this.isopen = true;
    console.log('Reconnected using ' + response.transport);
    prompt.setPrompt("> ", 2);
    prompt.prompt();
  };

  request.onClose = function(response) {
    this.isopen = false;
  };

  request.onError = function(response) {
    console.log("Sorry, something went wrong: " + response.responseBody);
  };

  return request;
}

function createAtmosphere(request) {
  return atmosphere.subscribe(request);
}

function main() {
  const args = parseArgument();
  const request = createRequest(args);
  const socket = createAtmosphere(request);
  console.log(args);
  setTimeout(() => socket.push(atmosphere.util.stringifyJSON({ author: args.author, message: args.message })),
             args.interval);
}

main();
