$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Drawing
$destination = Join-Path $PSScriptRoot '..\src\main\resources\assets\nordui\textures\gui'
function Points($values) { ,([System.Drawing.Point[]]($values | ForEach-Object { [System.Drawing.Point]::new($_[0], $_[1]) })) }
$bitmap = [System.Drawing.Bitmap]::new(256,24)
$graphics = [System.Drawing.Graphics]::FromImage($bitmap)
$graphics.Clear([System.Drawing.Color]::Transparent)
$outline = Points @(@(2,12),@(13,4),@(24,7),@(232,7),@(243,4),@(254,12),@(243,20),@(232,17),@(24,17),@(13,20))
$metal = [System.Drawing.Drawing2D.LinearGradientBrush]::new([System.Drawing.Point]::new(0,3),[System.Drawing.Point]::new(0,21),[System.Drawing.Color]::FromArgb(240,224,224,213),[System.Drawing.Color]::FromArgb(230,75,83,86))
$graphics.FillPolygon($metal,$outline)
$graphics.DrawPolygon([System.Drawing.Pens]::Silver,$outline)
$inner = Points @(@(8,12),@(14,7),@(23,9),@(233,9),@(242,7),@(248,12),@(242,17),@(233,15),@(23,15),@(14,17))
$graphics.FillPolygon([System.Drawing.Brushes]::Black,$inner)
$graphics.CompositingMode = [System.Drawing.Drawing2D.CompositingMode]::SourceCopy
$graphics.FillRectangle([System.Drawing.Brushes]::Transparent,24,9,208,6)
$graphics.CompositingMode = [System.Drawing.Drawing2D.CompositingMode]::SourceOver
$graphics.DrawLine([System.Drawing.Pens]::LightGray,25,8,231,8)
$graphics.DrawLine([System.Drawing.Pens]::DimGray,25,16,231,16)
$graphics.DrawLines([System.Drawing.Pens]::Silver,(Points @(@(6,12),@(13,9),@(19,12),@(13,15),@(6,12))))
$graphics.DrawLines([System.Drawing.Pens]::Silver,(Points @(@(237,12),@(243,9),@(250,12),@(243,15),@(237,12))))
$bitmap.Save((Join-Path $destination 'bar_frame.png'))
$graphics.Dispose(); $bitmap.Dispose(); $metal.Dispose()
foreach ($style in @(
    @{Name='health_fill'; Top=[System.Drawing.Color]::FromArgb(255,206,89,79); Bottom=[System.Drawing.Color]::FromArgb(255,82,12,18)},
    @{Name='stamina_fill'; Top=[System.Drawing.Color]::FromArgb(255,121,162,108); Bottom=[System.Drawing.Color]::FromArgb(255,34,67,43)},
    @{Name='experience_fill'; Top=[System.Drawing.Color]::FromArgb(255,162,188,215); Bottom=[System.Drawing.Color]::FromArgb(255,42,70,99)}
)) {
    $bitmap=[System.Drawing.Bitmap]::new(208,6); $graphics=[System.Drawing.Graphics]::FromImage($bitmap)
    $brush=[System.Drawing.Drawing2D.LinearGradientBrush]::new([System.Drawing.Point]::new(0,0),[System.Drawing.Point]::new(0,6),$style.Top,$style.Bottom)
    $graphics.FillRectangle($brush,0,0,208,6);$graphics.DrawLine([System.Drawing.Pens]::Silver,0,0,207,0)
    $bitmap.Save((Join-Path $destination ($style.Name+'.png')))
    $brush.Dispose();$graphics.Dispose();$bitmap.Dispose()
}
foreach($selected in @($false,$true)) {
    $bitmap=[System.Drawing.Bitmap]::new(32,32);$graphics=[System.Drawing.Graphics]::FromImage($bitmap);$graphics.Clear([System.Drawing.Color]::Transparent)
    $path=Points @(@(7,2),@(24,2),@(29,7),@(29,24),@(24,29),@(7,29),@(2,24),@(2,7))
    $well=[System.Drawing.SolidBrush]::new([System.Drawing.Color]::FromArgb(195,8,13,19));$graphics.FillPolygon($well,$path)
    $color=if($selected){[System.Drawing.Color]::FromArgb(255,228,218,182)}else{[System.Drawing.Color]::FromArgb(190,142,157,165)}
    $pen=[System.Drawing.Pen]::new($color);$graphics.DrawPolygon($pen,$path)
    $graphics.DrawLine($pen,9,0,22,0);$graphics.DrawLine($pen,9,31,22,31)
    if($selected){$graphics.DrawRectangle($pen,5,5,21,21);$graphics.DrawLine($pen,0,12,0,19);$graphics.DrawLine($pen,31,12,31,19)}
    $name=if($selected){'quick_selected.png'}else{'quick_slot.png'}
    $bitmap.Save((Join-Path $destination $name));$pen.Dispose();$well.Dispose();$graphics.Dispose();$bitmap.Dispose()
}
Write-Output 'Created ornate bar frame, three gradient fills, and two quick-slot textures.'
