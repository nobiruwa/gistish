module ClassPath.Dependency where

import qualified ClassPath.Class as C
import Types (Dependency(..))

import Data.Text (Text)

{-| クラスを定義したファイル名を返します。
-}
onlyName :: Dependency -> Dependency
onlyName (Dependency from to) = Dependency (C.onlyName from) (C.onlyName to)

{-| JARのなかのクラスであればjarファイル名を返します。ファイルシステムにあるクラスであればクラスを定義したファイル名を返します。
-}
onlyJarName :: Dependency -> Dependency
onlyJarName (Dependency from to) = Dependency (C.onlyJarName from) (C.onlyJarName to)

{-| 依存関係のエッジの両端ノードのいずれかが正規表現にマッチするかをテストします。
-}
testRegex :: Text -> Dependency -> Bool
testRegex re (Dependency from to) = C.testRegex re from || C.testRegex re to
