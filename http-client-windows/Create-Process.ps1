<#
.SYNOPSIS
プロセスを作成します。

.DESCRIPTION
プロセスを作成します。与えられたidをリクエストJSONのテンプレートに適用してサーバーにPOSTします。

.PARAMETER Foobar
id

.EXAMPLE
.\Create-Process.ps1 1

#>
Param (
    [Parameter(Mandatory=$true, ValueFromPipelineByPropertyName=$true)]
    [string] $Id
)

. .\Source.ps1

$Template = (Get-Content .\create-process-template.json)

$RequestBody = [String]::Format($Template, $Id).Replace('"', '\"')

Write-Host $RequestBody

curl.exe -u user01:Password -X POST -H "Content-Type: application/json" "http://localhost:3000/processes" -d "$RequestBody" | jq.exe

# Local Variables:
# coding: utf-8-dos
# End:
