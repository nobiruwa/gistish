# リスト1-57
# Select-Objectで初めの1ファイルに関して全プロパティを出力する
New-Item -Force -ItemType Directory 1-57
1..10 | ForEach-Object { New-Item -Force -ItemType File 1-57/file$_.txt }
Get-ChildItem 1-57/* | Select-Object * -First 1
