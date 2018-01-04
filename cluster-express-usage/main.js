/*global require:false */
const cluster = require('cluster');

if (cluster.isMaster) {
  const request = require('request');
  const worker = cluster.fork();
  cluster.on('exit', function(worker, code, signal) {
    console.log(`worker ${worker.process.pid} died`);
  });

  const get = () => {
    return new Promise((resolve, reject) => {
      request.get('http://localhost:5000/', (error, response, body) => {
        if (error) {
          reject(error);
        }
        resolve(body);
      });
    });
  };
  cluster.on('listening', () => {
    Promise.all(new Array(100).fill(get())).then(function(bodys) {
      console.log(bodys);
      worker.kill();
    });
  });
} else {
  const express = require('express');
  const app = express();
  app.get('/', (req, res) => res.send('Hello'));
  app.listen(5000);
}
