/*global require:false */
import axios from 'axios';
import cluster from 'cluster';
import express from 'express';

// 使い方
// node main.mjs

if (cluster.isMaster) {
  const worker = cluster.fork();
  cluster.on('exit', function(worker, code, signal) {
    console.log(`worker ${worker.process.pid} died`);
  });

  const get = async () => {
    return await axios.get('http://localhost:5000/');
  };
  cluster.on('listening', async () => {
    for (let i = 0; i < 100; i++) {
      let response = await get();

      console.log(response.data);
    }
    worker.kill();
  });
} else {
  const app = express();
  app.get('/', (req, res) => res.send('Hello'));
  app.listen(5000);
}
