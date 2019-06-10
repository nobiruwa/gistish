module Text.JIS.JISX0208.JISChar where

import Data.Char (chr)
import Data.List (isPrefixOf)
import qualified Numeric

data JISChar
  = JISChar
    { jisCharShiftJIS :: Int
    , jisCharJISX0208 :: Int
    , jisCharUnicode  :: Int
    , jisCharShiftJISHex :: String
    , jisCharJISX0208Hex :: String
    , jisCharUnicodeHex :: String
    , jisCharUnicodeName :: String
    , jisChar :: Char
    }
  deriving (Eq, Show)

data Category
  = Category
    { categoryKanji :: [JISChar]
    , categoryHiragana :: [JISChar]
    , categoryKatakana :: [JISChar]
    , categorySymbol :: [JISChar]
    }
  deriving (Eq, Show)

newJISChar :: String -> String -> String -> String -> JISChar
newJISChar sjis jisx uni uName =
  let uChar = hexToChar uni
  in JISChar
     (hexToInt sjis)
     (hexToInt jisx)
     (hexToInt uni)
     sjis
     jisx
     uni
     uName
     uChar

isHiraganaLetter :: JISChar -> Bool
isHiraganaLetter = ("HIRAGANA LETTER" `isPrefixOf`) . jisCharUnicodeName

isKatakanaLetter :: JISChar -> Bool
isKatakanaLetter = ("KATAKANA LETTER" `isPrefixOf`) . jisCharUnicodeName

isKanji :: JISChar -> Bool
isKanji = (== "<CJK>") . jisCharUnicodeName

hexToInt :: String -> Int
hexToInt = fst . head . Numeric.readHex

hexToChar :: String -> Char
hexToChar = chr . hexToInt

categorize :: [JISChar] -> Category
categorize chars =
  let kanjis = filter isKanji chars
      noKanjis = filter (not . isKanji) chars
      hiraganas = filter isHiraganaLetter noKanjis
      noHiraganas = filter (not . isHiraganaLetter) noKanjis
      katakanas = filter isKatakanaLetter noHiraganas
      noKatakanas = filter (not . isKatakanaLetter) noHiraganas
      symbols = noKatakanas
  in Category kanjis hiraganas katakanas symbols
