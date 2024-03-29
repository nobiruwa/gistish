Param (
    [string]
    $SettingsPath,
    [string]
    $SourceRootDirectory,
    [string]
    $DestinationRootDirectory
)

# settings.jsonを読み込みます。
Function Read-SettingsJSON () {
    Get-Content $SettingsPath | ConvertFrom-JSON
}

# $SourcePathとnameをつなげてコピー元のパスを取得します。
Function Get-SourcePath (
    [string]
    $RelativeFilePath
) {
    Join-Path $SourceRootDirectory $RelativeFilePath
}

Function Main () {
    $Settings  = (Read-SettingsJSON)
    ForEach ($Item in $Settings) {
        $SourceFullPath = (Get-SourcePath $Item.name)
        $DestinationRelativePath = $Item.path
        $DestinationFullPath = Join-Path $DestinationRootDirectory $DestinationRelativePath

        If (Test-Path $DestinationFullPath -PathType Leaf) {
            If (Test-Path $SourceFullPath -PathType Leaf) {
                # ファイルを上書きコピーする
                Copy-Item $SourceFullPath $DestinationFullPath
            } Else {
                Write-Host "Source does not exist:" $SourceFullPath
            }
        } Else {
            Write-Host "Destination does not exist:" $DestinationFullPath
        }
    }
}

Main

# Local Variables:
# coding: utf-16-dos
# End:
