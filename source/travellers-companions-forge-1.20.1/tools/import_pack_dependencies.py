"""Copy the exact integration dependencies from an existing pack into libs."""
import argparse, hashlib, json, pathlib, shutil

ROOT = pathlib.Path(__file__).resolve().parents[1]
parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument('mods_directory', type=pathlib.Path)
args = parser.parse_args()
manifest = json.loads((ROOT/'tools/integration-dependencies.json').read_text())
(ROOT/'libs').mkdir(exist_ok=True)
for name, expected in manifest.items():
    source = args.mods_directory/name
    if not source.is_file():
        raise SystemExit(f'Missing required dependency: {source}')
    if hashlib.sha256(source.read_bytes()).hexdigest() != expected:
        raise SystemExit(f'Wrong version or changed dependency: {source}')
    destination = ROOT/'libs'/name
    if source.resolve() != destination.resolve():
        shutil.copyfile(source, destination)
    print(name)
