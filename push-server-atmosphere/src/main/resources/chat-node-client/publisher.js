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

const process = require('process');
// npm install atmosphere.js
const atmosphere = require('atmosphere.js');

const USAGE = 'node chat-client.js ATMOSHPERE_SERVICE_URL AUTH_USER AUTH_PASSWORD TRANSPORT_NAME YOUR_NAME INTERVAL MESSAGE';
const TRANSPORT_NAMES = ["websocket", "sse", "long-polling", "streaming", "ajax", "jsonp"];

var hostURL = 'http://localhost:8080/push-server-atmosphere/chat';

function parseArgument() {
  if (process.argv.length !== 5) {
    throw USAGE;
  }

  return {
    hostURL: process.argv[2],
    headers: [
      'Authorization: Basic ' + Buffer.from(process.argv[3] + ':' + process.argv[4]).toString('base64')
    ],
    withCredentials: true,
    transport : process.argv[5],
    author: process.argv[6],
    interval: parseInt(process.argv[7]),
    message: process.argv[8]
  };
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
    } catch (e) {
      console.log('Invalid response: ', message);
      return;
    }
    if (json.author == json.message) {
      console.log(json.author + " joined the room");
    } else {
      console.log(json.author + " says '" + json.message + "'");
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

  setTimeout(() => socket.push(atmosphere.util.stringifyJSON({ author: args.author, message: args.message })),
             args.interval);
}

main();
