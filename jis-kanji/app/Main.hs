module Main where

import Text.JIS.JISX0208.JISChar
import Text.JIS.JISX0208.Parser
import Text.JIS.JISX0208.Writer

import Control.Monad (unless)
import Data.Char (chr)
import Data.Either (Either(..))
import Data.List (group)
import System.Directory (createDirectory, doesDirectoryExist)

verify :: [[a]] -> [a] -> Bool
verify separated total = (length . concat) separated == length total

main :: IO ()
main = do
  file <- readFile "resources/JIS0208.TXT"
  case parseFile file of
    Left e -> print e
    Right chars -> do
      exist <- doesDirectoryExist "result"
      unless exist (createDirectory "result")
      let (Category kanjis hiraganas katakanas symbols) = categorize chars

      -- 上記categorizeで漏れがないか検算
      unless (verify [kanjis, hiraganas, katakanas, symbols] chars) (error "categorizeにバグ")

      writeString "result/jisx0208-kanji.txt" kanjis
      writeString "result/jisx0208-hiragana.txt" hiraganas
      writeString "result/jisx0208-katakana.txt" katakanas
      writeString "result/jisx0208-symbol.txt" symbols
      writeJISChar "result/jisx0208.json" chars
