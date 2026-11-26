"""Package only after every boss lifecycle, server regression and client check passes."""
from pathlib import Path
import hashlib
import json
import re
import shutil
import zipfile

root = Path(__file__).resolve().parents[1]
version = re.search(r"version = '([^']+)'", (root / 'build.gradle').read_text()).group(1)
bosses = re.findall(r'^    (\w+)\("[^\n]+Kind.BOSS', (root / 'src/main/java/dev/deepnight/Species.java').read_text(), re.M)
assert len(bosses) == 22 and sum(b.startswith('DAWN_') for b in bosses) == 15
audit = json.loads((root / 'verification/boss-lifecycle-report.json').read_text())
assert set(audit) == {b.lower() for b in bosses}
assert all(row['status'] == 'PASS' and row['boss_damage_hits'] > 0 and row['player_damage_hits'] > 0 for row in audit.values())
server = (root / 'boss-lifecycle-tests.log').read_text(errors='replace')
match = re.search(r'All (\d+) required tests passed', server)
assert match and 'BUILD SUCCESSFUL' in server
server_errors = [line for line in server.splitlines() if '/ERROR]' in line]
# Forge creates this file after logging the recoverable first-run configuration read.
assert all('Failed to load properties from file: server.properties' in line for line in server_errors), server_errors
client = (root / 'verification/boss-menu-client-report.txt').read_text()
gallery = (root / 'verification/source-client-report.txt').read_text()
assert client.startswith('PASS\n') and gallery.startswith('PASS\n')
assert 'Elemental Deity died' in client and 'Herobrine died' in client
for log in ['boss-menu-client.log', 'boss-audit-gallery.log']:
    text = (root / log).read_text(errors='replace')
    assert 'BUILD SUCCESSFUL' in text
    for problem in ['Unable to find animation', 'Failed to parse animation', 'Exception ticking', 'Reported exception thrown', 'Caught exception']:
        assert problem not in text, (log, problem)
assert 'BUILD SUCCESSFUL' in (root / 'boss-audit-build.log').read_text()
jar = root / f'build/libs/deep-night-forge-1.20.1-{version}.jar'
changed = []
unchanged = sounds = 0
allowed = {'CorruptedOgreEntity', 'DragonLordEntity', 'ElementalDeityEntity', 'EntEntity', 'FireGiantEntity',
           'HerobrineEntity', 'MartianEntity', 'NineTailsEntity', 'NotchBossEntity', 'SentinelKnightEntity', 'SkeletonlordEntity',
           'ProelementaldeathProcedure', 'ProherobrinedeathProcedure', 'ProentdeathProcedure', 'ProDragonLordProcedure', 'ProskelelordProcedure', 'SimpleMobsModItems'}
with zipfile.ZipFile(jar) as current, zipfile.ZipFile(root / 'dist/deep-night-forge-1.20.1-1.2.2.jar') as baseline:
    names = current.namelist()
    assert not any('aod_' in n.lower() or '/aod/' in n.lower() or 'age_of_dragons' in n.lower() or '/qa/' in n for n in names)
    assert not any('BossAttackProfile' in n or n.endswith('boss_attacks.json') for n in names)
    assert not any(n.endswith('/' + b + 'Entity.class') for n in names for b in ['Gael', 'Morgott', 'FirstTwin', 'SecondTwin', 'Knight4', 'Samurai4', 'Samurai5'])
    for name in ['BossNetwork', 'client/BossScreen', 'SafeSounds', 'source/SourceStun', 'source/SourceAdvancements', 'source/dawn/procedures/ProcheckaggroProcedure']:
        assert f'dev/deepnight/{name}.class' in names
    for name in ['killelementaldeity', 'the_last_invader']:
        assert f'data/simple_mobs/advancements/{name}.json' in names
    assert f'version="{version}"' in current.read('META-INF/mods.toml').decode()
    for name in baseline.namelist():
        if name.startswith('dev/deepnight/source/') or name.startswith('dev/deepnight/SafeSounds'):
            if current.read(name) != baseline.read(name):
                if name == 'dev/deepnight/source/dawn/entity/CaveDwellerEntity$1.class':
                    # Recompiling its NineTails FollowTargetGoal parent changes constructor debug
                    # parameter names. javap comparison confirmed identical executable bytecode.
                    assert hashlib.sha256(current.read(name)).hexdigest() == '62148599301bc5054b561bf746f460a34bb2ead107b3c91c3fe53b103080c7d3'
                    assert (root / 'verification/recompiled-helper-check.txt').read_text().startswith('PASS:')
                else:
                    assert name.startswith('dev/deepnight/source/dawn/') and Path(name).stem.split('$')[0] in allowed, name
                changed.append(name)
            else:
                unchanged += 1
        if name.startswith('assets/') and ('/sounds/' in name or name.endswith('/sounds.json')):
            assert current.read(name) == baseline.read(name), name
            sounds += 1

dist = root / 'dist'
dist.mkdir(exist_ok=True)
note_name = f'BOSS-AUDIT-{version}.txt'
notes = [f'THE DEEP NIGHT {version} - BOSS AUDIT', '',
         f'{len(audit)} / 22 boss lifecycle checks passed; {match[1]} server GameTests passed.',
         'Environment: Minecraft 1.20.1, Forge 47.4.10, GeckoLib 4.8.4, Java 17.',
         'The supplied crash used Forge 47.4.15 with additional mods. That full modpack was not run.', '',
         'Each boss ran native AI in an isolated, ticking open arena against a controlled survival',
         'ServerPlayer, at full, 55% and 20% health. Every boss dealt damage, accepted player melee',
         'damage, died, advanced progression, granted exactly four first-clear sigils and completed',
         'its corpse cleanup. Tests waited 320 ticks after defeat for delayed callbacks.',
         'The target heals between hits; outgoing damage is controlled. After native dimension',
         'banishment, the controlled target returns to the arena. This is functional testing,',
         'not a balance/playability rating or exhaustive coverage of every randomized attack.', '',
         'RESULTS (player hits received by boss / hits dealt by boss or its lightning):']
for key in [b.lower() for b in bosses]:
    row = audit[key]
    notes.append(f"PASS  {row['boss']}: {row['player_damage_hits']} / {row['boss_damage_hits']}; defeated at tick {row['defeated_at_tick']}")
notes += ['', 'CLIENT CHECKS', client, gallery.splitlines()[-1], '',
          'FIXES',
          '- Elemental Deity and DawnCraft Herobrine: restore and safely award delayed advancements.',
         '- Restore DawnCraft Herobrine\'s missing Accursed Sword trophy drop.',
          '- Dragon Lord: release invulnerability on the early dive collision transition.',
          '- Skeleton Lord: restore native aggression callback and repair invulnerability commands.',
          '- Ent: remove defeated entity when its staff reward appears.',
          '- Update DawnCraft health bars during NoAI/stuns; add missing Divine Guardian/Knight bars.',
          '- Preserve 1.2.2 sound protections and creative boss replacement/menu behavior.', '',
          'DIAGNOSTICS',
          'No boss runtime exceptions or failed tests remained in the passing run. Forge logged',
          'the recoverable first-run absence of server.properties before generating that file.',
          'The client also logs legacy custom animation instructions without handlers; native',
          'source combat and all 161 model/texture/animation/renderer checks still passed.', '',
          'Install the new Deep Night jar on both server and clients, replacing the prior jar.',
          'Keep only one Deep Night jar. The install ZIP also contains GeckoLib 4.8.4.']
(root / note_name).write_text('\n'.join(notes) + '\n', encoding='utf-8')
shutil.copy2(root / note_name, dist / note_name)
copy = dist / jar.name
shutil.copy2(jar, copy)
docs = ['README.txt', 'ASSET-NOTES.txt', 'IMPORTED-CREATURES.txt', note_name]
install = dist / f'Deep-Night-{version}-Forge-1.20.1-install.zip'
with zipfile.ZipFile(install, 'w', zipfile.ZIP_DEFLATED) as archive:
    archive.write(copy, 'mods/' + copy.name)
    gecko = root / 'compat-mods/geckolib-forge-1.20.1-4.8.4.jar'
    archive.write(gecko, 'mods/' + gecko.name)
    for name in docs:
        archive.write(root / name, name)
source = dist / f'deep-night-forge-1.20.1-{version}-source.zip'
with zipfile.ZipFile(source, 'w', zipfile.ZIP_DEFLATED) as archive:
    for folder in ['src', 'gradle', 'compat-mods']:
        for path in sorted((root / folder).rglob('*')):
            if path.is_file():
                archive.write(path, path.relative_to(root))
    for name in docs + ['gradlew', 'gradlew.bat', 'build.gradle', 'gradle.properties', 'settings.gradle', '.gitignore',
                       'tools/audit_source_animations.py', 'tools/package_boss_audit_release.py',
                       'boss-lifecycle-tests.log', 'boss-menu-client.log', 'boss-audit-gallery.log', 'boss-audit-build.log',
                       'verification/boss-lifecycle-report.json', 'verification/boss-menu-client-report.txt', 'verification/source-client-report.txt',
                       'verification/recompiled-helper-check.txt',
                       'verification/dawn-animation-audit.json', 'verification/source-api-repairs.json', 'verification/source-command-remaps.json']:
        archive.write(root / name, name)
report = {'version': version, 'server_tests': int(match[1]), 'boss_lifecycles': audit, 'client_checks': client.splitlines(),
          'recoverable_first_run_messages': server_errors,
          'source_render_checks': gallery.splitlines(), 'intentional_source_changes_since_1_2_2': changed,
          'unchanged_1_2_2_source_classes': unchanged, 'unchanged_1_2_2_sound_assets': sounds, 'artifacts': []}
for path in [copy, install, source]:
    report['artifacts'].append({'name': path.name, 'bytes': path.stat().st_size, 'sha256': hashlib.sha256(path.read_bytes()).hexdigest()})
(dist / f'Deep-Night-{version}-verification.json').write_text(json.dumps(report, indent=2) + '\n')
print(json.dumps({'version': version, 'server_tests': int(match[1]), 'bosses_passed': len(audit), 'artifacts': report['artifacts']}, indent=2))
