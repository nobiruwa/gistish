# リスト1-58
# Get-Memberで全メンバーを出力する
New-Item -Force -ItemType Directory 1-58
1..10 | ForEach-Object { New-Item -Force -ItemType File 1-58/file$_.txt }
Get-ChildItem 1-58/* | Get-Member *
