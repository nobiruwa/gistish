/* global after:false, afterEach:false, before:false, beforeEach:false, describe:false, it:false, require:false */
import proxyquire from 'proxyquire';
import { assert, expect } from 'chai';
import sinon from 'sinon';

// electronの動作を確認するのではなく、
// プロダクトコードの内容が正しいことを確認したい。
const fakeOn = sinon.spy();
const fakeLoadURL = sinon.spy();
const faketron = {
  BrowserWindow: function() {
    this.on = fakeOn;
    this.loadURL = fakeLoadURL;
  }
};

function resetFaketron() {
  fakeOn.resetHistory();
  fakeLoadURL.resetHistory();
}

const main = proxyquire('../../../src/main-process/main.js', { electron: faketron }).default;

describe('main', function() {
  describe('launch', function() {
    afterEach(function() {
      // importで仕込んだstub/spyが存在する場合はリセットする
      resetFaketron();
      sinon.restore();
    });
    it('should be successful.', function() {
      assert.equal(fakeOn.callCount, 0);

      main.onReady();

      assert.ok(fakeOn.calledWith('closed', main.onClose));
      assert.equal(fakeOn.callCount, 1);
      assert.ok(fakeLoadURL.calledWith(sinon.match(/file:\/\/.*index.html/)));
      assert.equal(fakeLoadURL.callCount, 1);
    });
  });
  describe('close', function() {
    it('should be successful.', function() {
      main.onClose();
    });
  });
});
