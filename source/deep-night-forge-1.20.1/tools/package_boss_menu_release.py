"""Package the verified boss-menu release, retaining the 1.2.2 sound repairs."""
from pathlib import Path
import hashlib
import json
import re
import shutil
import zipfile

root = Path(__file__).resolve().parents[1]
version = re.search(r"version = '([^']+)'", (root / 'build.gradle').read_text()).group(1)
jar = root / f'build/libs/deep-night-forge-1.20.1-{version}.jar'
bosses = re.findall(r'^    (\w+)\("[^\n]+Kind.BOSS', (root / 'src/main/java/dev/deepnight/Species.java').read_text(), re.M)
assert len(bosses) == 22
assert sum(b.startswith('DAWN_') for b in bosses) == 15
assert sum(b.startswith('MOBOSSES_') for b in bosses) == 7
server = (root / 'boss-menu-tests.log').read_text()
client = (root / 'verification/boss-menu-client-report.txt').read_text()
client_log = (root / 'boss-menu-client.log').read_text()
build = (root / 'boss-menu-build.log').read_text()
assert 'All 22 required tests passed' in server
assert client.startswith('PASS\n')
assert 'BUILD SUCCESSFUL' in build
assert 'Unable to find animation' not in client_log
assert 'Failed to parse animation' not in client_log

with zipfile.ZipFile(jar) as archive:
    names = archive.namelist()
    assert not any('aod_' in n.lower() or '/aod/' in n.lower() or 'age_of_dragons' in n.lower() for n in names)
    assert not any('BossAttackProfile' in n or n.endswith('boss_attacks.json') or '/qa/' in n for n in names)
    assert not any(n.endswith('/' + b + 'Entity.class') for n in names for b in ['Gael', 'Morgott', 'FirstTwin', 'SecondTwin', 'Knight4', 'Samurai4', 'Samurai5'])
    for name in ['BossNetwork', 'client/BossScreen', 'SafeSounds', 'source/SourceStun', 'source/dawn/entity/LostEyeOgreEntity']:
        assert f'dev/deepnight/{name}.class' in names, name
    assert f'version="{version}"' in archive.read('META-INF/mods.toml').decode()
    for name in names:
        if name.endswith(('.json', '.toml', '.txt')):
            assert not re.search(r'age_of_dragons|aod_', archive.read(name).decode('utf-8', errors='replace'), re.I), name
    # Prove that this release did not revert the latest sound/source fixes.
    baseline = root / 'dist/deep-night-forge-1.20.1-1.2.2.jar'
    checked = 0
    changed_source = []
    if baseline.exists():
        with zipfile.ZipFile(baseline) as previous:
            for name in previous.namelist():
                if name.startswith('dev/deepnight/source/') or name.startswith('dev/deepnight/SafeSounds'):
                    if archive.read(name) != previous.read(name):
                        assert name == 'dev/deepnight/source/dawn/entity/DragonLordEntity.class', f'Unexpected 1.2.2 source change: {name}'
                        changed_source.append(name)
                    else:
                        checked += 1

dist = root / 'dist'
dist.mkdir(exist_ok=True)
copy = dist / jar.name
shutil.copy2(jar, copy)
gecko = root / 'compat-mods/geckolib-forge-1.20.1-4.8.4.jar'
docs = ['README.txt', 'ASSET-NOTES.txt', 'IMPORTED-CREATURES.txt']
install = dist / f'Deep-Night-{version}-Forge-1.20.1-install.zip'
with zipfile.ZipFile(install, 'w', zipfile.ZIP_DEFLATED) as archive:
    archive.write(copy, 'mods/' + copy.name)
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
                       'tools/audit_source_animations.py', 'tools/package_boss_menu_release.py',
                       'boss-menu-tests.log', 'boss-menu-client.log', 'boss-menu-build.log',
                       'verification/boss-menu-client-report.txt', 'verification/boss-menu-next.png',
                       'verification/boss-menu-progress.png', 'verification/dawn-animation-audit.json',
                       'verification/boss-menu-replace.png', 'verification/boss-menu-dragon-lord-health.png',
                       'verification/source-api-repairs.json', 'verification/source-command-remaps.json']:
        archive.write(root / name, name)
report = {'version': version, 'bosses': bosses, 'dawncraft_bosses': 15, 'mobosses_bosses': 7,
          'server_tests': 22, 'client_checks': client.splitlines(),
          'intentional_source_changes_since_1_2_2': changed_source,
          'unchanged_1_2_2_source_and_sound_classes': checked, 'artifacts': []}
for path in [copy, install, source]:
    report['artifacts'].append({'name': path.name, 'bytes': path.stat().st_size,
                                'sha256': hashlib.sha256(path.read_bytes()).hexdigest()})
(dist / f'Deep-Night-{version}-verification.json').write_text(json.dumps(report, indent=2) + '\n')
print(json.dumps(report, indent=2))
