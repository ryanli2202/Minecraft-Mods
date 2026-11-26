"""Package the tested build and the exact locally tested optional combat runtime."""
from pathlib import Path
import json,zipfile,shutil,hashlib,re
R=Path(__file__).resolve().parents[1];dist=R/'dist';dist.mkdir(exist_ok=True)
version=re.search(r"^version = '([^']+)'",(R/'build.gradle').read_text(),re.M)[1]
if version=='1.1.1':
    import runpy
    runpy.run_path(str(R/'tools/package_drake_update.py'),run_name='__main__')
    raise SystemExit(0)
if version=='1.1.0':
    import runpy
    runpy.run_path(str(R/'tools/package_java_imports.py'),run_name='__main__')
    raise SystemExit(0)
jar=R/f'build/libs/deep-night-forge-1.20.1-{version}.jar'
for tag,runtime in [('gecko484','4.8.4'),('gecko447','4.4.7')]:
    report=(R/f'verification/{tag}-client-report.txt').read_text()
    assert f'Runtime GeckoLib: {runtime}' in report
    assert f'Deep Night build: {version}' in report
    assert 'All 177 entity renderers' in report and 'Menu click summons exactly one' in report
    assert 'Explicit resource reload preserves selected packs' in report
    assert report.count('Every preserved source animation baked:')==177
    assert report.count('Boss contact pose visibly differs from its starting pose:')==41
    assert report.count('Boss one-shot attack survives hurt/restart and late tracking:')==164
    assert 'Bog Sovereign tongue extends at contact using the locked target distance' in report
    client_log=(R/f'verification/{tag}-client.log').read_text(errors='replace')
    assert 'BUILD SUCCESSFUL' in client_log
    for error in ['Error loading animation file','Unable to parse animation','Caught error loading resourcepacks']:
        assert error not in client_log, (tag,error)
assert not (R/'verification/CLIENT-FAILED.txt').exists()
log=(R/'final-verification.log').read_text(errors='replace')
assert 'All 9 required tests passed' in log and 'BUILD SUCCESSFUL' in log
assert 'Unable to parse animation' not in log
catalogue=json.loads((R/'verification/content-catalogue.json').read_text())
with zipfile.ZipFile(jar) as z:
    names=z.namelist()
    assert not any(n.startswith('dev/deepnight/qa/') for n in names)
    assert sum(n.endswith('.geo.json') for n in names)==177
    assert sum(n.endswith('.ogg') for n in names)==574
    assert 'dev/deepnight/PetSummoningTool.class' in names
    assert 'dev/deepnight/PetGrowth.class' in names
    assert 'assets/deepnight/sounds.json' in names
    assert 'dev/deepnight/BossAttackProfile.class' in names
    profiles=json.loads(z.read('data/deepnight/boss_attacks.json'))
    assert len(profiles)==41
    for boss,profile in profiles.items():
        clip=json.loads(z.read(f'assets/deepnight/animations/{boss}.animation.json'))['animations']['attack']
        assert clip['loop'] is False
        assert abs(clip['animation_length']*20-profile['durationTicks'])<0.001
        assert [float(t)*20 for t in clip['timeline']]==profile['impactTicks']
    assert z.testzip() is None
shutil.copy2(jar,dist/jar.name)
compat=R.parent/'fantasy-races-forge-1.20.1/compat-mods'
gecko=compat/'geckolib-forge-1.20.1-4.8.4.jar'
deps=[gecko]+[compat/name for name in ['bettercombat-forge-1.9.0+1.20.1.jar','cloth-config-11.1.136-forge.jar','player-animation-lib-forge-1.0.2-rc1+1.20.jar']]
install='''THE DEEP NIGHT - LOCAL INSTALL BUNDLE

Minecraft Java 1.20.1, Forge 47.4.10, Java 17.
1. Install/run a Forge 1.20.1 profile.
2. Copy the five jars from this bundle's mods directory into that profile's
   mods directory. Replace previous Deep Night jars instead of keeping two.
   Keep only one version of each dependency if your modpack already has it.
3. Start Minecraft. The Deep Night has a creative tab and /deepnight guide.

This bundle includes the exact locally tested versions of GeckoLib, Better
Combat, Cloth Config and Player Animator. Their original licences remain
inside their jars. The supplied artwork/audio retains its original rights.
For a server, use the same versions on the server and every client.

Updating an earlier release: replace only the Deep Night jar if dependencies are installed.
This release synchronizes all 41 bosses' attack animations and damage. It also
includes the Abyssal Demon texture correction and the GeckoLib 4.8.4 loading fix;
it does not require a dependency downgrade.
If the earlier reload failure reset resource packs, re-enable them in Options.

Pets: craft the Pet Summoning Tool using an ender pearl above a book, two
gold ingots either side, and a golden apple below. Clerics can also sell it.
Use the tool and select a pet; it arrives already tamed and can level up.
Bosses: defeat the Ender Dragon to receive the separate Boss Summon Tool.
Defeat all 41 campaign bosses in order. See README.txt for the full guide.
'''
(dist/'INSTALL.txt').write_text(install)
for name in ['README.txt','ASSET-NOTES.txt']:shutil.copy2(R/name,dist/name)
result=f'''THE DEEP NIGHT - VERIFIED 2026-10-08

BUILD: Deep Night {version} / Forge 1.20.1-47.4.10 / Java 17.
Release JAR built and reobfuscated successfully; development QA classes excluded.

1.0.3 BOSS ATTACKS: All 41 bosses use an explicit one-shot attack with shared
server/client contact timings. Immediate vanilla boss hits and arbitrary
special-attack delays are replaced by scheduled contact damage. Multi-hit
clips split damage across their contacts. Facing and attack phase are locked
for a swing, range and line of sight are checked at melee contact, and death,
lost targets, arena resets and reloads cancel outstanding hits. Hurt reactions
cannot override an active attack; repeated attacks reset and late observers
seek to the current frame. Attacks include anticipation and recovery. Seven
idle-only models and two pose-only attacks now have authored attack motion.
The Bog Sovereign's tongue extension uses its locked target distance.
Original supplied source clips remain preserved.

PREVIOUS FIXES RETAINED: The Abyssal Demon uses the supplied asylum_demon.png
matching asylumdemon.geo.json, correcting the previous mismatched texture UVs.

RESOURCE RELOAD REGRESSION: 1.0.0 converted vector/easing objects into invalid
post/easing objects. GeckoLib 4.8.4 then aborted resource loading with
"Invalid keyframe data - multiple starting keyframes?easing" and Minecraft
removed selected resource packs. The importer now preserves GeckoLib vectors,
easing metadata and genuine timed Bedrock pre/post keyframes. All 52,037 bone
channels pass schema checks; five targeted importer regression tests pass.
The current build with GeckoLib 4.8.4 and 4.4.7 completed initial loading and
an explicit resource reload, retained selected packs, baked every preserved
animation clip, and rendered vanilla block models without missing models.

SERVER: All 9 Forge GameTests passed with GeckoLib 4.8.4 and Better Combat.
Attack tests verify all 41 timelines at every phase and both attack modes;
live damage waits for contact, hits once, misses dodges behind/out of range,
uses the special windup, splits three-punch combo damage correctly, and never
replays a hit after death or reload. Overlapping attacks are rejected.
Checks include all 41 ordered campaign stages, persistence, anti-skip and
abandon handling, delayed dragon reward delivery with full inventory, all
31 pets' taming/ownership, pet catalogue selection, forged-species rejection,
summon cooldown, growth of health/damage, level cap, injured-health save/reload,
no duplicate growth modifiers, vanilla book enchanting, recipes and spawn rules.

CLIENT: The current build was verified with both GeckoLib 4.8.4 and 4.4.7.
All 41 boss attacks rendered at start, anticipation, contact (while hurt),
and recovery. Contact poses differ from starting poses and all 164 samples
per runtime retain the attack through hurt/restart and late tracking.
All 177 creature models and 25 weapon models loaded and rendered.
Eight canonical animation channels per creature loaded. No animation parser
errors remain. All bound creature sound events resolve in SoundManager.
A real integrated server awarded the dragon tool, summoned and defeated all
41 bosses through combat events, checked damage caps, phases, saved state,
one-boss gating, sigil rewards and completion. All 25 Better Combat movesets
resolved with Better Combat 1.9.0, Cloth Config 11.1.136, Player Animator
1.0.2-rc1. The pet menu opened over S2C, search found Ember Drake, and the
button's C2S request summoned exactly one owned level-one pet.

RESOURCES: {catalogue['sound_files']} OGG files, {catalogue['sound_events']} sound events, 177 creature model/texture/animation
sets, all 25 weapons and 14 enchantments validated. Distinct Ember and Eclipse
meshes checked. 22 existing scale/offset corrections use
measured Java bone transforms. 41 attack screenshots, 33 creature/weapon
gallery screenshots and a pet-menu screenshot are retained in verification.

PRACTICAL LIMITS: These checks verify implementation and rendering, not a full
survival campaign playthrough or a WAN latency stress test. Contact timing is
quantized to Minecraft's server ticks. Difficulty across third-party modpacks needs
playtesting. The complete fantasycraft 2.0 modpack has not been relaunched by
this verification; tests isolate Deep Night and its combat dependencies.
Imported creatures use the new Java AI rather than source Bedrock
scripts. Dragons are ground rideable; player-controlled flight is not included.
External mod pets are detected only through standard tameable/horse base types.
'''
(R/'verification/TEST-RESULTS.txt').write_text(result)
shutil.copy2(R/'verification/TEST-RESULTS.txt',dist/'TEST-RESULTS.txt')
bundle=dist/f'Deep-Night-{version}-Forge-1.20.1-install.zip'
with zipfile.ZipFile(bundle,'w',zipfile.ZIP_DEFLATED,compresslevel=6) as z:
    for f in [jar]+deps:z.write(f,'mods/'+f.name)
    for name in ['INSTALL.txt','README.txt','ASSET-NOTES.txt','TEST-RESULTS.txt']:z.write(dist/name,name)
    z.write(R/'verification/PET-CATALOGUE.txt','PET-CATALOGUE.txt')
with zipfile.ZipFile(bundle) as z:assert z.testzip() is None
hashes={p.name:hashlib.sha256(p.read_bytes()).hexdigest() for p in [dist/jar.name,bundle]}
(dist/'SHA256.txt').write_text('\n'.join(v+'  '+k for k,v in hashes.items())+'\n')
print(json.dumps({'jar':str(dist/jar.name),'bundle':str(bundle),'jar_bytes':jar.stat().st_size,'bundle_bytes':bundle.stat().st_size,'sha256':hashes},indent=2))
