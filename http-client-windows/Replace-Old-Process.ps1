<#
.SYNOPSIS
プロセスを特定のプロセス定義から最新のプロセスに置き換えます。

.DESCRIPTION
プロセスを特定のプロセス定義から最新のプロセスに置き換えます。複数のプロセスが存在する場合、1件のプロセスにのみ適用されます。

.PARAMETER OldProcessDefinitionId
プロセス定義ID

.PARAMETER OldProcessDefinitionKey
プロセス定義キー

.EXAMPLE
.\Replace-Old-Process.ps1 process01:1:00001 process01

#>
Param (
    [Parameter(Mandatory=$true, ValueFromPipelineByPropertyName=$true)]
    [string] $OldProcessDefinitionId,
    [Parameter(Mandatory=$true, ValueFromPipelineByPropertyName=$true)]
    [string] $NewProcessDefinitionKey
)

. .\Source.ps1

# プロセスの検索
$OldProcess = (curl.exe -u user01:Password -H "Content-Type: application/json" "http://localhost:3000/processes?finished=false&processDefinitionKey=$OldProcessDefinitionId" | jq.exe -cr .[0])
$OldProcessId = (Echo $OldProcess | jq.exe -r .id)
$MetaInfo = (Echo $OldProcess | jq.exe -cr .metaInfo)

Write-Host $OldProcessId
Write-Host $MetaInfo

# プロセスの作成
$Template = (Get-Content .\replace-old-process-template.json)

$RequestBody = [String]::Format($Template, $MetaInfo).Replace('"', '\"')

Write-Host $RequestBody

curl.exe -u user01:Password -X POST -H "Content-Type: application/json" "http://localhost:3000/processes" -d "$RequestBody" | jq.exe

# プロセスの削除
curl.exe -u user01:Password -X DELETE -H "Content-Type: application/json" "http://localhost:3000/processes/$OldProcessDefinitionId"

# Local Variables:
# coding: utf-8-dos
# End:
