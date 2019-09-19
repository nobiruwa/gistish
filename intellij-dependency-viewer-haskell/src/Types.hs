module Types where

import Data.Set (Set)
import Data.Text (Text)

data Graph = Graph Class (Set Node) deriving (Eq, Ord, Show)

data Node = Node Class (Set Node) deriving (Eq, Ord, Show)

type Class = Text

data Dependency = Dependency
  { dependencyFrom :: Class
  , dependencyTo :: Class
  } deriving (Eq, Ord, Show)
