"""Package the tested Spire eye progression without reusing historical QA claims."""
import hashlib
import json
import shutil
import zipfile
from pathlib import Path
from eye_progression import STAGES

P = Path(__file__).resolve().parents[1]
N = P.parent/'thedeepnightnpcs-forge-1.20.1'
OUT = P.parent/'ashen-npc-integration/endrem-progression-1.3.0'
OUT.mkdir(parents=True, exist_ok=True)
versions = {P:'1.3.0', N:'1.4.0'}
report = (P/'verification/dedicated-server-tests.txt').read_text()
log = (P/'verification/eye-build-server.log').read_text()
assert 'All 1 required tests passed' in log
assert 'Spire Guard conversation completes portal preparation through Eldara' in report
assert report.count('NPC turn-in grants exactly one real End Remastered eye:') == 16
assert json.loads((P/'verification/eye-progression-audit.json').read_text())['result'] == 'PASS'
for root, version in versions.items():
    jar = root/'build/libs'/f'{root.name}-{version}.jar'
    with zipfile.ZipFile(jar) as z:
        assert not any('/qa/' in name for name in z.namelist())
        for f in (root/'src/main/resources').rglob('*'):
            if f.is_file() and f.name != 'empty.nbt':
                assert z.read(f.relative_to(root/'src/main/resources').as_posix()) == f.read_bytes(), f
    for destination in [OUT, root/'dist']:
        destination.mkdir(exist_ok=True)
        shutil.copyfile(jar, destination/jar.name)

readme = '''END REMASTERED EYES / THE SPIRE PORTAL
Ashen Covenant quests 1.3.0 + The Deep Night NPCs 1.4.0
Minecraft 1.20.1 / Forge 47.4.10 / Java 17

Replace the older versions of BOTH mods with the two included jars, on the
server and every client. Keep End Remastered and your other campaign content
mods installed. These files have not been installed into your live profile.

An End portal frame already exists at the Spire. There is no stronghold search.
Start with Sera after Eldara's The First Hearth. Choose Covenant quests, accept
the next Lost Eyes NPC Trial, complete its task, choose Discuss a quest, then
turn it in to that NPC for one eye. Each trial requires the previous trial and
the main-story milestone below. The first twelve unlock The Portal at the Spire.
Speak to the Spire Guard for that quest, return to Eldara for final preparations,
then use the existing Spire portal frame in A Sky without Dawn.

The four final eyes are optional. Existing discovery records and completed
quests are preserved. New trial IDs let existing players earn the new rewards.
Quest eyes are never consumed by quest objectives. Original End Remastered
sources remain available for replacements. Keep your eyes for the Spire frame.
The update does not place or alter any portal blocks in your world.

TRIAL ORDER / GIVER / MAIN QUEST GATE
'''
for i,(eye,giver,gate,*_) in enumerate(STAGES,1):
    readme += f'{i:02}. {eye.title()} Eye / {giver.title()} / ac_m{gate}' + (' (optional)' if i>12 else '') + '\n'
readme += f'''
VERIFICATION
Dedicated Forge server with End Remastered 5.3.3: {report.count('PASS:')} assertions passed.
All sixteen actual eye rewards, ordered unlocks, duplicate-claim rejection,
missing-reward protection, legacy discovery saves, and Spire Guard turn-in tested.
573 quest definitions, dependency graph, dialogue links and packaged resources checked.
No new graphical client playthrough was performed for this release.

SOURCE
tools/eye_progression.py defines the trials and is called by build_campaign.py.
Build with gradlew.bat build :npcs:build. For the compatibility test, place
End Remastered 5.3.3 in compat-mods/endrem-forge-5.3.3.jar and run
gradlew.bat runGameTestServer -PtestEndRemastered.
'''
(OUT/'README.txt').write_text(readme,encoding='utf-8')
install = OUT/'Deep-Night-Eye-Progression-1.3.0.zip'
with zipfile.ZipFile(install,'w',zipfile.ZIP_DEFLATED) as z:
    z.write(OUT/'README.txt','README.txt')
    for jar in OUT.glob('*.jar'): z.write(jar,'mods/'+jar.name)
    for name in ['dedicated-server-tests.txt','eye-progression-audit.json','eye-build-server.log']:
        z.write(P/'verification'/name,'verification/'+name)
source = OUT/'Deep-Night-Eye-Progression-source-1.3.0.zip'
with zipfile.ZipFile(source,'w',zipfile.ZIP_DEFLATED) as z:
    for root in versions:
        for name in ['build.gradle','settings.gradle','gradle.properties','gradlew','gradlew.bat','README.txt','.gitignore']:
            z.write(root/name,root.name+'/'+name)
        for folder in ['src','tools','gradle']:
            for f in (root/folder).rglob('*'):
                if f.is_file() and '__pycache__' not in f.parts:
                    z.write(f,root.name+'/'+f.relative_to(root).as_posix())
    for name in ['dedicated-server-tests.txt','eye-progression-audit.json','eye-build-server.log']:
        z.write(P/'verification'/name,P.name+'/verification/'+name)
(OUT/'SHA256SUMS.txt').write_text('\n'.join(hashlib.sha256(f.read_bytes()).hexdigest()+'  '+f.name for f in sorted(OUT.iterdir()) if f.suffix in ('.jar','.zip'))+'\n')
print(install)
print(source)
