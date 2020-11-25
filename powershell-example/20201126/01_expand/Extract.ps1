Param (
    [string]
    $ZipPath,
    [string]
    $DestinationPath
)

Expand-Archive -Path $ZipPath -Destination $DestinationPath
