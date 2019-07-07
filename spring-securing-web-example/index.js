/*global fetch:false, btoa:false, FormData:false */
function doGet() {
  fetch('http://localhost:8080/greeting', {
    cors: true,
    headers: {
      'Authorization': `Basic ${btoa('user:password')}`
    },
  });
};

function doPost() {
  const formData = new FormData();
  formData.append('name', 'John');

  fetch('http://localhost:8080/greeting', {
    method: 'POST',
    cors: true,
    headers: {
      'Authorization': `Basic ${btoa('user:password')}`
    },
    body: formData
  }).then(resp => resp.json()).then(console.log, console.error);
};

function bind(id, func) {
  const code = document.getElementById(`${id}-code`);
  code.innerHTML = func.toString();

  const button = document.getElementById(`${id}-button`);
  button.addEventListener('click', function() {
    func();
  });
}

window.addEventListener('load', function() {
  bind('doget', doGet);
  bind('dopost', doPost);
});
