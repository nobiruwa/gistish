# リスト1-59
# 必要なプロパティだけ出力する
New-Item -Force -ItemType Directory 1-59
New-Item -Force -ItemType File 1-59/file.txt
1..10 | ForEach-Object { New-Item -Force -ItemType File 1-59/file$_.txt }
Get-ChildItem 1-59 | Select-Object Name,DirectoryName,Length
