Param (
    [string]
    $SettingsPath,
    [string]
    $SourceFullPath,
    [string]
    $DestinationFolderName
)

# destination.jsonを読み込みます。
Function Read-SettingsJSON () {
    Get-Content $SettingsPath | ConvertFrom-JSON
}

# ファイルのコピー先ディレクトリのフルパスを作成します。
Function Get-DestinationDirectoryPath (
    [string]
    $DestinationRootDirectory
) {
    Write-Host $DestinationRootDirectory $DestinationFolderName
    Join-Path $DestinationRootDirectory $DestinationFolderName
}

# ディレクトリを再帰的に作成します。
Function Create-DirectoryRecursively (
    [string]
    $DirectoryFullPath
) {
    [System.IO.Directory]::CreateDirectory()
}

# 
Function Get-DestinationFullPath (
    [string]
    $DirectoryPath
) {
    Join-Path $DirectoryPath $SourceFullPath
}

Function Main () {
    $Settings = (Read-SettingsJSON)
    ForEach ($Item in $Settings) {

        $ServerName = $Item.name
        $DestinationDirectory = Get-DestinationDirectoryPath $Item.directory
        $DestinationFullPath = Get-DestinationFullPath $DestinationDirectory

        If (-Not (Test-Path $DestinationDirectory)) {
            New-Item -Path $DestinationDirectory -ItemType "directory"
        }

        Copy-Item $SourceFullPath $DestinationFullPath

    }
}

Main

# Local Variables:
# coding: utf-16-dos
# End:
