{-# LANGUAGE OverloadedStrings #-}
module Template (apply) where

import Types (Dependency(..))

import Data.Aeson ((.=), object, pairs, ToJSON(..))
import Data.Set (Set)
import Data.Text (Text)
import qualified Data.Text.Lazy as TL
import qualified Data.Set as Set
import qualified Text.Mustache as M

newtype Dependencies = Dependencies
  { unDependencies :: Set Dependency
  } deriving (Eq, Show)

instance ToJSON Dependency where
  toJSON (Dependency from to) = object
    [ "from" .= from
    , "to" .= to
    ]

instance ToJSON Dependencies where
  toJSON (Dependencies deps) = object [ "dependencies" .= toJSONList (Set.toList deps) ]

apply :: M.Template -> Set Dependency -> Text
t `apply` ds = TL.toStrict $ M.renderMustache t (toJSON (Dependencies ds))
