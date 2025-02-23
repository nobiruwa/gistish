/*global require */
const express = require('express');

const app = express();

app.use(express.json());
app.use(express.urlencoded({
        extended: true,
        inflate: true,
        limit: '1mb',
        parameterLimit: 5000,
        type: 'application/x-www-form-urlencoded',
}));

app.get('/', (req, res) => {
  res.header('Content-Type', 'text/html');
  res.send(`
<html>
  <head>
    <title>Karate Target</title>
  </head>
  <body>
    <form method="post" action="/protect/hello">
      <div>
        <span>Username</span>
      </div>
      <div>
        <input id="username">
      </div>
      <div>
        <input id="submit" type="submit">
      </div>
    </form>
  </body>
</html>
`);
});

app.post('/protect/hello', (req, res) => {
  console.log(req.body);
  res.header('Content-Type', 'text/html');
  res.send(`
<html>
  <head>
    <title>Karate Protected Page</title>
  </head>
  <body>Hello!!!</body>
</html>
`);
});


const port = 3000;

app.listen(port, () => console.log(`listening on port ${port}`));
