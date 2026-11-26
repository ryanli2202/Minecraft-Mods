$ErrorActionPreference = 'Stop'
$taskRoot = Split-Path -Parent $PSScriptRoot
$assetRoot = Join-Path $taskRoot 'src/main/resources/assets/thedeepnightnpcs'
New-Item -ItemType Directory -Force "$assetRoot/dialogue", "$assetRoot/lang", "$assetRoot/models/item" | Out-Null
function Build-Tree($file, $title, $greeting) {
    $tree = [ordered]@{ root = [ordered]@{title=$title; text=$greeting.Replace('~',"`n`n"); children=[System.Collections.Generic.List[string]]::new()} }
    $chapter = ''
    $chapterIndex = 0
    $topicIndex = 0
    foreach ($line in Get-Content -LiteralPath (Join-Path $PSScriptRoot $file)) {
        if ([string]::IsNullOrWhiteSpace($line) -or $line.StartsWith('#')) { continue }
        $parts = $line.Split('|', 3)
        if ($parts[0] -eq '@') {
            $chapterIndex++; $topicIndex=0; $chapter = "chapter_$chapterIndex"
            $tree.root.children.Add($chapter)
            $tree[$chapter] = [ordered]@{title=$parts[1];text=$parts[2].Replace('~',"`n`n");children=[System.Collections.Generic.List[string]]::new()}
        } else {
            $topicIndex++; $id="${chapter}_$topicIndex"
            $tree[$chapter].children.Add($id)
            $tree[$id]=[ordered]@{title=$parts[1];text=$parts[2].Replace('~',"`n`n");children=@()}
        }
    }
    return $tree
}
$eldara = Build-Tree 'eldara-dialogue.txt' 'Where shall we begin?' 'Come closer, traveller. I am Eldara, called the Eternal Sage by people who mistake surviving a century for understanding it. I keep the histories of the Deep Night, and I help those who must live through its next chapter.~Ask about your first steps, the Spire, the fallen covenant, the worlds beyond this one, or something that weighs upon you. There is no examination here. We can return to any subject as often as you wish.~Choose a spoke of the wheel. More subjects wait on the next pages.'
$eldara | ConvertTo-Json -Depth 20 | Set-Content -Encoding utf8 "$assetRoot/dialogue/eldara.json"
$caelen = Build-Tree 'caelen-dialogue.txt' 'A hunter returns' 'You have the look of someone deciding whether a road is worth the danger. I am Caelen. I keep the hunting board and pay for completed work.~Read a report before accepting it. One contract at a time, no deadline, and your own credited kills after acceptance. Return here to claim payment. If a target is beyond your reach, abandon the contract and choose another.'
$caelen | ConvertTo-Json -Depth 20 | Set-Content -Encoding utf8 "$assetRoot/dialogue/caelen.json"
$guard = Build-Tree 'guard-dialogue.txt' 'Hold a moment, traveller' 'Easy there. You are welcome beneath the Spire so long as you keep your blade sheathed around our guests. I keep watch here; Eldara keeps the stories, and Caelen keeps the hunting ledger.~What do you need?'
$jorge = Build-Tree 'jorge-dialogue.txt' 'Welcome to the counter' 'Jorge, armskeeper. I keep the metal moving, the shelves stocked, and the tools ready for the next road. Emeralds buy ingots or inscribed books; your experience and lapis pay for work at the enchanting bench.~Browse before you commit. My stock changes when the next shipment arrives, and I can put fresh offers on either counter when you ask. What are you working on?'
# Guards and Caelen have short, direct wheels; flatten their sole chapter.
foreach ($pair in @(@('caelen',$caelen),@('spire_guard',$guard),@('jorge',$jorge))) {
    $tree=$pair[1]
    if ($tree.root.children.Count -eq 1) { $parent=$tree.root.children[0]; $tree.root.children=$tree[$parent].children; $tree.Remove($parent) }
    $tree | ConvertTo-Json -Depth 20 | Set-Content -Encoding utf8 "$assetRoot/dialogue/$($pair[0]).json"
}
$lang=[ordered]@{'itemGroup.thedeepnightnpcs'='The Deep Night NPCs';'entity.thedeepnightnpcs.eldara'='Eldara the Eternal Sage';'entity.thedeepnightnpcs.caelen'='Caelen the Monster Hunter';'entity.thedeepnightnpcs.spire_guard'='Spire Guard';'entity.thedeepnightnpcs.jorge'='Jorge the Armskeeper'}
foreach ($npc in @('eldara','caelen','spire_guard','jorge')) {
    $lang["item.thedeepnightnpcs.${npc}_spawn_egg"] = "$($lang["entity.thedeepnightnpcs.$npc"]) Spawn Egg"
    '{"parent":"minecraft:item/template_spawn_egg"}' | Set-Content -Encoding utf8 "$assetRoot/models/item/${npc}_spawn_egg.json"
}
$lang | ConvertTo-Json | Set-Content -Encoding utf8 "$assetRoot/lang/en_us.json"
$words=(($eldara.Values | ForEach-Object {$_.text}) -join ' ' -split '\s+').Count
Write-Output "Eldara: $($eldara.Count) dialogue nodes; $words words."
