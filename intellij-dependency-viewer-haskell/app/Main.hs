{-# LANGUAGE OverloadedStrings #-}
module Main where

import ClassPath.Dependency (onlyJarName, testRegex, replaceRegex)
import Graph (makeGraph, toDependencies)
import IO (applyTemplate, readDependencies)
import Template (apply)
import Types (Dependency(..))

import qualified Data.Set as Set
import qualified Data.Text as T
import qualified Data.Text.IO as T
import System.Environment (getArgs)

main :: IO ()
main = do
  -- ex) ["samples/sample.xml", "$PROJECT_DIR$/src/main/java/org/example/websocket/WebSocketServer.java"]
  [dependenciesXmlFile, rootFilePath] <- getArgs
  -- XMLを読み込み依存関係をメモリに展開する
  dependencies <- readDependencies dependenciesXmlFile
  let g = makeGraph (T.pack rootFilePath) dependencies
  let ds = Set.filter (not . testRegex ".*jbr.*") $ Set.map onlyJarName (toDependencies g)
  -- let ds = Set.map (\d -> replaceRegex ".*/([a-zA-Z0-9_]+\\.(java|class))" d "\\1") $ Set.filter (not . testRegex ".*jbr.*") $ (toDependencies g)
  text <- applyTemplate "templates/dependency.dot" (`apply` ds)
  T.putStrLn text
