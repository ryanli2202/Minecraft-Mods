$ErrorActionPreference = 'Stop'
$root = [System.IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..'))
$release = Join-Path $root 'dist'
$jarName = 'the-deep-night-ui-forge-1.20.1-1.1.1.jar'
$jar = Join-Path $root "build\libs\$jarName"
if (!(Test-Path -LiteralPath $jar)) { throw 'Build the release JAR first with gradlew.bat build.' }
$previews = Join-Path $release 'previews-1.1.1'
New-Item -ItemType Directory -Force -Path $release,$previews | Out-Null
Copy-Item -LiteralPath $jar -Destination $release
Copy-Item -LiteralPath (Join-Path $root 'README.txt'),(Join-Path $root 'LICENSE.txt'),(Join-Path $root 'COMPATIBILITY.txt') -Destination $release
Copy-Item -LiteralPath (Join-Path $root 'verification\VERIFICATION.txt') -Destination $release
Copy-Item -LiteralPath (Join-Path $root 'src\main\resources\assets\nordui\font\ofl.txt') -Destination (Join-Path $release 'Jost-OFL.txt')
foreach($image in '01-title.png','12-hud-compact.png','14-spells.png','15-spells-compact.png','16-magic-hud.png','16-magic-hud-rpg.png','17-compatibility.png','18-inventory-jei.png'){
    Copy-Item -LiteralPath (Join-Path $root "verification\$image") -Destination $previews
}
$hash = (Get-FileHash -LiteralPath (Join-Path $release $jarName) -Algorithm SHA256).Hash.ToLower()
"$hash  $jarName" | Set-Content -LiteralPath (Join-Path $release 'SHA256.txt')
$bundle = Join-Path $release 'The-Deep-Night-UI-1.1.1.zip'
Compress-Archive -Path (Join-Path $release $jarName),(Join-Path $release 'README.txt'),(Join-Path $release 'COMPATIBILITY.txt'),(Join-Path $release 'VERIFICATION.txt'),(Join-Path $release 'LICENSE.txt'),(Join-Path $release 'Jost-OFL.txt'),(Join-Path $release 'SHA256.txt'),$previews -DestinationPath $bundle -Force
$source = @('src','gradle','tools','artwork','verification','build.gradle','settings.gradle','gradle.properties','gradlew','gradlew.bat','README.txt','COMPATIBILITY.txt','LICENSE.txt') | ForEach-Object { Join-Path $root $_ }
Add-Type -AssemblyName System.IO.Compression.FileSystem
$stream = [IO.File]::Create((Join-Path $release 'The-Deep-Night-UI-Source-1.1.1.zip'))
$archive = [IO.Compression.ZipArchive]::new($stream,[IO.Compression.ZipArchiveMode]::Create)
try {
    foreach ($path in $source) {
        foreach ($file in (Get-ChildItem -LiteralPath $path -Recurse -File)) {
            $relative = [IO.Path]::GetRelativePath($root,$file.FullName).Replace('\','/')
            if ($relative.StartsWith('verification/research/') -or $relative.EndsWith('.log') -or $relative.EndsWith('CLIENT-FAILED.txt')) { continue }
            [IO.Compression.ZipFileExtensions]::CreateEntryFromFile($archive,$file.FullName,$relative,[IO.Compression.CompressionLevel]::Optimal) | Out-Null
        }
    }
} finally { $archive.Dispose(); $stream.Dispose() }
Get-ChildItem -LiteralPath $release | Select-Object Name,Length
