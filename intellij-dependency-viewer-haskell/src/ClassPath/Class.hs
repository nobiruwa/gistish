module ClassPath.Class where

import Types (Class)

import Data.Text (Text)
import qualified Data.Text as T
import Codec.Binary.UTF8.String (decodeString)
import Text.Regex.Posix ((=~))

{-| クラスを定義したファイル名を返します。
-}
onlyName :: Class -> Class
onlyName = T.reverse . T.takeWhile (/= '/') . T.reverse

{-| JARのなかのクラスであればjarファイル名を返します。ファイルシステムにあるクラスであればクラスを定義したファイル名を返します。
-}
onlyJarName :: Class -> Class
onlyJarName = onlyName . T.takeWhile (/= '!')

{-| 正規表現にマッチするかをテストします。
-}
testRegex :: Text -> Class -> Bool
testRegex re c = decode re =~ decode c :: Bool
  where
    decode = decodeString . T.unpack

