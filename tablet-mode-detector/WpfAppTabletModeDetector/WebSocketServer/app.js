const httpServer = require("http-server").createServer({
    autoIndex: true,
    cors: true
});

httpServer.listen(3000, () => {
    console.log((new Date()) + "HTTP Server is litening on port 3000");
});

const WebSocketServer = require("ws").Server;
const wsServer = new WebSocketServer({
    host: "localhost",
    port: 3001,
    path: "/chat",
    clientTracking: true
});

wsServer.on("connection", ((ws) => {
    ws.on("message", (message) => {
        console.log((new Date()) + " Message Received.");
        wsServer.clients.forEach((client) => {
            client.send(message);
        });
    });
}));

wsServer.on("error", (error) => {
    console.log(error);
})