{-# LANGUAGE FlexibleContexts #-}
module Text.JIS.JISX0208.Parser where

import Text.JIS.JISX0208.JISChar (JISChar, newJISChar)

import Data.Maybe (catMaybes)
import Text.Parsec ((<|>), ParseError, ParsecT, Stream, char, endOfLine, hexDigit, many, parse, satisfy, sepEndBy, space, string, tab, try)

parseFile :: String -> Either ParseError [JISChar]
parseFile content = catMaybes <$> parse parseContent "JISX0208.TXT" content

parseContent :: Stream String m t => ParsecT String () m [Maybe JISChar]
parseContent = parseLine `sepEndBy` endOfLine

parseLine :: Stream String m t => ParsecT String () m (Maybe JISChar)
parseLine = try commentLine <|> charLine

commentLine :: Stream String m t => ParsecT String () m (Maybe JISChar)
commentLine =
  Nothing
  <$ char '#'
  <* many (satisfy (/= '\n'))

charLine :: Stream String m t => ParsecT String () m (Maybe JISChar)
charLine =
  (Just <$>)
  $ newJISChar
  <$> readHex
  <* tab
  <*> readHex
  <* tab
  <*> readHex
  <* (tab >> char '#' >> space)
  <*> many (satisfy (/= '\n'))

readHex :: Stream String m t => ParsecT String () m String
readHex =
  string "0x"
  *> many hexDigit
