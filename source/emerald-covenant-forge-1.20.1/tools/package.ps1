param([string]$ProjectRoot=(Split-Path -Parent $PSScriptRoot),[string]$ReleaseVersion='1.1.0')
$ErrorActionPreference='Stop'
$ProjectRoot=(Resolve-Path -LiteralPath $ProjectRoot).Path
$dist=Join-Path $ProjectRoot "dist/$ReleaseVersion"
New-Item -ItemType Directory -Force -Path $dist | Out-Null
$jar=Join-Path $ProjectRoot "build/libs/emerald-covenant-forge-1.20.1-$ReleaseVersion.jar"
if(-not(Test-Path -LiteralPath $jar)){throw 'Build the release JAR first.'}
Copy-Item -LiteralPath $jar -Destination $dist
Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem
function Write-Zip([string]$Name,[hashtable]$Entries) {
    $path=Join-Path $dist $Name
    $stream=[IO.File]::Open($path,[IO.FileMode]::Create)
    $archive=[IO.Compression.ZipArchive]::new($stream,[IO.Compression.ZipArchiveMode]::Create)
    try { foreach($relative in ($Entries.Keys|Sort-Object)) { [IO.Compression.ZipFileExtensions]::CreateEntryFromFile($archive,$Entries[$relative],$relative,[IO.Compression.CompressionLevel]::Optimal)|Out-Null } }
    finally { $archive.Dispose();$stream.Dispose() }
}
$install=@{}
$install["mods/emerald-covenant-forge-1.20.1-$ReleaseVersion.jar"]=$jar
foreach($file in @('README.txt','catalogue.example.json','verification/TEST-RESULTS.txt')){$install[$file]=Join-Path $ProjectRoot $file}
foreach($name in @('01-weapons.png','02-armour.png','03-bell-bearings.png','04-provisions.png','05-spells.png','06-spellbooks.png')){$install["previews/$name"]=Join-Path $ProjectRoot "verification/$ReleaseVersion/compatibility/$name"}
Write-Zip "Emerald-Covenant-$ReleaseVersion-Forge-1.20.1-install.zip" $install
$source=@{}
foreach($folder in @('src','gradle','artwork','tools')) {
    Get-ChildItem -LiteralPath (Join-Path $ProjectRoot $folder) -Recurse -File | ForEach-Object {
        $relative=[IO.Path]::GetRelativePath($ProjectRoot,$_.FullName).Replace('\','/')
        $source[$relative]=$_.FullName
    }
}
foreach($file in @('README.txt','catalogue.example.json','verification/TEST-RESULTS.txt','build.gradle','settings.gradle','gradle.properties','gradlew','gradlew.bat','.gitignore')){$source[$file]=Join-Path $ProjectRoot $file}
Write-Zip "Emerald-Covenant-$ReleaseVersion-Forge-1.20.1-source.zip" $source
$hashes=Get-ChildItem -LiteralPath $dist -File | Where-Object {$_.Extension -in @('.jar','.zip')} | Get-FileHash -Algorithm SHA256 | ForEach-Object { $_.Hash.ToLower()+'  '+[IO.Path]::GetFileName($_.Path) }
[IO.File]::WriteAllLines((Join-Path $dist 'SHA256SUMS.txt'),$hashes,[Text.UTF8Encoding]::new($false))
Get-ChildItem -LiteralPath $dist -File | Select-Object Name,Length
