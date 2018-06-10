(function(window, document, undefined) {
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

  window.addEventListener("load", () => {
    var original = document.querySelector("#original2");

    original.addEventListener("keyup", () => {
      pseudoCaretOn(original, window.getCaretCoordinates(original, original.selectionStart));
    });
    original.addEventListener("input", () => {
      pseudoCaretOn(original, window.getCaretCoordinates(original, original.selectionStart));
    });
  });
})(window, document);
