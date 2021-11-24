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
  Write-Error ("�s���ȃ��[�h���w�肳��܂����B: {0}" -f $Mode)
  # ���̃X�N���v�g�����s����powershell��$LASTEXITCODE
  # �܂��̓o�b�`�t�@�C����%ERRORLEVEL%�ɂ�1���i�[����܂��B
  Exit 1
}

& $ScriptPath 2>&1