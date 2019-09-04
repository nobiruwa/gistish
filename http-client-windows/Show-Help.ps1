<#
.SYNOPSIS
curl.exeとjq.exeが使用可能であることを確認します。

.DESCRIPTION
curl.exeとjq.exeが使用可能であることを確認します。

curl.exeとjq.exeの-helpオプションの出力を続けて表示します。

.EXAMPLE
.\Show-Help.ps1

#>

. .\Source.ps1

curl.exe -help
jq.exe -help

# Local Variables:
# coding: utf-8-dos
# End:
