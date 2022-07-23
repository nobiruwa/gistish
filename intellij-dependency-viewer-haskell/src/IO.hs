{-# LANGUAGE OverloadedStrings #-}
module IO
  ( applyTemplate
  , readDependencies
  ) where

import Types

import Data.Default.Class (def)
import Data.Set (Set)
import qualified Data.Set as Set
import Data.Text (Text)
import Prelude hiding (readFile)
import Text.Mustache (compileMustacheFile, Template)
import Text.XML (readFile)
import Text.XML.Cursor (fromDocument, attribute, parent, ($|), element, ($//), Cursor)

openFile :: FilePath -> IO Cursor
openFile file = fromDocument <$> readFile def file

getDependencies :: Cursor -> Set Dependency
getDependencies cursor =
  let dependencies = cursor $// element "dependency"
  in Set.fromList (map toDependency dependencies)
  where
    toDependency cur =
      let path = head $ attribute "path" cur
          par = head $ cur $| parent
          parentPath = head $ attribute "path" par
      in Dependency parentPath  path

readDependencies :: FilePath -> IO (Set Dependency)
readDependencies file = getDependencies <$> openFile file

applyTemplate :: FilePath -> (Template -> Text) -> IO Text
applyTemplate templateName action = do
  template <- compileMustacheFile templateName
  return (action template)
