{-|
Module      : Markdown.Dumb
Description : スペースの数で各行の箇条書きをどの列に置くかを決定する単純なパーサーです。
Copyright   : (c) ein, 2019
License     : MIT
Maintainer  : ein
Stability   : experimental
Portability : POSIX

@
- 1列目その1
  - 2列目その1
  - 2列目その2
- 1列目その2
  - 2列目その1
  - 2列目その2
@

'convertToCSV'によって変換すると:

@
"1列目その1"
,"2列目その1"
,"2列目その2"
"1列目その2"
,"2列目その1"
,"2列目その2"
@

となります。

0個以上の空白と箇条書きのシンボル/- /で始まらない行は1列目に置かれます。

@
    箇条書きでない
@

は

@
"    箇条書きでない"
@

となります。
-}
module Markdown.Dumb where

covertToCsv :: String -> String
covertToCsv = unlines . map toCsvLine . lines

toCsvLine :: String -> String
toCsvLine line =
  let spaces = takeWhile (== ' ') line
      item = dropWhile (== ' ') line
      numCommas = (length spaces) `div` 2
      commas = replicate numCommas ','
      csvLine = case item of
        [] -> "\"" ++ line ++ "\"" -- 1列目にlineそのまま
        ('-':' ':rest) -> commas ++ "\"" ++ rest ++ "\""
        xs -> "\"" ++ line ++ "\"" -- 1列目にlineそのまま
  in csvLine
