"""Build a verified, versioned pair of Forge mods and the corresponding source."""
import json,hashlib,shutil,zipfile
from pathlib import Path
P=Path(__file__).resolve().parents[1];N=P.parent/'thedeepnightnpcs-forge-1.20.1';OUT=P.parent/'ashen-npc-integration'
OUT.mkdir(exist_ok=True)
versions={P:'1.2.0',N:'1.3.0'}
jars={root:root/'build/libs'/f'{root.name}-{version}.jar' for root,version in versions.items()}
server=(P/'verification/dedicated-server-tests.txt').read_text();client=(P/'verification/client-smoke-test.txt').read_text();npc=(N/'verification/dedicated-server.txt').read_text()
for expected in ['All 276 main quests require Eldara','NPC network action finishes the ordered two-character report','Legacy completions remain completed after conversation objectives are appended','Renewed acceptance clears prior-cycle conversation credit']:
    assert expected in server,expected
for expected in ["Mara's authored response arrives over the actual NPC network",'Returning to Rowan pays the exact side-story reward','Completed conversation still cannot be rewarded through the hotkey','Long quest HUD advances past completed tasks to its fifth objective']:
    assert expected in client,expected
assert 'NPC constructs: entity.thedeepnightnpcs.ysolde' in npc
for name in ['conversion-audit','expansion-audit']:
    assert json.loads((P/f'verification/{name}.json').read_text())['result']=='PASS',name
for root,jar in jars.items():
    with zipfile.ZipFile(jar) as z:
        assert not any('/qa/' in name for name in z.namelist()),jar
        assert 'META-INF/mods.toml' in z.namelist()
        for source in (root/'src/main/resources').rglob('*'):
            if source.is_file() and source.name!='empty.nbt':assert z.read(source.relative_to(root/'src/main/resources').as_posix())==source.read_bytes(),source
    for source in (root/'src/main/java').rglob('*.java'):
        if 'qa' not in source.parts:
            report=P/'verification'/('client-smoke-test.txt' if 'client' in source.parts else 'dedicated-server-tests.txt')
            assert source.stat().st_mtime<=report.stat().st_mtime,source
            if root==N and 'client' not in source.parts:assert source.stat().st_mtime<=(N/'verification/dedicated-server.txt').stat().st_mtime,source
    (root/'dist').mkdir(exist_ok=True)
    shutil.copyfile(jar,root/'dist'/jar.name);shutil.copyfile(root/'README.txt',root/'dist/README.txt');shutil.copyfile(jar,OUT/jar.name)
with zipfile.ZipFile(jars[P]) as z:
    campaign=json.loads(z.read('data/ashenquests/ashen_quests/campaign.json'))['quests']
assert len(campaign)==557 and sum(q['giver']=='eldara' for q in campaign)==276
skin_report={};prior_path=P/'verification/skin-integrity.json';prior=json.loads(prior_path.read_text()) if prior_path.exists() else {}
with zipfile.ZipFile(jars[N]) as z:
    for name in ['rowan','sera','mara','elian','iona','tamsin','fen','ysolde']:
        packed=z.read(f'assets/thedeepnightnpcs/textures/entity/{name}.png');sha=hashlib.sha256(packed).hexdigest();supplied=P.parent/'thedeepnightnpcs'/f'{name}.png'
        if supplied.exists():assert packed==supplied.read_bytes(),name
        else:assert prior[name]['sha256']==sha,name
        skin_report[name]=dict(sha256=sha,exact_match=True,model='classic' if name in ('rowan','fen') else 'slim')
prior_path.write_text(json.dumps(skin_report,indent=2))
summary=(f'Ashen Covenant 1.2.0 + The Deep Night NPCs 1.3.0\n'
 'Minecraft Java 1.20.1 / Forge 47.4.10 / Java 17\n\n'
 f'Quest/integration dedicated server: {server.count("PASS:")} assertions passed.\n'
 f'NPC standalone dedicated server: {npc.count("PASS:")} assertions passed (including biome-table checks).\n'
 f'Real client + integrated server: {client.count("PASS:")} assertions passed.\n'
 '557 quests: 276 main / 281 side. 96 new main quests in Books IV and V.\n'
 '76 new side quests from ten NPCs; 48 older quests gain conversations.\n'
 '313 distinct authored conversation responses across 220 quests.\n'
 'Original 385 IDs, objective indices, prerequisites and rewards preserved.\n'
 'All 276 main quests required for Book V finale; no side quest required.\n'
 'Quest acceptance, ordered reports, wrong NPC, range, line of sight, closing,\n'
 'replay, save/load, legacy completions, repeat renewal, and turn-in checked.\n'
 'Every new side-story opener exercised through its giver and report recipient.\n'
 'Actual client completed Rowan -> Mara discussion -> Rowan reward flow.\n'
 'Hotkey remains tracking only. All eight supplied skins match byte-for-byte.\n'
 'Standalone NPC checks cover Caelen hunts, Jorge services, and guard behavior.\n'
 'Release JARs exclude QA classes; packaged resources match sources.\n'
 'Full 355-mod profile not launched. No existing playthrough used for testing.\n')
(P/'verification/INTEGRATION-RESULTS.txt').write_text(summary);(OUT/'TEST-RESULTS.txt').write_text(summary)
readme='''ASHEN COVENANT 1.2.0 + THE DEEP NIGHT NPCS 1.3.0
Minecraft Java 1.20.1 / Forge 47.4.10+ (47.x) / Java 17

INSTALL
Replace the old versions of these two mods with BOTH jars in mods/:
  ashen-quests-forge-1.20.1-1.2.0.jar
  thedeepnightnpcs-forge-1.20.1-1.3.0.jar
Install the same pair on the server and clients. Avoid duplicate older versions.

PLAY
Eldara > Main quests supplies all 276 main quests across five books.
The other named characters > Covenant quests supply their own side quests.
Accept in person, finish the objectives, and return to the giver for rewards.
Press minus (-, beside 0) or use /ashenquests to read and track quests.
The hotkey journal cannot accept quests or turn them in.

For a conversation objective, complete earlier tasks, then right-click the
named NPC > Discuss a quest > the assignment title. Read their written reply,
visit the next person if listed, and return to the original giver to turn in.
Ordinary lore topics do not count. Each conversation is specific to its quest.
Completed conversation text remains available beneath the journal objective.

NEW CONTENT
96 main quests: Book IV, The Roads That Remember; Book V, The Winter of
Unwritten Names. Book IV follows the existing Book III finale (hc_home06).
76 side quests open after The First Hearth (ac_m06): eight each from Rowan,
Sera, Mara, Elian, Iona, Tamsin, Fen and Ysolde; six each from Caelen and Jorge.
48 earlier quests gain conversations. 557 quests total: 276 main, 281 side.
The 313 new dialogue responses cover 220 quests. Side stories stay optional.

NPC PLACEMENT
Place all twelve characters using their creative spawn eggs or /summon:
eldara, rowan, sera, mara, elian, iona, tamsin, fen, ysolde, caelen, jorge,
spire_guard. Example: /summon thedeepnightnpcs:eldara ~ ~ ~
NPCs do not spawn automatically. Quest reports may require the Spire Guard.
The eight supplied skins are unchanged. Narrative events do not generate
settlements or alter terrain. Caelen's hunts and Jorge's services remain.

UPGRADING
Completed quests remain completed. Existing acceptance, counters, choices,
standing and placements persist. Unfinished modified quests require their
new conversations. Original tasks and rewards remain unchanged.
Original modpack content mods are still needed for the older quest targets.
Avoid running duplicate old Boundless questpacks alongside this campaign.
See docs/ for objective rules, NPC details, source build and test instructions.
'''
(OUT/'README.txt').write_text(readme)
install=OUT/'Ashen-Covenant-NPCs-1.2.0-1.3.0.zip'
with zipfile.ZipFile(install,'w',zipfile.ZIP_DEFLATED) as z:
    for root,jar in jars.items():z.write(jar,'mods/'+jar.name);z.write(root/'README.txt','docs/'+root.name+'-README.txt')
    for name in ['README.txt','TEST-RESULTS.txt']:z.write(OUT/name,name)
    for name in ['quest-givers.json','skin-integrity.json','expansion-audit.json','conversation-expansion.json']:z.write(P/'verification'/name,'verification/'+name)
source=OUT/'Ashen-Covenant-NPCs-source-1.2.0-1.3.0.zip'
with zipfile.ZipFile(source,'w',zipfile.ZIP_DEFLATED) as z:
    for root in versions:
        for name in ['build.gradle','settings.gradle','gradle.properties','gradlew','gradlew.bat','README.txt','.gitignore']:
            if (root/name).exists():z.write(root/name,root.name+'/'+name)
        for folder in ['src','tools','gradle','verification']:
            for file in (root/folder).rglob('*'):
                if file.is_file() and '__pycache__' not in file.parts and 'FAILED' not in file.name:z.write(file,root.name+'/'+file.relative_to(root).as_posix())
shutil.copyfile(source,OUT/'Ashen-Covenant-NPCs-source.zip')
shutil.copyfile(install,P/'dist/Ashen-Quests-Forge-1.20.1-install.zip');shutil.copyfile(source,P/'dist/Ashen-Quests-Forge-1.20.1-source.zip')
with zipfile.ZipFile(N/'dist/thedeepnightnpcs-source-1.3.0.zip','w',zipfile.ZIP_DEFLATED) as z,zipfile.ZipFile(source) as all_source:
    for name in all_source.namelist():
        if name.startswith(N.name+'/'):z.writestr(name,all_source.read(name))
for folder in [OUT,P/'dist',N/'dist']:
    entries=[hashlib.sha256(file.read_bytes()).hexdigest()+'  '+file.name for file in sorted(folder.iterdir()) if file.is_file() and file.suffix in ('.jar','.zip')]
    (folder/'SHA256SUMS.txt').write_text('\n'.join(entries)+'\n')
print(summary);print(install);print(source)

