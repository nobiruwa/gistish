module ClassPath where

import Types (Class)

import Data.Text (Text)
import qualified Data.Text as T

{-| クラスを定義したファイル名を返します。
-}
onlyName :: Class -> Class
onlyName = T.reverse . T.takeWhile (/= '/') . T.reverse

{-| JARのなかのクラスであればjarファイル名を返します。ファイルシステムにあるクラスであればクラスを定義したファイル名を返します。
-}
onlyJarName :: Class -> Class
onlyJarName = onlyName . T.takeWhile (/= '!')
