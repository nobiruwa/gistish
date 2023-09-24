/*global require */
const cookieParser = require('cookie-parser');
const crypto = require('crypto');
const express = require('express');
const fs = require('fs');

const app = express();

app.use(express.json());
app.use(cookieParser());

const port = 3000;

const sessionMap = {};

app.get('/', (req, res) => {
  const content = fs.readFileSync('index.html');
  res.type('text/html');
  res.send(content);
});

app.post('/authenticate', (req, res) => {
  const username = req.body.username;

  if (username) {
    const sessionId = crypto.randomUUID();
    sessionMap[sessionId] = username;

    res.cookie('SESSION', sessionId);
    res.cookie('HTTP_ONLY', 'meh', { httpOnly: true });

    res.send('{}');
  } else {
    res.status(401);
    res.send('{"error":"401 Unauthorized", "reason": "You specify user name!"}');
  }
});

app.get('/protect/hello', (req, res) => {
  const sessionId = req.cookies['SESSION'];

  if (!!sessionId && !!sessionMap[sessionId]) {
    res.send('<html><head><title>Good</title></head><body>Hello!!!</body></html>');
  } else {
    res.status(403);
    res.send('<html><head><title>Bad</title></head><body>:(</body></html>');
  }
});

app.listen(port, () => {
  console.info(`Listening on port ${port}`);
});
