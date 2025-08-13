/*global before:false describe:false it:false */
import { browser } from '@wdio/globals';
import { expect as expectChai } from 'chai';

async function move(browser, selector, dx, dy) {
  const initialMouseX = 1;
  const initialMouseY = 1;

  const beforeLoc = await browser.$(selector).getLocation();

  await browser.$(selector).dragAndDrop({ x: dx, y: dy });
  const afterLoc = await browser.$(selector).getLocation();

  return {
    dx: afterLoc.x - beforeLoc.x,
    dy: afterLoc.y - beforeLoc.y,
  };
}

describe('Electron Testing', () => {
  it('should be draggable', async() => {
    const expected = {
      dx: 100,
      dy: 200,
    };
    const actual = await move(browser, '#draggable', expected.dx, expected.dy);

    
  });
});

