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

# $SourcePathとpathをつなげてコピー元のパスを取得します。
Function Get-SourcePath (
    [string]
    $RelativeFilePath
) {
    Join-Path $SourceRootDirectory $RelativeFilePath
}

Function Main () {
    $Settings = (Read-SettingsJSON)
    ForEach ($Item in $Settings) {
        $SourceFullPath = (Get-SourcePath $Item.path)
        $DestinationFullPath = (Join-Path $DestinationRootDirectory $Item.name)

        If (-Not (Test-Path $DestinationRootDirectory)) {
            New-Item -Path $DestinationRootDirectory -ItemType "directory"
        }

        Copy-Item $SourceFullPath $DestinationFullPath

    }
}

Main

# Local Variables:
# coding: utf-16-dos
# End:
