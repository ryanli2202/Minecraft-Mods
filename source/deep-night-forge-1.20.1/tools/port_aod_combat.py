"""Reachability-driven Age of Dragons source port; no fabricated combat profiles."""
from pathlib import Path
import re,json,shutil
R=Path(__file__).resolve().parents[1]
S=R.parent/'deobfuscated/age_of_dragons-1.1.2-neoforge-1.21.1/src/main'
J=S/'java/net/mcreator/ageofdragons'
O=R/'work-in-progress/source-combat/src/main/java/dev/deepnight/source/aod'
index={p.stem:p for p in J.rglob('*.java')}
bosses=json.loads((R/'tools/java_creature_selection.json').read_text())['aod']['boss'].split()
catalog={Path(r['source_java'].replace('\\','/')).stem:r for r in json.loads((R/'verification/java-import-catalogue.json').read_text()) if r['pack']=='aod'}
def trim_imports(t):
    body=re.sub(r'^import .*?;','',t,flags=re.M)
    return re.sub(r'^import net\.mcreator\.ageofdragons\.([\w.]+);\n',lambda m:m[0] if re.search(r'\b'+m[1].split('.')[-1]+r'\b',body) else '',t,flags=re.M)
def remove_method(t,signature):
    match=re.search(signature,t)
    if not match:return t
    start=t.index('{',match.start());depth=1;end=start+1
    while depth:depth+=(t[end]=='{')-(t[end]=='}');end+=1
    return t[:match.start()]+t[end:]
def preprocess(t):
    # Keep Deep Night's interaction/progression; source battle hooks remain intact.
    t=remove_method(t,r'(?:@Override\s*)?public InteractionResult mobInteract\(')
    t=remove_method(t,r'(?:@Override\s*)?public static void init\(RegisterSpawnPlacementsEvent')
    return trim_imports(t)
def convert(t):
    t=t.replace('net.mcreator.ageofdragons','dev.deepnight.source.aod')
    t=t.replace('net.neoforged.api.distmarker','net.minecraftforge.api.distmarker').replace('net.neoforged.bus.api','net.minecraftforge.eventbus.api')
    t=t.replace('net.neoforged.fml.common.EventBusSubscriber','net.minecraftforge.fml.common.Mod.EventBusSubscriber')
    t=t.replace('net.neoforged.neoforge','net.minecraftforge')
    t=t.replace('NeoForgeMod','ForgeMod').replace('common.NeoForge','common.MinecraftForge').replace('NeoForge.EVENT_BUS','MinecraftForge.EVENT_BUS')
    t=t.replace('ResourceLocation.parse(', 'new ResourceLocation(').replace('ResourceLocation.fromNamespaceAndPath(', 'new ResourceLocation(')
    t=t.replace('import net.minecraft.network.syncher.SynchedEntityData.Builder;','')
    t=t.replace('defineSynchedData(Builder builder)','defineSynchedData()').replace('super.defineSynchedData(builder);','super.defineSynchedData();').replace('builder.define(', 'this.entityData.define(')
    t=t.replace('getDefaultDimensions(Pose pose)', 'getDimensions(Pose pose)').replace('super.getDefaultDimensions(pose)','super.getDimensions(pose)')
    t=t.replace('.igniteForSeconds(', '.setSecondsOnFire(').replace('.igniteForTicks(', '.setRemainingFireTicks(')
    t=t.replace('livingdata) {','livingdata, @Nullable CompoundTag spawnTag) {').replace('reason, livingdata);','reason, livingdata, spawnTag);')
    t=t.replace('import net.minecraftforge.event.entity.RegisterSpawnPlacementsEvent;','')
    t=t.replace('Attributes.STEP_HEIGHT','net.minecraftforge.common.ForgeMod.STEP_HEIGHT_ADDITION.get()')
    t=t.replace('Attributes.GRAVITY','net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get()')
    t=re.sub(r'getAttribute\((Attributes\.\w+)\)',r'getAttribute(\1)',t)
    t=t.replace('SoundEvents.GENERIC_EXPLODE.value()','SoundEvents.GENERIC_EXPLODE')
    t=re.sub(r'BuiltInRegistries\.(\w+)\.getHolderOrThrow\((.*?)\)',r'BuiltInRegistries.\1.getHolderOrThrow(\2)',t)
    t=re.sub(r'([\w.]+)\.getData\(\s*AgeOfDragonsModVariables.PLAYER_VARIABLES\s*\)',r'AgeOfDragonsModVariables.get(\1)',t)
    # ModelPart 1.21 packed colors become 1.20 RGBA without altering mesh data.
    t=t.replace('int packedOverlay, int rgb)', 'int packedOverlay, float red, float green, float blue, float alpha)')
    t=t.replace('packedOverlay, rgb);','packedOverlay, red, green, blue, alpha);')
    t=t.replace('LivingEntityRenderer.getOverlayCoords(entity, 0.0F));','LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1, 1, 1, 1);')
    for missing in re.findall(r'\bvoid (\w+);',t):
        alias=re.search(r'PartDefinition (\w+) = \w+\.addOrReplaceChild\("'+re.escape(missing)+'"',t)
        if alias:
            t=re.sub(r'\bvoid '+re.escape(missing)+r';','',t)
            t=re.sub(r'\b'+re.escape(alias[1])+r'\b',missing,t)
    return t
registry_names={n for n,p in index.items() if p.parent.name=='init'}
fields={}
for name in registry_names:
    fields[name]={m[2]:(m[1],m[3]) for m in re.finditer(r'public static final (DeferredHolder<[^;=]+>|DeferredItem<[^;=]+>|DeferredBlock<[^;=]+>)\s+(\w+)\s*=\s*(.*?);',index[name].read_text(),re.S)}
seen=set();wanted={};pending=[b+'Entity' for b in bosses]
while pending:
    name=pending.pop()
    if name in seen or name not in index or name in registry_names or name in ('AgeOfDragonsMod','AgeOfDragonsModVariables'):continue
    seen.add(name)
    content=preprocess(index[name].read_text(encoding='utf-8'))
    body=re.sub(r'^import .*?;','',content,flags=re.M)
    if index[name].parent.name=='entity':
        renderer=name.removesuffix('Entity')+'Renderer'
        if renderer in index:pending.append(renderer)
    for registry,field in re.findall(r'\b(AgeOfDragonsMod\w+)\.(\w+)',body):
        if registry in fields and field in fields[registry] and field not in wanted.setdefault(registry,set()):
            wanted[registry].add(field)
            typ,expr=fields[registry][field]
            if registry!='AgeOfDragonsModItems':pending.extend(n for n in set(re.findall(r'\b\w+\b',expr)) if n in index)
    pending.extend(n for n in set(re.findall(r'\b\w+\b',body)) if n in index and n not in registry_names and n not in ('AgeOfDragonsMod','AgeOfDragonsModVariables') and index[n].parent.name!='item')
for name in sorted(seen):
    p=index[name];t=preprocess(p.read_text(encoding='utf-8'))
    if p.parent.name=='entity' and name.removesuffix('Entity') in bosses:
        species=catalog[name]['id'].upper()
        t=t.replace('extends Monster','extends dev.deepnight.NightMob')
        t=t.replace('EntityType<'+name+'> type','EntityType<? extends dev.deepnight.NightMob> type')
        t=t.replace('super(type, world);',f'super(type, world, dev.deepnight.Species.{species});')
    out=O/p.relative_to(J);out.parent.mkdir(parents=True,exist_ok=True);out.write_text(convert(t),encoding='utf-8')
manifest={'classes':sorted(seen),'registries':{k:sorted(v) for k,v in wanted.items()},'bosses':bosses}
(R/'verification/aod-source-port-manifest.json').write_text(json.dumps(manifest,indent=2))
for name,needed in wanted.items():
    orig=index[name].read_text();imports='\n'.join(re.findall(r'^import .*?;',orig,re.M))
    declarations=[]
    for field in sorted(needed):
        typ,expr=fields[name][field]
        match=re.search(r'(\w+Entity)::new',expr)
        if match and match[1].removesuffix('Entity') in bosses:
            cls=match[1];species=catalog[cls]['id'].upper()
            declarations.append(f'public static final java.util.function.Supplier<EntityType<{cls}>> {field}=()->(EntityType<{cls}>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.{species}).get();')
        elif name=='AgeOfDragonsModItems':
            path=re.search(r'"([^"]+)"',expr)[1]
            declarations.append(f'public static final net.minecraftforge.registries.RegistryObject<net.minecraft.world.item.Item> {field}=REGISTRY.register("{path}",()->new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));')
        else:
            if typ.startswith('DeferredHolder<'):typ='RegistryObject<'+typ.split(', ',1)[1]
            elif typ.startswith('DeferredItem<'):typ=typ.replace('DeferredItem<','RegistryObject<')
            elif typ.startswith('DeferredBlock<'):typ=typ.replace('DeferredBlock<','RegistryObject<')
            declarations.append(f'public static final {typ} {field}={expr};')
    reg=re.search(r'public static final (?:DeferredRegister(?:<[^;]+?>)?|Items|Blocks) REGISTRY\s*=.*?;',orig,re.S)
    assert reg,name
    reg=reg[0].replace('DeferredRegister.Items','DeferredRegister<net.minecraft.world.item.Item>')
    reg=re.sub(r'\bItems REGISTRY', 'DeferredRegister<net.minecraft.world.item.Item> REGISTRY',reg)
    reg=re.sub(r'DeferredRegister.createItems\((.*?)\)',r'DeferredRegister.create(net.minecraftforge.registries.ForgeRegistries.ITEMS,\1)',reg)
    helper=''
    if name.endswith('Entities'):helper='private static <T extends net.minecraft.world.entity.Entity> RegistryObject<EntityType<T>> register(String name,EntityType.Builder<T> builder){return REGISTRY.register(name,()->builder.build(name));}'
    t='package net.mcreator.ageofdragons.init;\n'+imports+'\nimport net.minecraftforge.registries.RegistryObject;\npublic final class '+name+' {\n'+reg+'\n'+'\n'.join(declarations)+'\n'+helper+'\n}\n'
    t=re.sub(r'^import net.neoforged.neoforge.registries.Deferred(?:Holder|Item|Block);\n','',t,flags=re.M)
    t=re.sub(r'^import net.neoforged.neoforge.registries.DeferredRegister\.(?:Items|Blocks);\n','',t,flags=re.M)
    out=O/'init'/(name+'.java');out.parent.mkdir(parents=True,exist_ok=True);out.write_text(convert(trim_imports(t)),encoding='utf-8')
shutil.copytree(S/'resources/assets/age_of_dragons',R/'work-in-progress/source-combat/src/main/resources/assets/age_of_dragons',dirs_exist_ok=True)
print(len(seen),'AoD source classes;',sum(len(v) for v in wanted.values()),'registry fields')
