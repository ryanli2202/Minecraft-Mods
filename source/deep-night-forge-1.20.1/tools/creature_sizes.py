"""Absolute size overrides shared by both catalogue generators (never compound)."""
import json
import re
from pathlib import Path

SIZES = json.loads(Path(__file__).with_name('creature_size_overrides.json').read_text())

def apply_record(record):
    if record['id'] in SIZES:
        size = SIZES[record['id']]
        record['stats'][4:7] = [size['width'], size['height'], size['scale']]
        record['lift'] = size['lift']

def apply_java(source):
    for species, size in SIZES.items():
        pattern = r'(^    ' + species.upper() + r'\("[^"]+", Kind\.\w+, )([^\n]+)(\),)$'
        def replace(match):
            fields = match[2].split(', ')
            fields[4:8] = [f'{size[key]:g}f' for key in ('width', 'height', 'scale', 'lift')]
            return match[1] + ', '.join(fields) + match[3]
        source, count = re.subn(pattern, replace, source, flags=re.M)
        assert count == 1, species
    return source
