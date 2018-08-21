window.addEventListener('load', function() {
  function assert() {
    if (!arguments[0]) {
      throw arguments[1];
    }
  }

  function createForm(parent, id, settings) {
    assert(id, 'フォームを作成するにはID属性が必要です。');

    // assume to handle monospace font
    const width = settings.fontSize / 2 * settings.columns,
          height = settings.fontSize * settings.rows;

    return d3.select(parent.node())
      .append('div')
      .attr('id', id)
      .attr('class', 'form')
      .style('width', (width + 1) + 'px')
      .style('height', (height + 1) + 'px');
  }

  function createBackground(parent, settings) {
    assert(parent.attr('id'), '背景にメッシュを描画するためにはID属性が必要です。');
    const parentId = parent.attr('id'),
          smallGridId = parentId + '--small-grid',
          gridId = parentId + '--grid';

    const fontSize = settings.fontSize;
    assert(fontSize, '引数フォントサイズを指定してください。');
    // assume to handle monospace font
    const width = fontSize / 2,
          height = fontSize;

    const svg = d3.select(parent.node()).append('svg')
          .attr('class', 'main-area--background')
          .attr('width', '100%')
          .attr('height', '100%');

    const defs = svg.append('defs');
    defs
      .append('pattern')
      .attr('id', smallGridId)
      .attr('class', 'small-grid')
      .attr('width', width)
      .attr('height', height)
      .attr('patternUnits', 'userSpaceOnUse')
      .append('path')
      .attr('d', ['M', width, '0', 'L', '0', '0', '0', height].join(' '))
      .attr('fill', 'none');

    const grid = defs
          .append('pattern')
          .attr('id', gridId)
          .attr('class', 'grid')
          .attr('width', width * 5)
          .attr('height', height * 5)
          .attr('patternUnits', 'userSpaceOnUse');

    const smallGridUrl = 'url(#' + smallGridId + ')';
    grid
      .append('rect')
      .attr('width', width * 5)
      .attr('height', height * 5)
      .attr('fill', smallGridUrl);

    grid
      .append('path')
      .attr('d', ['M', width * 5, '0', 'L', '0', '0', '0', height * 5].join(' '));

    const gridUrl = 'url(#' + gridId + ')';
    svg
      .append('rect')
      .attr('class', 'main-area--rect')
      .attr('width', '100%')
      .attr('height', '100%')
      .attr('fill', gridUrl);

    return svg;
  };

  (function() {
    const settings = {
      columns: 80,
      rows: 25 * 99,
      fontSize: 20
    };
    const form = createForm(d3.select('body'), 'one-of-main-area', settings);

    createBackground(form, settings);
  })();
});
