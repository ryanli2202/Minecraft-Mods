$ErrorActionPreference = 'Stop'
$root = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..'))
$libs = Join-Path $root 'libs'
New-Item -ItemType Directory -Force -Path $libs | Out-Null
# Official release metadata; third-party binaries are never redistributed in the source/release ZIP.
$releases = @(
    @('curios','curios-forge-5.14.1+1.20.1.jar'),
    @('irons-spells-n-spellbooks','irons_spellbooks-1.20.1-3.16.1.jar'),
    @('irons-lib','irons_lib-1.20.1-1.1.0.jar'),
    @('geckolib','geckolib-forge-1.20.1-4.8.4.jar'),
    @('caelus','caelus-forge-3.2.0+1.20.1.jar'),
    @('playeranimator','player-animation-lib-forge-1.0.2-rc1+1.20.jar'),
    @('parcool','ParCool-1.20.1-3.4.3.3.jar'),
    @('jei','jei-1.20.1-forge-15.20.0.132.jar'),
    @('jade','Jade-1.20.1-Forge-11.13.2.jar'),
    @('trashslot','trashslot-forge-1.20.1-15.1.5.jar'),
    @('balm','balm-forge-1.20.1-7.3.38-all.jar'),
    @('rpg-hud','[1.20.1][Forge] RPG-HUD-3.13.jar','rpg-hud-3.13.jar')
)
foreach ($release in $releases) {
    $name = if ($release.Length -gt 2) { $release[2] } else { $release[1] }
    $target = Join-Path $libs $name
    $versions = Invoke-RestMethod -Uri ('https://api.modrinth.com/v2/project/' + $release[0] + '/version?game_versions=%5B%221.20.1%22%5D&loaders=%5B%22forge%22%5D')
    $asset = $versions.files | Where-Object { $_.filename -eq $release[1] } | Select-Object -First 1
    if (!$asset) { throw "Exact release not found: $($release[1]). Supply the official release manually in libs." }
    if (!(Test-Path -LiteralPath $target) -or (Get-FileHash -LiteralPath $target -Algorithm SHA512).Hash.ToLower() -ne $asset.hashes.sha512) {
        Invoke-WebRequest -Uri $asset.url -OutFile $target
    }
    if ((Get-FileHash -LiteralPath $target -Algorithm SHA512).Hash.ToLower() -ne $asset.hashes.sha512) { throw "Checksum mismatch: $name" }
    Write-Output "Verified $name"
}
