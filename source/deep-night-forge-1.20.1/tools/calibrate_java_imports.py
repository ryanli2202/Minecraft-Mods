"""Store measured client idle bounds only for imports with significant offsets."""
from pathlib import Path
import json,math
R=Path(__file__).resolve().parents[1]
rows=json.loads((R/'verification/java-import-catalogue.json').read_text(encoding='utf8'))
bounds=json.loads((R/'verification/render-bounds.json').read_text())
report=(R/'verification/client-smoke-test.txt').read_text(encoding='utf8')
assert 'All 407 entity renderers' in report and 'Menu click summons exactly one' in report
result={}
for r in rows:
    b=bounds[r['id']];assert all(math.isfinite(v) for v in b) and b[4]>b[1]
    ratio=(b[4]-b[1])*r['scale']/r['height'];floor=b[1]*r['scale']+r['lift']
    if ratio<0.65 or ratio>1.6 or abs(floor)>0.75:
        result[r['id']]={'signature':r['asset_signature'],'bounds':b,'previous_height_ratio':ratio,'previous_floor_offset':floor}
(R/'tools/java_render_calibration.json').write_text(json.dumps(result,indent=2)+'\n')
print('Calibrated',len(result),'creatures against actual GeckoLib meshes')
