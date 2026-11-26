"""Preserve native model cubes, UVs, pivots and keyframes; do not convert to Bedrock."""
from pathlib import Path
import json,re,shutil
R=Path(__file__).resolve().parents[1]
S=R.parent/'deobfuscated/age_of_dragons-1.1.2-neoforge-1.21.1/src/main'
J=S/'java/net/mcreator/ageofdragons'
O=R/'src/main/java/dev/deepnight/source/aod/client'
rows=[r for r in json.loads((R/'verification/java-import-catalogue.json').read_text()) if r['pack']=='aod']
index={p.stem:p for p in J.rglob('*.java')}
def block(text,start):
    start=text.index('{',start);depth=1;end=start+1
    while depth:
        depth+=(text[end]=='{')-(text[end]=='}');end+=1
    return text[start:end]
entries=[];models=set();anims=set()
for row in rows:
    entity=Path(row['source_java'].replace('\\','/')).stem.removesuffix('Entity')
    renderer=index[entity+'Renderer'].read_text(encoding='utf-8')
    model=Path(row['model'].replace('\\','/')).stem
    models.add(model)
    animations=re.findall(r'import [\w.]+\.animations\.(\w+);',renderer)
    anims.update(animations)
    clips=[]
    for animation in animations:
        text=index[animation].read_text(encoding='utf-8')
        clips.extend(f'Map.entry("{name}",{animation}.{name})' for name in re.findall(r'AnimationDefinition\s+(\w+)\s*=',text))
    scale=re.search(r'poseStack.scale\(([^;]+)\);',renderer)
    scale_values=re.findall(r'[-\d.]+[Ff]?',scale[1]) if scale else ['1F','1F','1F']
    if scale and scale[1]=='scale, scale, scale':
        proc=re.search(r'float scale = \(float\)(\w+)\.execute',renderer)[1]
        base=re.search(r': ([\d.]+);',index[proc].read_text())[1]
        scale_values=[base+'F']*3
    if scale and scale[1]=='entity.getAgeScale(), entity.getAgeScale(), entity.getAgeScale()':scale_values=['1F']*3
    assert len(scale_values)==3,(entity,scale_values)
    overlays=re.findall(r'LAYER_TEXTURE\s*=\s*ResourceLocation.parse\("([^"]+)"\)',renderer)
    overlay_values=','.join('new ResourceLocation("'+p+'")' for p in overlays)
    mapping=row['animations']
    idle=mapping['idle']; walk=mapping['walk']; attack=mapping['attack']; sit=mapping.get('sit') or idle
    entries.append(f'case "{row["id"]}" -> new Definition({model}.createBodyLayer(),Map.ofEntries({",".join(clips)}),new ResourceLocation("age_of_dragons","{row["texture"]}"),new ResourceLocation[]{{{overlay_values}}},new float[]{{{",".join(scale_values)}}},"{idle}","{walk}","{attack}","{sit}");')
O.mkdir(parents=True,exist_ok=True)
for model in models:
    t=index[model].read_text(encoding='utf-8')
    body=block(t,t.index('createBodyLayer()'))
    for missing in re.findall(r'\bvoid (\w+);',body):
        alias=re.search(r'PartDefinition (\w+) = \w+\.addOrReplaceChild\("'+re.escape(missing)+'"',body)[1]
        body=re.sub(r'\bvoid '+re.escape(missing)+r';','',body)
        body=re.sub(r'\b'+re.escape(alias)+r'\b',missing,body)
    code='package dev.deepnight.source.aod.client;\nimport net.minecraft.client.model.geom.*;\nimport net.minecraft.client.model.geom.builders.*;\npublic final class '+model+' { public static LayerDefinition createBodyLayer() '+body+'\n}\n'
    (O/(model+'.java')).write_text(code,encoding='utf-8')
for name in anims:
    t=index[name].read_text(encoding='utf-8').replace('package net.mcreator.ageofdragons.client.model.animations;','package dev.deepnight.source.aod.client;')
    (O/(name+'.java')).write_text(t,encoding='utf-8')
code='''package dev.deepnight.source.aod.client;
import java.util.Map;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
public final class AoDVisuals {
public record Definition(LayerDefinition layer,Map<String,AnimationDefinition> clips,ResourceLocation texture,ResourceLocation[] overlays,float[] scale,String idle,String walk,String attack,String sit){}
public static Definition get(String id){return switch(id){
'''+ '\n'.join(entries)+'\ndefault -> throw new IllegalArgumentException(id);\n};}\n}\n'
(O/'AoDVisuals.java').write_text(code,encoding='utf-8')
shutil.copytree(S/'resources/assets/age_of_dragons/textures',R/'src/main/resources/assets/age_of_dragons/textures',dirs_exist_ok=True)
print(len(rows),'native visual definitions;',len(models),'original Java meshes;',len(anims),'original keyframe classes')
