{-# LANGUAGE FlexibleContexts #-}

module Text.JIS.JISX0208.Parser where

import Data.Maybe (catMaybes)
import Text.JIS.JISX0208.JISChar (JISChar, newJISChar)
import Text.Parsec (ParseError, ParsecT, char, endOfLine, hexDigit, many, parse, satisfy, sepEndBy, space, string, tab, try, (<|>))

parseFile :: String -> Either ParseError [JISChar]
parseFile content = catMaybes <$> parse parseContent "JISX0208.TXT" content

parseContent :: (Monad m) => ParsecT String () m [Maybe JISChar]
parseContent = parseLine `sepEndBy` endOfLine

parseLine :: (Monad m) => ParsecT String () m (Maybe JISChar)
parseLine = try commentLine <|> charLine

commentLine :: (Monad m) => ParsecT String () m (Maybe JISChar)
commentLine =
  Nothing
    <$ char '#'
    <* many (satisfy (/= '\n'))

charLine :: (Monad m) => ParsecT String () m (Maybe JISChar)
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

readHex :: (Monad m) => ParsecT String () m String
readHex =
  string "0x"
    *> many hexDigit
