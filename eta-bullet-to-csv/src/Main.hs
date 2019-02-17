module Main where

import Markdown.Dumb

main :: IO ()
main =
  readFile "b.md"
  >>= return . covertToCsv
  >>= writeFile "b.csv"
