# リスト1-49
# PowerShellでitemから始まるファイル一覧だけを表示する
New-Item -Force -ItemType Directory 1-49
1..10 | ForEach-Object { New-Item -Force -ItemType File 1-49\item$_.txt }
Get-ChildItem item*.txt -File
