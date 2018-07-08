(function(window, document, undefined) {
  function createPseudoCaret() {
    var caret = document.createElement("div");

    caret.setAttribute("class", "ui-keyboard-caret");
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

  function removePseudoCaret(target) {
    var pseudoCaret = pseudoCaretOn.pseudoCaret;

    if (pseudoCaret && target.parentNode === pseudoCaret.parentNode) {
      pseudoCaret.parentNode.removeChild(pseudoCaret);
    }
  }

  function attachPseudoCaret(target, debug) {
    target.addEventListener("focus", () => {
      pseudoCaretOn(target, window.getCaretCoordinates(target, target.selectionStart, debug));
    });

    target.addEventListener("blur", () => {
      removePseudoCaret(target);
    });

    target.addEventListener("keyup", () => {
      pseudoCaretOn(target, window.getCaretCoordinates(target, target.selectionStart, debug));
    });

    target.addEventListener("input", () => {
      pseudoCaretOn(target, window.getCaretCoordinates(target, target.selectionStart, {
        debug: debug
      }));
    });
  }

  window.addEventListener("load", () => {
    var originals = document.querySelectorAll(".original2");
    originals.forEach((node) => attachPseudoCaret(node, true));
  });
})(window, document);
