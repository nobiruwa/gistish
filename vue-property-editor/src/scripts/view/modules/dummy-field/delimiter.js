const fullWidthDelimiters = {
  '[': '［',
  ']': '］',
  '{': '｛',
  '}': '｝',
  ' ': '　',
  '': ''
};

const fullWidthSepratros = {
  ',': '，',
  '|': '｜',
  '': '',
  ' ': '　'
};

const digits = {
  '0': '０',
  '1': '１',
  '2': '２',
  '3': '３',
  '4': '４',
  '5': '５',
  '6': '６',
  '7': '７',
  '8': '８',
  '9': '９'
};

function getChar(charSet, halfChar, requiredFullWidth) {
  if (requiredFullWidth) {
    return charSet[halfChar];
  } else {
    return halfChar;
  }
}

function getCharWidth(halfChar, requiredFullWidth) {
  if (halfChar === '') {
    return 0;
  } else if (requiredFullWidth) {
    return 2;
  } else {
    return 1;
  }
}


// 文字を作るところは以下の
export default {
  data: function() {
    return {
      // 全角として表示するか
      renderAsFullWidth: false,
      // デリミタ
      openHalfParen: '[',
      closeHalfParen: ']',
      // セパレータ
      halfSeparator: ',',
      // 表示する幅[半角換算の文字数]
      widthByHalfChars: 10,
      // アラインメント
      align: 'left',
      // 文字種: number, ANK, 漢字, 混在
      type: '',
      // 入力可能文字数
      numberOfHalfChars: 6
    };
  },
  computed: {
    openParen: function() {
      return getChar(fullWidthDelimiters, this.openHalfParen, this.renderAsFullWidth);
    },
    closeParen: function() {
      return getChar(fullWidthDelimiters, this.closeHalfParen, this.renderAsFullWidth);
    },
    separator: function() {
      return getChar(fullWidthSepratros, this.halfSeparator, this.renderAsFullWidth);
    },
    // セパレータ抜きの文字領域
    widthByHalfCharsButParens: function() {
      // 0 or 1 or 2
      const openLen = getCharWidth(this.openParen, this.renderAsFullWidth);
      const closeLen = getCharWidth(this.closeParen, this.renderAsFullWidth);

      return this.widthByHalfChars - (openLen + closeLen);
    },
    // 括弧の間の文字
    chars: function() {
      // TODO プロパティ値による分岐
      return this.left_number_integer();
    }
  },
  methods: {
    // 左寄せx数字x小数点なしの場合
    left_number_integer: function() {
      // セパレータを含む文字'表示'領域の長さ
      const width = this.widthByHalfCharsButParens;
      console.log(`width = ${width} (セパレータを含む文字'表示'領域の長さ)`);
      // セパレータを含む'入力可能'文字数の長さ
      const availableWidth = Math.min(width, this.numberOfHalfChars);
      // 入力文字を決める
      const chars = [];
      let count = availableWidth;
      // 入力可能文字数分の追加
      while (count > 0) {
        let c = '';
        let l = 0;
        if (count == 1 && this.renderAsFullWidth) {
          // 半角1文字分の残りは半角空白で埋める
          c = ' ';
          l = 1;
        } else if (count === availableWidth) {
          // 文字の先頭は1
          c = getChar(digits, '1', this.renderAsFullWidth);
          l = getCharWidth('1', this.renderAsFullWidth);
        } else {
          // 途中の文字は0
          c = getChar(digits, '0', this.renderAsFullWidth);
          l = getCharWidth('0', this.renderAsFullWidth);
        }
        chars.push(c);
        count = count - l;
      }
      // 表示領域と入力可能文字領域のすきまは半角空白で埋める
      for (var i = 0; i < width - availableWidth; i++) {
        // U+2002 EN SPACEを使用(U+0020ではなくU+2000-U+2003のいずれかを使うべきと思われる)
        chars.push(' ');
      }
      return chars;
    }
  },
  template: '<span><span>{{openParen}}</span><span v-for="char in chars">{{ char }}</span><span>{{closeParen}}</span></span>'
};
