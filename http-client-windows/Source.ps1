<#
.SYNOPSIS
dot source for http client.

.DESCRIPTION
dot source for http client.

.EXAMPLE
. .\Source.ps1

#>

# curl, jqのホームディレクトリがこのディレクトリのサブディレクトリとし
# て存在すると過程し検索します。発見したらCURL_HOMEにセットします。
Get-ChildItem -Path "curl-*" | Where-Object { $_.PSIsContainer } | ForEach-Object { Set-Variable -Name "CURL_HOME" -Value $_.FullName -Force }

Set-Variable -Name "CURL_BIN" -Value "$CURL_HOME\bin" -Force

# curl, jqのパスを追加します
If (!"$env:PATH".Contains("$CURL_BIN"))
{
    Set-Item env:PATH "$CURL_BIN;$env:PATH"
}

# jq-win64.exeのエイリアスjq.exeを作成します
Set-Alias -Name "jq.exe" -Value "jq-win64.exe" -Force

# このスクリプトをdot sourceとしてロードしたスクリプトでは、
# curl.exeとjq.exe(エイリアス)を利用できるようになります。
# 単に.\Source.ps1とした場合は、エイリアス等は使用できません。

# Local Variables:
# coding: utf-8-dos
# End:
