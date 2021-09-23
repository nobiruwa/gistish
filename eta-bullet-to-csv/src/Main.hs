module Main where

import Markdown.Dumb

main :: IO ()
main = readFile "b.md" >>= writeFile "b.csv" . covertToCsv
