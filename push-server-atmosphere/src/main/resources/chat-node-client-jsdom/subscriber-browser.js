function createRequest(arg) {
  var request = {
    logLevel: 'debug',
    url: arg.hostURL,
    contentType : "application/json",
    transport : arg.transport,
    trackMessageLength: true,
    reconnectInterval : 5000
  };

  request.isopen = false;

  request.onOpen = function(response) {
    request.isopen = true;
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
  const args = {
    "hostURL": "http://localhost:8080/push-server-atmosphere/chat",
    "transport": "jsonp",
    "author": "ichiro",
    "message": "heeleoe",
    "interval": 1000
  };

  const request = createRequest(args);
  const socket = createAtmosphere(request);
    setInterval(() => {
        if (request.isopen) {
            console.log(request.isopen);
        }
  }, 1000);
}

main();
