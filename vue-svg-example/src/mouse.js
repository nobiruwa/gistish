let mousedown;
let mouseleave;
let mousemove;
let mouseup;

function copyCoordinates(e) {
  return {
    offsetX: e.offsetX,
    offsetY: e.offsetY,
    clientX: e.clientX,
    clientY: e.clientY,
    pageX: e.pageX,
    pageY: e.pageY,
    screenX: e.screenX,
    screenY: e.screenY,
  };
}

function onWindowMousedown(e) {
  this.mousedown = copyCoordinates(e);
}

function onWindowMouseleave(e) {
  this.mouseleave = copyCoordinates(e);
}

function onWindowMousemove(e) {
  this.mousemove = copyCoordinates(e);
}

function onWindowMouseup(e) {
  this.mouseup = copyCoordinates(e);
}

function startCapture() {
  window.addEventListener('mousedown', onWindowMousedown);
  window.addEventListener('mousemove', onWindowMousemove);
  window.addEventListener('mouseleave', onWindowMouseleave);
  window.addEventListener('mouseup', onWindowMouseup);
}
