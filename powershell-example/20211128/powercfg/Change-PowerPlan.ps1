Param
(
    [string]
    $PowerPlanName
)

function ConvertTo-Object
(
    [Parameter(ValueFromPipeline)]
    [Microsoft.PowerShell.Commands.MatchInfo]
    $MatchInfo
)
{
    [PSCustomObject]@{
        Guid = $MatchInfo.Matches.Groups[1].Value
        Name = $MatchInfo.Matches.Groups[2].Value
        Active = ($MatchInfo.Matches.Groups[3].Value)
    }
}

function Get-PowerPlanList ()
{
    $ListText = (powercfg /list)
    $Matches = ($ListText -Split "`r`n" | Select-String -Pattern ".+GUID: +([-0-9a-fA-F]+) +\((.+)\) *(\*)?")
    $Matches | % { ConvertTo-Object $_ }
}

function Get-ActivePowerPlan ()
{
    $ListText = (powercfg /getactivescheme)
    $Matches = ($ListText -Split "`r`n" | Select-String -Pattern ".+GUID: +([-0-9a-fA-F]+) +\((.+)\)")
    $Matches | ConvertTo-Object
}

function Set-PowerPlan
(
    [Parameter(ValueFromPipeline)]
    [PSCustomObject]
    $NewPowerPlanGuid
)
{
    $Command = "powercfg.exe /S {0}" -f $NewPowerPlanGuid.Guid
    Start-Process -NoNewWindow -FilePath "powercfg.exe" -ArgumentList "/S", $NewPowerPlanGuid.Guid
}

Write-Host "---電源プラン一覧(変更前)---"
powercfg /list

# コマンドライン引数で指定された名前の電源プランをアクティブにする
# 電源プランの表示名はおそらく重複しないため、高々1個の電源プランが
# Set-PowerPlanに渡される
Get-PowerPlanList | ? { $_.Name -eq $PowerPlanName } | % { Set-PowerPlan $_ }

Write-Host "---電源プラン一覧(変更後)---"
powercfg /list

# Local Variables:
# coding: utf-16-dos
# End:
