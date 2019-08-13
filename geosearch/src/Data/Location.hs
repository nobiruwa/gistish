module Data.Location where

import Data.Text.Lazy (Text)

data Location = Location
  { locationPrefectureName :: Text
  , locationCityName :: Text
  , locationTownName :: Text
  , locationGeometric :: Geometric
  } deriving (Eq, Show)

data Geometric = Geometric
  { geometricLongitude :: Double
  , geometricLatitude :: Double
  } deriving (Eq, Show)
