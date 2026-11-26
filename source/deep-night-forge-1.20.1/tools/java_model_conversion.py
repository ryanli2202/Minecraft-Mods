"""Convert Blockbench-exported Java model/keyframe data to GeckoLib 4 JSON.

No source Java is executed. Unsupported builder syntax is rejected.
Java coordinates are reflected in X/Y into Bedrock model coordinates.
"""
import re, math

NUMBER=r'[-+]?(?:\d+\.?\d*|\.\d+)(?:[eE][-+]?\d+)?'
def clean(text):
    return re.sub(r'\((?:float|double|int|String|MeshDefinition)\)', '', text)
def nums(text):
    return [float(n) for n in re.findall(NUMBER,clean(text))]

def geometry(text,identifier):
    text=clean(text)
    body=text.split('createBodyLayer()',1)[-1]
    matches=re.findall(r'(?:PartDefinition\s+)?(\w+)\s*=\s*(\w+)\.addOrReplaceChild\("([^"]+)",\s*(.*?),\s*PartPose\.(.*?)\);',body,re.S)
    assert matches, 'No model part definitions'
    bones=[]; variables={}; pivots={}
    for variable,parent,name,builder,pose in matches:
        if pose=='ZERO':v=[0,0,0]
        else:v=nums(pose)
        assert len(v) in (3,6),(name,pose)
        parent_name=variables.get(parent)
        origin=pivots.get(parent,[0,0,0])
        p=[origin[i]+v[i] for i in range(3)]
        bone={'name':name,'pivot':[-p[0],24-p[1],p[2]]}
        if parent_name:bone['parent']=parent_name
        if len(v)==6:bone['rotation']=[-math.degrees(v[3]),-math.degrees(v[4]),math.degrees(v[5])]
        uv=[0,0];mirror=False;cubes=[]
        for token in re.finditer(r'\.texOffs\(([^)]+)\)|\.mirror\(([^)]*)\)|\.addBox\((.*?)\)\)',builder,re.S):
            if token[1] is not None:uv=[int(x) for x in nums(token[1])]
            elif token[2] is not None:mirror=token[2]!='false'
            else:
                n=nums(token[3]);assert len(n)==7,(name,token[3])
                x,y,z,w,h,d,inflate=n
                cube={'origin':[-p[0]-x-w,24-p[1]-y-h,p[2]+z], 'size':[w,h,d],'uv':uv[:]}
                if inflate:cube['inflate']=inflate
                if mirror:cube['mirror']=True
                cubes.append(cube)
        assert len(cubes)==builder.count('.addBox('),(name,'Unparsed cube')
        if cubes:bone['cubes']=cubes
        variables[variable]=name;pivots[variable]=p;bones.append(bone)
    dimensions=re.search(r'LayerDefinition.create\(\w+,\s*(\d+),\s*(\d+)\)',body)
    assert dimensions,'Missing texture dimensions'
    assert len(matches)==body.count('.addOrReplaceChild('),'Unparsed model parts'
    return {'format_version':'1.12.0','minecraft:geometry':[{'description':{'identifier':'geometry.'+identifier,'texture_width':int(dimensions[1]),'texture_height':int(dimensions[2]),'visible_bounds_width':32,'visible_bounds_height':32,'visible_bounds_offset':[0,3,0]},'bones':bones}]}

def animations(text):
    text=clean(text);clips={}
    for name,length,expression in re.findall(r'AnimationDefinition\s+(\w+)\s*=\s*AnimationDefinition.Builder.withLength\(([^)]+)\)(.*?).build\(\);',text,re.S):
        bones={}
        for bone,channel,frames in re.findall(r'\.addAnimation\("([^"]+)",\s*new AnimationChannel\(AnimationChannel.Targets.(\w+),\s*new Keyframe\[\]\s*\{(.*?)\}\)\)',expression,re.S):
            key={'POSITION':'position','ROTATION':'rotation','SCALE':'scale'}[channel]
            values={}
            parsed=re.findall(r'new Keyframe\(([^,]+),\s*KeyframeAnimations.(\w+)\(([^)]+)\),\s*AnimationChannel.Interpolations.(\w+)\)',frames)
            for stamp,kind,vec,interp in parsed:
                v=nums(vec);assert len(v)==3
                if channel=='ROTATION':v=[-v[0],-v[1],v[2]]
                elif channel=='POSITION':v=[-v[0],v[1],v[2]]
                values[str(nums(stamp)[0])]={'vector':v,'lerp_mode':'catmullrom'} if interp=='CATMULLROM' else v
            assert len(parsed)==frames.count('new Keyframe('),(name,bone,'Unparsed frames',frames[:1500])
            bones.setdefault(bone,{})[key]=values
        assert expression.count('.addAnimation(')==sum(len(v) for v in bones.values()),(name,'Unparsed channels')
        clips[name]={'animation_length':nums(length)[0],'loop':'.looping()' in expression,'bones':bones}
    assert clips,'No exported keyframe animations'
    return clips
