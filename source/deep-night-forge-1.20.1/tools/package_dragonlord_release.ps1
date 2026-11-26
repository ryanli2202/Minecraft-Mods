$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent
$version = '1.3.3'
$log = Get-Content (Join-Path $root 'dragonlord-tests.log') -Raw
if ($log -notmatch 'All (\d+) required tests passed' -or $log -notmatch 'BUILD SUCCESSFUL' -or $log -match 'failed!') {
    throw 'Build and server regressions must pass before packaging.'
}
$testCount = [regex]::Match($log, 'All (\d+) required tests passed').Groups[1].Value
$jarName = "deep-night-forge-1.20.1-$version.jar"
$jarPath = Join-Path $root "build/libs/$jarName"
Add-Type -AssemblyName System.IO.Compression.FileSystem
$archive = [IO.Compression.ZipFile]::OpenRead($jarPath)
try {
    if (!$archive.GetEntry('dev/deepnight/source/DragonLordParts.class')) { throw 'Missing ownership fix' }
    if ($archive.Entries.FullName -match '^dev/deepnight/qa/') { throw 'QA classes leaked into release' }
    $reader = [IO.StreamReader]::new($archive.GetEntry('META-INF/mods.toml').Open())
    try { if ($reader.ReadToEnd() -notmatch 'version="1.3.3"') { throw 'Wrong jar version' } }
    finally { $reader.Dispose() }
} finally { $archive.Dispose() }
$dist = Join-Path $root 'dist'
Copy-Item -LiteralPath $jarPath -Destination (Join-Path $dist $jarName)
$notes = @"
THE DEEP NIGHT $version - DRAGON LORD HITBOX FIX

All six Dragon Lord parts now forward incoming damage to their saved owner.
The main dragon handles health, armor, invulnerability, cooldowns and player
kill credit. Parts no longer take separate damage or hurt nearby dragons.
Existing saved parts automatically adopt a nearby living Dragon Lord once.
The damage cooldown expires during stun; rejected hits do not start it.
Native arrow/potion immunity and attack-phase invulnerability are retained.

Verification: build and all $testCount server GameTests passed, including the
22 boss lifecycle tests. New regressions cover all six parts, repeated
melee hits, shared health/bar updates, damage without an attacker, native
immunity, nearby-dragon isolation, orphan parts and saved ownership.
The full user modpack and an interactive client fight were not tested.

Install: replace the old Deep Night jar on both client and server. Keep only
one Deep Night version. Requires Forge 1.20.1 and GeckoLib 4.8.4; the install
ZIP includes GeckoLib. Do not install a duplicate GeckoLib jar.
"@
$notePath = Join-Path $dist "UPDATE-$version.txt"
Set-Content -LiteralPath $notePath -Value $notes
$zipPath = Join-Path $dist "Deep-Night-$version-Forge-1.20.1-install.zip"
$zip = [IO.Compression.ZipFile]::Open($zipPath, [IO.Compression.ZipArchiveMode]::Create)
try {
    [IO.Compression.ZipFileExtensions]::CreateEntryFromFile($zip, (Join-Path $dist $jarName), "mods/$jarName") | Out-Null
    [IO.Compression.ZipFileExtensions]::CreateEntryFromFile($zip, (Join-Path $root 'compat-mods/geckolib-forge-1.20.1-4.8.4.jar'), 'mods/geckolib-forge-1.20.1-4.8.4.jar') | Out-Null
    [IO.Compression.ZipFileExtensions]::CreateEntryFromFile($zip, $notePath, "UPDATE-$version.txt") | Out-Null
    foreach ($name in @('README.txt', 'ASSET-NOTES.txt')) {
        [IO.Compression.ZipFileExtensions]::CreateEntryFromFile($zip, (Join-Path $root $name), $name) | Out-Null
    }
} finally { $zip.Dispose() }
$hashes = @($jarName, "Deep-Night-$version-Forge-1.20.1-install.zip") | ForEach-Object {
    $hash = Get-FileHash -LiteralPath (Join-Path $dist $_) -Algorithm SHA256
    "$($hash.Hash)  $_"
}
Set-Content -LiteralPath (Join-Path $dist "SHA256-$version.txt") -Value $hashes
Write-Output "Packaged $version; $testCount server tests passed."
