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

  function createMeasureBlock(debug) {
    var container = document.createElement("div"),
        textContainer = document.createElement("span"),
        caretPosition = document.createElement("span");

    // indentifiers
    container.setAttribute("class",
                           [
                             "ui-keyboard-container",
                             debug ? "debug" : ""
                           ].join(" "));
    textContainer.setAttribute("class", ["ui-keyboard-text-container"].join(" "));
    caretPosition.setAttribute("class", ["ui-keyboard-caret-position"].join(" "));

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

    // Default textarea styles
    textContainer.style.whiteSpace = 'pre-wrap';
    if (!isInput) {
      textContainer.style.wordWrap = 'break-word'; // only for textarea-s
    }

    // attributes for visualisation
    if (debug) {
      container.style.position = "relative";
      container.classList.add("debug");
      container.style.border = "1px black dotted";
      textContainer.style.backgroundColor = "#F0F8FF";
      caretPosition.innerHTML = "";
    } else {
      container.style.position = "absolute";
      container.classList.remove("debug");
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
        computed = window.getComputedStyle(original),
        restContent = original.value.substring(position) || (computed['textAlign'] === 'right' ? "&#x200B" : '.'),  // || because a completely empty faux span doesn't render at all
        textContainer = measureBlock.textContainer,
        caretPosition = measureBlock.caretPosition;

    textContainer.innerHTML = textContent;
    caretPosition.innerHTML = restContent;
  }

  function createPseudoCaret(className="ui-keyboard-caret") {
    var caret = document.createElement("div");

    caret.setAttribute("class", className);
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
      textAlign: computed['textAlign'],
      left: caretPosition.offsetLeft + parseInt(computed['borderLeftWidth']),
      height: parseInt(computed['lineHeight']),
      selectionStart: original.selectionStart,
      selectionEnd: original.selectionEnd
    };
  }

  function removePseudoCaret(target) {
    var pseudoCaret = pseudoCaretOn.pseudoCaret;

    if (pseudoCaret && target.parentNode === pseudoCaret.parentNode) {
      pseudoCaret.parentNode.removeChild(pseudoCaret);
    }
  }

  function attachPseudoCaret(target, debug) {
    var measureBlock;
    if (!attachPseudoCaret.measureBlock) {
      measureBlock = createMeasureBlock(debug);
      attachPseudoCaret.measureBlock = measureBlock;
      document.body.appendChild(measureBlock.container);
    } else {
      measureBlock = attachPseudoCaret.measureBlock;
    }

    target.addEventListener("focus", () => {
      cloneAttributes(target, measureBlock);
      updateText(target, measureBlock);
      ignoreOriginalAttributes(target, measureBlock, debug);
      pseudoCaretOn(target, getCaretPosition(target, measureBlock));
    });

    target.addEventListener("blur", () => {
      removePseudoCaret(target);
    });

    target.addEventListener("keyup", () => {
      cloneAttributes(target, measureBlock);
      ignoreOriginalAttributes(target, measureBlock, debug);
      updateText(target, measureBlock);
      pseudoCaretOn(target, getCaretPosition(target, measureBlock));
    });

    target.addEventListener("input", () => {
      cloneAttributes(target, measureBlock);
      ignoreOriginalAttributes(target, measureBlock, debug);
      updateText(target, measureBlock);
      pseudoCaretOn(target, getCaretPosition(target, measureBlock));
    });
  }

  window.addEventListener("load", () => {
    var originals = document.querySelectorAll(".original");
    originals.forEach((node) => attachPseudoCaret(node, true));
  });
})(window, document);
