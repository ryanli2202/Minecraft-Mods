"""Estimate bind-pose bounds including cube/bone rotations and idle frame zero."""
import math, itertools
def vector(value,default):
    if isinstance(value,(int,float)):return [value,value,value]
    if isinstance(value,dict):
        value=value.get('vector',value.get('post',value.get('0.0',value.get('0',next(iter(value.values()),default)))))
        return vector(value,default)
    return [v if isinstance(v,(int,float)) else default[i] for i,v in enumerate(value)] if isinstance(value,list) else default
def rotate(p,pivot,angles):
    x,y,z=[p[i]-pivot[i] for i in range(3)]
    for axis,degrees in enumerate(angles):
        c=math.cos(math.radians(degrees));s=math.sin(math.radians(degrees))
        if axis==0:y,z=y*c-z*s,y*s+z*c
        if axis==1:x,z=x*c+z*s,-x*s+z*c
        if axis==2:x,y=x*c-y*s,x*s+y*c
    return [x+pivot[0],y+pivot[1],z+pivot[2]]
def bounds(geo,animation):
    bones={b['name']:b for b in geo['minecraft:geometry'][0]['bones']}
    animated=animation.get('bones',{});points=[]
    for name,bone in bones.items():
        current=name;hidden=False
        while current:
            if 0 in vector(animated.get(current,{}).get('scale'),[1,1,1]):hidden=True;break
            current=bones[current].get('parent')
        if hidden:continue
        for cube in bone.get('cubes',[]):
            for side in itertools.product([0,1],repeat=3):
                p=[cube['origin'][i]+side[i]*cube['size'][i] for i in range(3)]
                p=rotate(p,cube.get('pivot',bone.get('pivot',[0,0,0])),cube.get('rotation',[0,0,0]))
                current=name
                while current:
                    b=bones[current];a=animated.get(current,{})
                    pivot=b.get('pivot',[0,0,0]);scale=vector(a.get('scale'),[1,1,1]);angles=vector(a.get('rotation'),[0,0,0])
                    p=[pivot[i]+(p[i]-pivot[i])*scale[i] for i in range(3)]
                    p=rotate(p,pivot,[x+y for x,y in zip(b.get('rotation',[0,0,0]),angles)])
                    offset=vector(a.get('position'),[0,0,0]);p=[p[i]+offset[i] for i in range(3)]
                    current=b.get('parent')
                points.append(p)
    return [min(p[i] for p in points) for i in range(3)],[max(p[i] for p in points) for i in range(3)]
if __name__=='__main__':
    import pathlib,json
    root=pathlib.Path(__file__).resolve().parents[1]/'src/main/resources/assets/deepnight'
    for p in (root/'geo').glob('*.json'):
        g=json.loads(p.read_text());a=json.loads((root/'animations'/p.name.replace('.geo.','.animation.')).read_text())['animations']['idle']
        lo,hi=bounds(g,a);print(p.name,[round(v/16,2) for v in lo],[round((hi[i]-lo[i])/16,2) for i in range(3)])
