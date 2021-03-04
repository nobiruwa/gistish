# リスト1-67
# CSVファイルのLengthを集計する
Import-Csv -Path 1-67\output.csv | Measure-Object -Sum -Property Length
