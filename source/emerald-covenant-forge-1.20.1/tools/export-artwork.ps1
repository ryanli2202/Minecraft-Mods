param([string]$ProjectRoot=(Split-Path -Parent $PSScriptRoot))
$ErrorActionPreference='Stop'
$ProjectRoot=(Resolve-Path -LiteralPath $ProjectRoot).Path
Add-Type -AssemblyName System.Drawing
# Mechanical game-texture export of generated art. Nearest-neighbor preserves pixel edges and alpha.
function Export-Texture([string]$InputFile,[string]$OutputFile,[int]$Width,[int]$Height) {
    $source=[Drawing.Image]::FromFile((Join-Path $ProjectRoot $InputFile))
    $target=[Drawing.Bitmap]::new($Width,$Height,[Drawing.Imaging.PixelFormat]::Format32bppArgb)
    $graphics=[Drawing.Graphics]::FromImage($target)
    try {
        $graphics.CompositingMode=[Drawing.Drawing2D.CompositingMode]::SourceCopy
        $graphics.InterpolationMode=[Drawing.Drawing2D.InterpolationMode]::NearestNeighbor
        $graphics.PixelOffsetMode=[Drawing.Drawing2D.PixelOffsetMode]::Half
        $graphics.DrawImage($source,[Drawing.Rectangle]::new(0,0,$Width,$Height),0,0,$source.Width,$source.Height,[Drawing.GraphicsUnit]::Pixel)
        $target.Save((Join-Path $ProjectRoot $OutputFile),[Drawing.Imaging.ImageFormat]::Png)
    } finally {$graphics.Dispose();$target.Dispose();$source.Dispose()}
}
Export-Texture 'artwork/pixel-frame.png' 'src/main/resources/assets/emeraldcovenant/textures/gui/shop_frame.png' 384 256
Export-Texture 'artwork/pixel-merchants.png' 'src/main/resources/assets/emeraldcovenant/textures/gui/merchants.png' 128 128
Export-Texture 'artwork/bell-bearing.png' 'src/main/resources/assets/emeraldcovenant/textures/item/bell_bearing.png' 32 32
