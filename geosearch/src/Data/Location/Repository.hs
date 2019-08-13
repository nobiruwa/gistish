{-# LANGUAGE OverloadedStrings #-}
module Data.Location.Repository where

import Data.Location (Geometric(..), Location(..))

import Data.Attoparsec.ByteString.Char8 (Parser, double, inClass, many', parseOnly, satisfy, string)
import Data.ByteString (ByteString, pack, unpack)
import Data.Either (fromRight)
import Database.HDBC (SqlValue, execute, fetchAllRows, fromSql, prepare, toSql)
import Database.HDBC.PostgreSQL (Connection)
import Text.Printf (printf)

toPoint :: Double -> Double -> String
toPoint = printf "SRID=4326;POINT(%f %f)"

persist :: Connection -> Location -> IO Integer
persist conn (Location p c t (Geometric lon lat)) = do
  statement <- prepare conn "INSERT INTO location (prefecture_name, city_name, town_name, geometric) VALUES (?, ?, ?, ST_GeographyFromText(?))"
  execute statement [toSql p, toSql c, toSql t, toSql (toPoint lon lat)]

findWithinAndLimit :: Connection -> Geometric -> Double -> Integer -> IO [(Location, Double)]
findWithinAndLimit conn (Geometric lon lat) within limit = do
  let point = toPoint lon lat
  statement <- prepare conn "SELECT prefecture_name, city_name, town_name, ST_AsText(geometric), ST_Distance(?, geometric) as distance FROM location WHERE ST_DWithin(geometric, ST_GeographyFromText(?), ?) ORDER BY distance;"
  execute statement [ toSql point
                    , toSql point
                    , toSql within
                    ]
  map toLocationWithDistance <$> fetchAllRows statement
  where
    toLocationWithDistance [p, c, t, g, d] = (toLocation p c t g, fromSql d :: Double)
    toLocation p c t g = Location (fromSql p) (fromSql c) (fromSql t) (parsePoint (fromSql g))

parsePoint :: ByteString -> Geometric
parsePoint = fromRight (Geometric 0 0) . parseOnly pointParser

pointParser :: Parser Geometric
pointParser = do
  string "POINT("
  lon <- double
  string " "
  lat <- double
  string ")"
  return (Geometric lon lat)

clear :: Connection -> IO Integer
clear conn = do
  statement <- prepare conn "DELETE FROM location"
  execute statement []
