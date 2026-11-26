"""Preserve unfinished combat ports outside the build, then clean removed bosses."""
from pathlib import Path
import json, re, shutil, zipfile

R=Path(__file__).resolve().parents[1]
draft=R/'work-in-progress/source-combat'
draft.mkdir(parents=True,exist_ok=True)
def move(relative):
    src=(R/relative).resolve();dst=draft/relative
    assert src.is_relative_to(R.resolve()) and dst.resolve().is_relative_to(R.resolve())
    if src.exists():
        assert not dst.exists(),dst
        dst.parent.mkdir(parents=True,exist_ok=True);shutil.move(str(src),str(dst))

for folder in ['block','entity','init','network','potion','procedures','client/model','client/renderer']:
    move('src/main/java/dev/deepnight/source/aod/'+folder)
move('src/main/java/dev/deepnight/source/mobosses')
move('src/main/resources/assets/mobosses')
for name in ['NightMob.java','DeepNight.java']:
    p=R/'src/main/java/dev/deepnight'/name
    shutil.copy2(p,draft/name)
    with zipfile.ZipFile(R/'dist/deep-night-forge-1.20.1-1.1.1-source.zip') as z:
        p.write_bytes(z.read('src/main/java/dev/deepnight/'+name))

for name in ['port_aod_combat.py','port_mobosses.py','port_dawncraft.py']:
    p=R/'tools'/name
    text=p.read_text()
    text=text.replace("R/'src/main/", "R/'work-in-progress/source-combat/src/main/")
    text=text.replace("ROOT/'src/main/", "ROOT/'work-in-progress/source-combat/src/main/")
    p.write_text(text)

(draft/'README.txt').write_text('Unfinished original combat ports, excluded from release 1.1.2.\nAoD port currently fails compilation; MoBosses needs lifecycle, attribute, renderer and summoner integration.\nDo not copy into src/main or describe these as released behavior.\n')

from boss_roster import clean_resources
clean_resources(R)
