{-# LANGUAGE OverloadedStrings #-}
module Data.Location where

import Data.Aeson ((.:), FromJSON(..), (.=), object, ToJSON(..), Value(..))
import Data.Text.Lazy (Text)

data Location = Location
  { locationPrefectureName :: Text
  , locationCityName :: Text
  , locationTownName :: Text
  , locationGeometric :: Geometric
  } deriving (Eq, Show)

instance FromJSON Location where
  parseJSON (Object v) = Location
    <$> v .: "prefectureName"
    <*> v .: "cityName"
    <*> v .: "townName"
    <*> v .: "geometric"

instance ToJSON Location where
  toJSON (Location p c t g) = object [ "prefectureName" .= p, "cityName" .= c, "townName" .= t, "geometric" .= toJSON g ]

data Geometric = Geometric
  { geometricLongitude :: Double
  , geometricLatitude :: Double
  } deriving (Eq, Show)

instance FromJSON Geometric where
  parseJSON (Object v) = Geometric <$> v .: "lon" <*> v .: "lat"

instance ToJSON Geometric where
  toJSON (Geometric lon lat) = object [ "lon" .= lon, "lat" .= lat ]
