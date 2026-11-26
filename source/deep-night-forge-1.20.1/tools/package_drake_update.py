"""Package the verified 1.1.1 drake size update without reusing old test claims."""
from pathlib import Path
import hashlib
import shutil
import zipfile

root = Path(__file__).resolve().parents[1]
dist = root / 'dist'
version = '1.1.1'
server = (root / 'drake-size-build.log').read_text(errors='replace')
client = (root / 'drake-size-client.log').read_text(errors='replace')
report = (root / 'verification/client-smoke-test.txt').read_text(encoding='utf-8')
assert 'All 11 required tests passed' in server and 'BUILD SUCCESSFUL' in server
assert 'BUILD SUCCESSFUL' in client
assert 'Deep Night build: 1.1.1' in report and 'Runtime GeckoLib: 4.8.4' in report
assert 'All 407 entity renderers' in report and 'Menu click summons exactly one' in report
assert 'Pet menu search finds Ember Drake' in report
assert not (root / 'verification/CLIENT-FAILED.txt').exists()
jar = root / f'build/libs/deep-night-forge-1.20.1-{version}.jar'
with zipfile.ZipFile(jar) as z:
    assert z.testzip() is None
    assert 'version="1.1.1"' in z.read('META-INF/mods.toml').decode()
    assert not any(n.startswith('dev/deepnight/qa/') for n in z.namelist())
shutil.copy2(jar, dist / jar.name)
shutil.copy2(root / 'verification/client-smoke-test.txt', root / 'verification/drake-size-client-report.txt')
notes = '''THE DEEP NIGHT 1.1.1 - LARGER DRAKES

Ember Drake, Tide Drake and Dusk Drake are now 2.5 times their previous
linear size. Collision width/height and model floor offsets scale together.
Rider height follows the enlarged hitbox. Combat stats are unchanged.
Size overrides are preserved by both creature catalogue generators.

Replace the previous Deep Night JAR in your Forge 1.20.1 mods folder.
Keep only one Deep Night JAR. Update both clients and server, then restart.
Existing saved drakes use the new size after loading.
The install ZIP includes GeckoLib 4.8.4; keep only one GeckoLib JAR.

Verification for this update: build/reobfuscation and all 11 server GameTests
passed. The focused GeckoLib 4.8.4 client check passed, including all 407
renderers and summoning the enlarged Ember Drake through the pet menu.
The full client suite stopped twice at the unchanged Ashen Knight attack
pose assertion; that unrelated gallery is excluded only in the focused run.
This is not a survival pathfinding or
multiplayer playtest. Larger pets require more open space to move and summon.
'''
notes_path = dist / 'UPDATE-1.1.1.txt'
notes_path.write_text(notes, encoding='utf-8')
bundle = dist / f'Deep-Night-{version}-Forge-1.20.1-install.zip'
with zipfile.ZipFile(bundle, 'w', zipfile.ZIP_DEFLATED, compresslevel=6) as z:
    z.write(jar, 'mods/' + jar.name)
    gecko = root / 'compat-mods/geckolib-forge-1.20.1-4.8.4.jar'
    z.write(gecko, 'mods/' + gecko.name)
    z.write(notes_path, notes_path.name)
    for name in ['README.txt', 'ASSET-NOTES.txt', 'IMPORTED-CREATURES.txt']:
        z.write(root / name, name)
source = dist / f'deep-night-forge-1.20.1-{version}-source.zip'
with zipfile.ZipFile(source, 'w', zipfile.ZIP_DEFLATED, compresslevel=6) as z:
    for folder in ['src', 'tools', 'gradle', 'compat-mods']:
        for p in (root / folder).rglob('*'):
            if p.is_file() and '__pycache__' not in p.parts:
                z.write(p, p.relative_to(root).as_posix())
    for name in ['build.gradle', 'settings.gradle', 'gradle.properties', 'gradlew', 'gradlew.bat', 'README.txt', 'ASSET-NOTES.txt', 'IMPORTED-CREATURES.txt', 'drake-size-build.log', 'drake-size-client.log', 'drake-size-full-client.log', 'verification/drake-size-full-client-failure.txt', 'verification/drake-size-client-report.txt']:
        z.write(root / name, name)
    z.write(notes_path, notes_path.name)
artifacts = [dist / jar.name, bundle, source]
(dist / 'SHA256-1.1.1.txt').write_text(''.join(hashlib.sha256(p.read_bytes()).hexdigest() + '  ' + p.name + '\n' for p in artifacts))
for p in artifacts:
    with zipfile.ZipFile(p) as z:
        assert z.testzip() is None
    print(p)
