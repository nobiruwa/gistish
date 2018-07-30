window.addEventListener("load", function () {
    var ws = new WebSocket("ws://" + window.location.hostname + ":3001/chat");

    ws.onmessage = function(e) {
        if (e.data) {
            var json = JSON.parse(e.data);
            var tabletModeText = document.getElementById("tablet-mode");
            tabletModeText.innerHTML = json.message;
        }
    };
    ws.onopen = function(e) {
        ws.send(JSON.stringify({ message: "current-mode" }));
    };
});