"""Fetch pinned public development dependencies; never modifies a launcher profile."""
import json, urllib.request, pathlib, hashlib
ROOT = pathlib.Path(__file__).resolve().parents[1]
def get(url):
    return urllib.request.urlopen(urllib.request.Request(url, headers={'User-Agent':'TravellersCompanions-build/1.0'}), timeout=60)
projects = [('better-combat','1.9.0+1.20.1-forge'), ('playeranimator','1.0.2-rc1+1.20-forge'), ('cloth-config','11.1.136+forge'), ('epic-knights-shields-armor-and-weapons','10.11'), ('architectury-api','9.2.14+forge'), ('epic-knights-addon','2.6'), ('epic-knights-bettercombat-compatibility','1.0.1')]
(ROOT/'libs').mkdir(exist_ok=True)
for project, version in projects:
    versions = json.load(get(f'https://api.modrinth.com/v2/project/{project}/version?game_versions=%5B%221.20.1%22%5D&loaders=%5B%22forge%22%5D'))
    match = next((v for v in versions if v['version_number']==version), None)
    if not match:
        print(project, 'available:',[(v['version_number'],v['files'][0]['filename']) for v in versions[:12]])
        continue
    f = next((f for f in match['files'] if f['primary']), match['files'][0])
    path=ROOT/'libs'/f['filename']
    if not path.exists(): path.write_bytes(get(f['url']).read())
    assert hashlib.sha512(path.read_bytes()).hexdigest()==f['hashes']['sha512'],f'Hash mismatch: {path}'
    print(path.name)
