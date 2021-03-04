# リスト1-60
# CSVファイルとして出力する
New-Item -Force -ItemType Directory 1-60
New-Item -Force -ItemType File 1-60/file.txt
1..10 | ForEach-Object { New-Item -Force -ItemType File 1-60/file$_.txt }
Get-ChildItem -Path 1-60/file* | Select-Object Name,DirectoryName,Length | Export-Csv -Path 1-60/output.csv
