/*global jQuery:false */
(function(window, document, undefined) {
  var properties = [
    'direction',  // RTL support
    'boxSizing',
    'width',  // on Chrome and IE, exclude the scrollbar, so the mirror div wraps exactly as the textarea does
    'height',
    'overflowX',
    'overflowY',  // copy the scrollbar for IE

    'borderTopWidth',
    'borderRightWidth',
    'borderBottomWidth',
    'borderLeftWidth',
    'borderStyle',

    'paddingTop',
    'paddingRight',
    'paddingBottom',
    'paddingLeft',

    // https://developer.mozilla.org/en-US/docs/Web/CSS/font
    'fontStyle',
    'fontVariant',
    'fontWeight',
    'fontStretch',
    'fontSize',
    'fontSizeAdjust',
    'lineHeight',
    'fontFamily',

    'textAlign',
    'textTransform',
    'textIndent',
    'textDecoration',  // might not make a difference, but better be safe

    'letterSpacing',
    'wordSpacing',

    'tabSize',
    'MozTabSize'
  ];

  function createMeasureBlock() {
    var container = document.createElement("div"),
        textContainer = document.createElement("span"),
        caretPosition = document.createElement("span");

    // indentifiers
    container.setAttribute("class", ["container"].join(" "));
    textContainer.setAttribute("class", ["text-container"].join(" "));
    caretPosition.setAttribute("class", ["caret-position"].join(" "));

    // constructuring
    container.appendChild(textContainer);
    container.appendChild(caretPosition);

    return {
      container: container,
      textContainer: textContainer,
      caretPosition: caretPosition
    };
  }

  function cloneAttributes(original, measureBlock) {
    var container = measureBlock.container,
        computed = window.getComputedStyle(original);
    properties.forEach(function(propName) {
      var originalValue = computed[propName];
      container.style[propName] = originalValue;
    });
  }

  function ignoreOriginalAttributes(original, measureBlock, debug) {
    var container = measureBlock.container,
        textContainer = measureBlock.textContainer,
        caretPosition = measureBlock.caretPosition,
        isInput = original.nodeName === "INPUT";

    if (window.mozInnerScreenX != null) {
      if (original.scrollHeight > parseInt(window.getComputedStyle(original).height)) {
        container.style.overflowY = 'scroll';
      }
    } else {
      container.style.overflow = 'hidden';  // for Chrome to not render a scrollbar; IE keeps overflowY = 'scroll'
    }

    container.style.position = "absolute";

    // Default textarea styles
    textContainer.style.whiteSpace = 'pre-wrap';
    if (!isInput) {
      textContainer.style.wordWrap = 'break-word'; // only for textarea-s
    }

    // attributes for visualisation
    if (debug) {
      container.style.border = "1px black dotted";
      textContainer.style.backgroundColor = "#F0F8FF";
      caretPosition.innerHTML = "";
    } else {
      container.style.border = "";
      textContainer.style.backgroundColor = "";
      caretPosition.innerHTML = "";
    }
  }

  function lookupCaretPosition(original) {
    return original.selectionStart;
  }

  function updateText(original, measureBlock) {
    var position = lookupCaretPosition(original),
        textContent = original.value.substring(0, position) || "&#x200B",
        textContainer = measureBlock.textContainer;

    textContainer.innerHTML = textContent;
  }

  function createPseudoCaret() {
    var caret = document.createElement("div");

    caret.setAttribute("class", "pseudo-caret");
    caret.style.position = "absolute";

    return caret;
  }

  function pseudoCaretOn(element, position) {
    var pseudoCaret = pseudoCaretOn.pseudoCaret || (pseudoCaretOn.pseudoCaret = createPseudoCaret());

    element.parentNode.appendChild(pseudoCaret);

    pseudoCaret.style.left = position.left + "px";
    pseudoCaret.style.top = position.top + "px";
    pseudoCaret.style.height = position.height + "px";

  }

  function getCaretPosition(original, measureBlock) {
    var caretPosition = measureBlock.caretPosition,
        computed = window.getComputedStyle(original);
    return {
      top: caretPosition.offsetTop + parseInt(computed['borderTopWidth']),
      left: caretPosition.offsetLeft + parseInt(computed['borderLeftWidth']),
      height: parseInt(computed['lineHeight']),
      selectionStart: original.selectionStart,
      selectionEnd: original.selectionEnd
    };
  }

  window.addEventListener("load", () => {
    var original = document.querySelector("#original"),
        measureBlock = createMeasureBlock();

    document.body.appendChild(measureBlock.container);
    cloneAttributes(original, measureBlock);
    ignoreOriginalAttributes(original, measureBlock, true);

    original.addEventListener("keyup", () => {
      cloneAttributes(original, measureBlock);
      ignoreOriginalAttributes(original, measureBlock, true);
      updateText(original, measureBlock);
      pseudoCaretOn(original, getCaretPosition(original, measureBlock));
    });
    original.addEventListener("input", () => {
      cloneAttributes(original, measureBlock);
      ignoreOriginalAttributes(original, measureBlock, true);
      updateText(original, measureBlock);
      pseudoCaretOn(original, getCaretPosition(original, measureBlock));
    });
  });
})(window, document);
