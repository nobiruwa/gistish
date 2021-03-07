# リスト1-56
# Select-Objectで全プロパティを出力する
New-Item -Force -ItemType Directory 1-56
1..10 | ForEach-Object { New-Item -Force -ItemType File 1-56/file$_.txt }
Get-ChildItem 1-56/* | Select-Object *
