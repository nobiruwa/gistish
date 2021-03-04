# リスト1-52
# PowerShellで10個のファイルをリネームする
New-Item -Force -ItemType Directory 1-52
1..10 | ForEach-Object { New-Item -Force -ItemType File 1-52/item$_.txt }
Get-ChildItem 1-52\item*.txt | ForEach-Object { Rename-Item -Path $_ -NewName $_.Name.Replace("item", "file") }
