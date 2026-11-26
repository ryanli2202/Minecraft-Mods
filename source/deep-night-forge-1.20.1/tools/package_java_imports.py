"""Package only after current server, resource and both client checks pass."""
from pathlib import Path
import json,zipfile,hashlib,shutil,re
R=Path(__file__).resolve().parents[1];dist=R/'dist';dist.mkdir(exist_ok=True)
version=re.search(r"^version = '([^']+)'",(R/'build.gradle').read_text(),re.M)[1]
assert version=='1.1.0'
def sha(p):return hashlib.sha256(p.read_bytes()).hexdigest()
inputs={p.name:sha(p) for p in [R.parent/'age_of_dragons-1.1.2-neoforge-1.21.1.jar',R.parent/'dawncraft_mobs-1.18-1.3.9a.jar',R.parent/'mobosses-1.0.7.jar']}
(R/'verification/java-import-input-hashes.json').write_text(json.dumps(inputs,indent=2)+'\n')
resources=json.loads((R/'verification/java-import-resource-check.json').read_text())
assert resources['errors']==[] and resources['creatures']==230
assert not (R/'verification/CLIENT-FAILED.txt').exists()
server=(R/'java-import-final.log').read_text(errors='replace')
assert 'All 11 required tests passed' in server and 'BUILD SUCCESSFUL' in server
for tag,runtime in [('gecko447','4.4.7'),('gecko484','4.8.4')]:
    report=(R/f'verification/java-import-{tag}-client.txt').read_text(encoding='utf8')
    assert f'Runtime GeckoLib: {runtime}' in report and 'Deep Night build: 1.1.0' in report
    assert 'All 407 entity renderers' in report and 'Menu click summons exactly one' in report
    assert report.count('Every preserved source animation baked:')==407
    assert report.count('Boss contact pose visibly differs from its starting pose:')==114
    assert report.count('Boss one-shot attack survives hurt/restart and late tracking:')==456
    log=(R/('java-import-final.log' if tag=='gecko447' else 'java-import-gecko484.log')).read_text(errors='replace')
    assert 'BUILD SUCCESSFUL' in log
    for bad in ['Unable to parse animation','Error loading animation file','Reported exception thrown!','Caught error loading resourcepacks']:
        assert bad not in log,(tag,bad)
jar=R/f'build/libs/deep-night-forge-1.20.1-{version}.jar'
with zipfile.ZipFile(jar) as z:
    names=z.namelist()
    assert sum(n.endswith('.geo.json') for n in names)==407
    assert sum(n.endswith('.ogg') for n in names)==793
    assert not any(n.startswith('dev/deepnight/qa/') for n in names)
    assert not any(n.startswith(('net/mcreator/','net/hell/','net/neoforged/')) for n in names)
    assert len(json.loads(z.read('data/deepnight/boss_attacks.json')))==114
    assert 'data/deepnight/molang_variables.json' in names
    assert 'dev/deepnight/client/MolangVariables.class' in names
    assert z.testzip() is None
    for source in (R/'src/main/resources').rglob('*'):
        if not source.is_file():continue
        key=source.relative_to(R/'src/main/resources').as_posix()
        if key=='data/deepnight/structures/empty.nbt':continue
        assert z.read(key)==source.read_bytes(),('Stale resource',key)
shutil.copy2(jar,dist/jar.name)
install='''THE DEEP NIGHT 1.1.0 - FORGE 1.20.1

Requires Minecraft Java 1.20.1, Forge 47.4.10+ and Java 17.
Copy the two JARs in mods/ to your instance's mods folder. Replace the old
Deep Night JAR. Keep only one GeckoLib JAR if your pack already supplies it.
The bundle includes tested GeckoLib 4.8.4; 4.4.7 was also tested.
Install the same versions on server and clients.

The Age of Dragons, DawnCraft Mobs and Mo'Bosses source JARs are not required.
Their creatures are adapted into Deep Night. Do not add their incompatible
1.18.2 or NeoForge 1.21.1 JARs to this Forge 1.20.1 instance.

Existing progress is preserved. New bosses follow the original 41 encounters,
using the same Boss Summon Tool. New pets are in the Pet Summoning Tool menu.
Total: 114 bosses, 58 native pets, 407 creatures.
See IMPORTED-CREATURES.txt for the roster and adaptation limitations.
Optional Better Combat/Player Animator/Cloth Config support is unchanged;
those optional dependencies are not included in this two-JAR bundle.
'''
(dist/'INSTALL-1.1.0.txt').write_text(install,encoding='utf8')
result='''THE DEEP NIGHT 1.1.0 - VERIFICATION

Forge 1.20.1-47.4.10 / Java 17
Clean build and reobfuscation: passed.
11 server GameTests: passed (campaign persistence, legacy stage migration,
active encounter preservation, ownership, taming, cooldown, XP persistence,
boss/pet separation, recipes, natural spawns and authoritative attack timing).
GeckoLib 4.4.7 and 4.8.4 client checks: passed.
407 entity models, all preserved source clips and eight runtime animation
states loaded; explicit resource reload preserved selected resource packs.
114 complete campaign encounters exercised; all 114 contact poses and 456
attack replay/hurt/late-tracking checks passed. All creature renderers ran.
Pet screen/network summon flow passed; final test selects the imported Ignis.
230 imported texture/model/animation/sound resource audits: no errors.
Release archive contains 407 geometry files and 793 OGG files, no development
QA classes and no source-mod NeoForge/1.18.2 binaries. Resource bytes match
the current project. Source inputs were not modified.

19 significant model scale/floor offsets calibrated using actual client
meshes. Sample screenshots inspected; automated checks rendered the full
catalogue. This is not an exhaustive survival balance or multiplayer modpack
playtest. Source scripted combat and full source-mod systems are adapted,
as described in IMPORTED-CREATURES.txt.
'''
(dist/'TEST-RESULTS-1.1.0.txt').write_text(result,encoding='utf8')
for name in ['README.txt','ASSET-NOTES.txt','IMPORTED-CREATURES.txt']:shutil.copy2(R/name,dist/name)
bundle=dist/'Deep-Night-1.1.0-Forge-1.20.1-install.zip'
with zipfile.ZipFile(bundle,'w',zipfile.ZIP_DEFLATED,compresslevel=6) as z:
    z.write(jar,'mods/'+jar.name)
    gecko=R/'compat-mods/geckolib-forge-1.20.1-4.8.4.jar';z.write(gecko,'mods/'+gecko.name)
    for name in ['README.txt','ASSET-NOTES.txt','IMPORTED-CREATURES.txt','INSTALL-1.1.0.txt','TEST-RESULTS-1.1.0.txt']:z.write(dist/name,name)
sourcezip=dist/'deep-night-forge-1.20.1-1.1.0-source.zip'
with zipfile.ZipFile(sourcezip,'w',zipfile.ZIP_DEFLATED,compresslevel=6) as z:
    for folder in ['src','tools','gradle','compat-mods']:
        for p in (R/folder).rglob('*'):
            if p.is_file() and '__pycache__' not in p.parts:z.write(p,p.relative_to(R).as_posix())
    for p in (R/'verification').rglob('*'):
        if p.is_file() and (p.name.startswith('java-import') or 'pre-java-import' in p.parts):z.write(p,p.relative_to(R).as_posix())
    for name in ['build.gradle','settings.gradle','gradle.properties','gradlew','gradlew.bat','README.txt','ASSET-NOTES.txt','IMPORTED-CREATURES.txt']:
        z.write(R/name,name)
    z.write(dist/'TEST-RESULTS-1.1.0.txt','TEST-RESULTS-1.1.0.txt')
artifacts=[dist/jar.name,bundle,sourcezip]
(dist/'SHA256-1.1.0.txt').write_text(''.join(sha(p)+'  '+p.name+'\n' for p in artifacts))
print('\n'.join(str(p) for p in artifacts))
