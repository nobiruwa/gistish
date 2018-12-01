/* global after:false, afterEach:false, before:false, beforeEach:false, describe:false, it:false, require:false */
import main from '../../../src/main-process/main.js';

import electron from 'electron';
import { assert } from 'chai';

describe('main', function() {
  describe('launch', function() {
    it('should be successful.', function() {
      main.onReady();
    });
  });
  describe('close', function() {
    it('should be successful.', function() {
      main.onClose();
    });
  });
});
