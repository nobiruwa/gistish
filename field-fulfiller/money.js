// module Money where

// data MoneyChar = Neg
//                | One
//                | Zero
//                | Period
//                deriving (Eq)

// instance Show MoneyChar where
//   show Neg = "-"
//   show One = "1"
//   show Zero = "0"
//   show Period = "."

// showMoneyChars :: [MoneyChar] -> String
// showMoneyChars = concat . map show

// data Decimal = Decimal Int -- 少数点部の桁数

// data MoneyField = MoneyField
//   { moneyIsNegative :: Bool
//   , decimalPartLength :: Int
//   , moneyLength :: Int
//   } deriving (Eq, Show)

// generateMoneyValue :: MoneyField -> [MoneyChar]
// generateMoneyValue (MoneyField neg 0 len) = generateInteger neg len
// generateMoneyValue (MoneyField neg decLen len) = generateInteger neg (len - decLen - 1) ++ [Period] ++ replicate decLen Zero

// generateInteger :: Bool -> Int -> [MoneyChar]
// generateInteger _ 0 = []
// generateInteger _ 1 = [Zero]
// generateInteger True len = Neg : One : replicate (len - 2) Zero
// generateInteger False len = One : replicate (len - 1) Zero
