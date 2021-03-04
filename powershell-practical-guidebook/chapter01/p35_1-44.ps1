# リスト1-44
# PowerShellで10個のファイルを作成する
New-Item -Force -ItemType Directory 1-44
1..10 | ForEach-Object { New-Item -Path 1-44\item$_.txt }
