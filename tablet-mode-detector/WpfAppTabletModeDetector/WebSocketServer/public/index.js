window.addEventListener("load", function () {
  var ws = new WebSocket("ws://" + window.location.hostname + ":3001/chat");

  ws.onmessage = function(e) {
    const fileReader = new FileReader();

    fileReader.onload = (e) => {
      const json = JSON.parse(fileReader.result);
      const tabletModeText = document.getElementById("tablet-mode");
      tabletModeText.innerHTML = json.message;
    };
    if (e.data) {
      if (e.data instanceof Blob) {
        fileReader.readAsText(e.data);
      } else {
        const json = JSON.parse(e.data);
        const tabletModeText = document.getElementById("tablet-mode");
        tabletModeText.innerHTML = json.message;
      }
    }
  };
  ws.onopen = function(e) {
    ws.send(JSON.stringify({ message: "current-mode" }));
  };
});
