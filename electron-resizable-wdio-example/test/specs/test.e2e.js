/*global describe it */
import { browser } from '@wdio/globals';
import { expect as expectChai } from 'chai';

async function move(browser, selector, dx, dy) {
  const beforeLoc = await browser.$(selector).getLocation();
  await browser.$(selector).dragAndDrop({ x: dx, y: dy });
  const afterLoc = await browser.$(selector).getLocation();

  return {
    dx: afterLoc.x - beforeLoc.x,
    dy: afterLoc.y - beforeLoc.y,
  };
}

describe('Electron Testing', () => {
  it('should be resizable', async() => {
    const expected = {
      dx: 120, // 10 * grid
      dy: 240, // 10 * grid
    };
    const actual = await move(browser, '#resizable .ui-resizable-se', expected.dx, expected.dy);

    expectChai(actual).to.be.deep.equal(expected);
  });
});

