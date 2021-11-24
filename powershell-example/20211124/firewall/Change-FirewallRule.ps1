#Requires -RunAsAdministrator
Param
(
  [Parameter(Mandatory=$true)]
  [string]
  $Mode
)

$LogPath = Join-Path -Path $PSScriptRoot -ChildPath ("{0}.log" -f $MyInvocation.MyCommand.Name)
Start-Transcript -Path $LogPath -Append

If ($Mode -eq "1")
{
  $ScriptPath = Join-Path -Path $PSScriptRoot -ChildPath "1" | Join-Path -ChildPath "Change-FirewallRule.ps1"
}
ElseIf ($Mode -eq "2")
{
  $ScriptPath = Join-Path -Path $PSScriptRoot -ChildPath "2" | Join-Path -ChildPath "Change-FirewallRule.ps1"
}
ElseIf ($Mode -eq "3")
{
  $ScriptPath = Join-Path -Path $PSScriptRoot -ChildPath "3" | Join-Path -ChildPath "Change-FirewallRule.ps1"
}
Else
{
  Write-Error ("不正なモードが指定されました。: {0}" -f $Mode)
  # このスクリプトを実行したpowershellの$LASTEXITCODE
  # またはバッチファイルの%ERRORLEVEL%には1が格納されます。
  Exit 1
}

& $ScriptPath 2>&1