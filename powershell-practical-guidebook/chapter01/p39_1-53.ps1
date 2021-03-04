# リスト1-53
# PowerShellでForEach-Objectを使わず10個のファイルをリネームする
New-Item -Force -ItemType Directory 1-53
1..10 | ForEach-Object { New-Item -Force -ItemType File 1-53/item$_.txt }
Get-ChildItem 1-53\item*.txt | Rename-Item -NewName { $_.Name.Replace("item", "file") }
