from pathlib import Path
import re,json,shutil
R=Path(__file__).resolve().parents[1];res=R/'src/main/resources';removed=set(json.loads((R/'verification/removed-source-content.json').read_text())['removed']);source=(R/'src/main/java/dev/deepnight/Species.java').read_text()
valid={m[1].lower() for m in re.finditer(r'^    (\w+)\("',source,re.M)}
bosses={m[1].lower() for m in re.finditer(r'^    (\w+)\("[^\n]+Kind.BOSS',source,re.M)}
deleted=[]
for p in (res/'data/deepnight').rglob('*.json'):
 t=p.read_text()
 if 'advancements/recipes/' in p.as_posix():
  recipe=res/'data/deepnight/recipes'/p.name
  if recipe.exists():
   sigil=re.search(r'deepnight:[\w]+_sigil',recipe.read_text())
   if sigil:t=re.sub(r'deepnight:[\w]+_sigil',sigil[0],t);p.write_text(t)
 if any(id.removesuffix('_sigil').removesuffix('_spawn_egg') in removed for id in re.findall(r'deepnight:([\w]+)',t)):
  p.unlink();deleted.append(str(p.relative_to(res)))
soundmap=res/'data/deepnight/creature_sounds.json';mapping=json.loads(soundmap.read_text());mapping={k:v for k,v in mapping.items() if k in valid};soundmap.write_text(json.dumps(mapping,indent=2))
p=res/'assets/deepnight/sounds.json';sounds=json.loads(p.read_text());used={v for entry in mapping.values() for v in entry.values() if isinstance(v,str)}
sounds={k:v for k,v in sounds.items() if k in used};p.write_text(json.dumps(sounds,indent=2))
paths=set()
for event in sounds.values():
 for entry in event.get('sounds',[]):
  name=entry if isinstance(entry,str) else entry.get('name','')
  if ':' in name:namespace,name=name.split(':',1)
  else:namespace='deepnight'
  if namespace=='deepnight':paths.add(name+'.ogg')
for p in (res/'assets/deepnight/sounds').rglob('*.ogg'):
 if p.relative_to(res/'assets/deepnight/sounds').as_posix() not in paths:p.unlink()
for p in (res/'assets/deepnight').rglob('*'):
 if p.is_file() and ('aod' in p.parts or any(x.startswith('aod_') for x in p.parts)):p.unlink()
# Correct the source layer's original custom glow type where provided.
for p in (R/'src/main/java/dev/deepnight/source/dawn/client/renderer').glob('*.java'):
 t=p.read_text();parts=re.split(r'(?=   protected final GeoRenderLayer)',t)
 for i,part in enumerate(parts):
  if 'public RenderType getRenderType(ResourceLocation texture)' in part:
   # Only the first layer in this segment defines this method.
   parts[i]=part.replace('this.getRenderer().getRenderType(entity,this.getRenderer().getTextureLocation(entity),buffer,partialTicks)','this.getRenderType(null)',1)
 p.write_text(''.join(parts))
p=R/'src/main/java/dev/deepnight/NightMob.java';t=p.read_text().replace('setMaxUpStep(1.5f); if(species.boss())','if(!sourceCombat())setMaxUpStep(1.5f); if(species.boss())').replace('arena=blockPosition();playSound(', 'arena=blockPosition();if(!sourceCombat())playSound(');p.write_text(t)
p=R/'src/main/java/dev/deepnight/client/ClientEvents.java';t=p.read_text();line='        dev.deepnight.source.dawn.client.SourceRenderers.renderers(e);';t=t.replace(line+'\n'+line,line);p.write_text(t)
p=R/'src/main/java/dev/deepnight/qa/CampaignTests.java';t=p.read_text().replace('Math.min(1000,s.health)', 'Math.min(1024,s.health) if_unused') if False else p.read_text();print([l.strip() for l in t.splitlines() if 'Invalid attributes' in l])
print('Removed',len(deleted),'obsolete data files;',len(bosses),'bosses;',len(valid),'species')
(R/'verification/source-resource-cleanup.json').write_text(json.dumps({'deleted':deleted,'bosses':sorted(bosses),'species':len(valid)},indent=2))
