module ClassPath.Class where

import Types (Class)

import Data.Text (Text)
import qualified Data.Text as T
import Codec.Binary.UTF8.String (encodeString)
import Text.Regex (mkRegex, subRegex)
import Text.Regex.Posix ((=~))

{-| Unicode文字をUTF-8エンコードします。
-}
encode :: Text -> String
encode = encodeString . T.unpack

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
testRegex re c = encode c =~ encode re :: Bool

{-| 正規表現によって文字列を加工します。
-}
replaceRegex :: Text -> Class -> Text -> Class
replaceRegex re c repl =
  let regex = mkRegex (encode re)
      input = encode c
      replText = T.unpack repl
  in T.pack (subRegex regex input replText)
