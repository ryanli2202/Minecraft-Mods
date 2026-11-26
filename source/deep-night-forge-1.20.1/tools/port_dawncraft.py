"""Port reachable DawnCraft boss code, preserving its procedure state machines."""
from pathlib import Path
import json,re,shutil
R=Path(__file__).resolve().parents[1]
S=R.parent/'deobfuscated/dawncraft_mobs-1.18-1.3.9a/src/main'
J=S/'java/net/mcreator/simplemobs'
O=R/'src/main/java/dev/deepnight/source/dawn'
index={p.stem:p for p in J.rglob('*.java')}
excluded={n+'Entity' for n in json.loads((R/'verification/removed-source-content.json').read_text())['excluded_dawn']}
excluded.add('TwinsStoneEntity')
bosses=json.loads((R/'tools/java_creature_selection.json').read_text())['dawn']['boss'].split()
catalog={Path(r['source_java'].replace('\\','/')).stem:r for r in json.loads((R/'verification/java-import-catalogue.json').read_text()) if r['pack']=='dawn'}
def clean_imports(text):
    text=re.sub(r'public void push\(Entity \w+\) \{\s*}', 'public void push(Entity other) {}', text)
    # Removed Epic Fight humanoids must not return through projectile immunity checks.
    for name in excluded:
        text=re.sub(r'\b\w+(?:\.\w+\(\))? instanceof '+name+r'\b','false',text)
    # Epic Fight was used here only to stagger the victim, or show ground debris.
    def patch(m):
        target,clip=m[1],m[2]
        if clip in ['SMALL_GB','BIG_GB']:
            return f'dev.deepnight.source.SourceStun.groundBreak({target},'+('3' if clip=='BIG_GB' else '1.5')+');'
        ticks={'GRAB':10,'SHOCK':10,'BIPED_COMMON_NEUTRALIZED':40}.get(clip)
        assert ticks is not None,clip
        return f'dev.deepnight.source.SourceStun.apply({target},{ticks});'
    text=re.sub(r'LivingEntityPatch<\?> \w+ = .*?EpicFightCapabilities.getEntityPatch\((\w+), LivingEntityPatch.class\);\s*if \(\w+ != null\) \{\s*\w+.playAnimationSynchronized\((?:SimpleMobsAnimations|Animations)\.(\w+), [\d.]+F\);\s*}',patch,text,flags=re.S)
    text=re.sub(r'^import (?:yesman\.epicfight\.world.capabilities[^;]+|net\.mcreator\.simplemobs\.epicfight\.SimpleMobsAnimations);\n','',text,flags=re.M)
    body=re.sub(r'^import .*?;','',text,flags=re.M)
    return re.sub(r'^import net\.mcreator\.simplemobs\.([\w.]+);\n',lambda m:m[0] if re.search(r'\b'+m[1].split('.')[-1]+r'\b',body) else '',text,flags=re.M)
def convert(t):
    t=t.replace('net.mcreator.simplemobs','dev.deepnight.source.dawn')
    t=re.sub(r'\.level\b(?!\s*\()', '.level()',t)
    t=t.replace('net.minecraft.world.level()', 'net.minecraft.world.level').replace('net.minecraft.server.level()', 'net.minecraft.server.level')
    t=re.sub(r'\.maxUpStep\s*=\s*([^;]+);',r'.setMaxUpStep(\1);',t)
    t=t.replace('Packet<?> getAddEntityPacket()', 'Packet<net.minecraft.network.protocol.game.ClientGamePacketListener> getAddEntityPacket()')
    t=t.replace('software.bernie.geckolib3.core.IAnimatable','software.bernie.geckolib.animatable.GeoEntity').replace('IAnimatable','GeoEntity')
    replacements={'software.bernie.geckolib3.core.PlayState':'software.bernie.geckolib.core.object.PlayState','software.bernie.geckolib3.core.builder.AnimationBuilder':'software.bernie.geckolib.core.animation.RawAnimation','software.bernie.geckolib3.core.controller.AnimationController':'software.bernie.geckolib.core.animation.AnimationController','software.bernie.geckolib3.core.event.predicate.AnimationEvent':'software.bernie.geckolib.core.animation.AnimationState','software.bernie.geckolib3.core.manager.AnimationData':'software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar','software.bernie.geckolib3.core.manager.AnimationFactory':'software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache'}
    for a,b in replacements.items():t=t.replace(a,b)
    t=t.replace('AnimationEvent','AnimationState').replace('AnimationData data','ControllerRegistrar data').replace('data.addAnimationController(', 'data.add(')
    t=t.replace('software.bernie.geckolib3.core.event.SoundKeyframeEvent','software.bernie.geckolib.core.keyframe.event.SoundKeyframeEvent')
    t=t.replace('registerSoundListener(', 'setSoundKeyframeHandler(')
    t=t.replace('new SoundEvent(new ResourceLocation(', 'SoundEvent.createVariableRangeEvent(new ResourceLocation(')
    t=t.replace('isOnGround()', 'onGround()')
    t=t.replace('net.minecraft.network.chat.TextComponent','net.minecraft.network.chat.Component').replace('new TextComponent(', 'Component.literal(').replace('TextComponent.EMPTY','Component.empty()')
    t=t.replace('net.minecraft.network.chat.TranslatableComponent','net.minecraft.network.chat.Component').replace('new TranslatableComponent(', 'Component.translatable(')
    t=t.replace('new Random()', 'net.minecraft.util.RandomSource.create()')
    t=t.replace('net.minecraftforge.event.world.', 'net.minecraftforge.event.level.').replace('.getWorld()', '.getLevel()').replace('.getPlayer()', '.getEntity()')
    t=t.replace('.getCommands().performCommand(', '.getCommands().performPrefixedCommand(')
    t=t.replace('DamageSource.OUT_OF_WORLD','DamageSource.FELL_OUT_OF_WORLD')
    t=t.replace('net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()', 'net.minecraft.world.level.block.state.BlockBehaviour.Properties.of()')
    t=t.replace('import net.minecraft.world.level.material.Material;','')
    t=re.sub(r'Properties.of\(Material\.\w+\)', 'Properties.of()',t)
    t=re.sub(r'\.tab\([^)]*\)', '',t)
    t=t.replace('new AnimationFactory(this)','software.bernie.geckolib.util.GeckoLibUtil.createInstanceCache(this)').replace('AnimationFactory','AnimatableInstanceCache').replace('getFactory()', 'getAnimatableInstanceCache()')
    t=t.replace('new AnimationBuilder()', 'RawAnimation.begin()')
    t=re.sub(r'\.addAnimation\(("[^"]+")\s*,\s*true\)',r'.thenLoop(\1)',t)
    t=re.sub(r'\.addAnimation\(("[^"]+")\s*,\s*false\)',r'.thenPlay(\1)',t)
    t=re.sub(r'\.addAnimation\(("[^"]+")\)',r'.then(\1,software.bernie.geckolib.core.animation.Animation.LoopType.DEFAULT)',t)
    t=re.sub(r'\.transitionLengthTicks\s*=\s*(\d+)\.0;',r'.setTransitionLength(\1);',t)
    t=re.sub(r'new AnimationController\(this, ("[^"]+"), (\d+)\.0F,',r'new AnimationController<>(this, \1, \2,',t)
    t=t.replace('.markNeedsReload()', '.forceAnimationReset()')
    t=re.sub(r'(\w+) == DamageSource\.(\w+)',r'\1.is(net.minecraft.world.damagesource.DamageTypes.\2)',t)
    t=t.replace('.isExplosion()', '.is(net.minecraft.tags.DamageTypeTags.IS_EXPLOSION)')
    t=t.replace('.isProjectile()', '.is(net.minecraft.tags.DamageTypeTags.IS_PROJECTILE)')
    t=t.replace('.isFire()', '.is(net.minecraft.tags.DamageTypeTags.IS_FIRE)')
    # Static 1.18 damage factories became per-level registries in 1.20.
    for old,new in [('mobAttack','mobAttack'),('playerAttack','playerAttack'),('indirectMagic','indirectMagic'),('thrown','thrown')]:
        t=re.sub(r'DamageSource\.'+old+r'\((\w+)',lambda m:m[1]+'.damageSources().'+new+'('+m[1],t)
    t=re.sub(r'(\w+)\.hurt\(DamageSource\.(\w+),',lambda m:m[1]+'.hurt('+m[1]+'.damageSources().source(net.minecraft.world.damagesource.DamageTypes.'+m[2]+'),',t)
    return t
registry_names={n for n,p in index.items() if p.parent.name=='init'}
registry_fields={}
for name in registry_names:
    registry_fields[name]={m[2]:(m[1],m[3]) for m in re.finditer(r'public static final (RegistryObject<[^;=]+>)\s+(\w+)\s*=\s*(.*?);',index[name].read_text(),re.S)}
seen=set();wanted={};pending=[b+'Entity' for b in bosses];parents={}
while pending:
    name=pending.pop()
    if name in seen or name not in index:continue
    if name in registry_names:continue
    if name in excluded:
        chain=[name]
        while chain[-1] in parents and parents[chain[-1]] not in chain:chain.append(parents[chain[-1]])
        raise ValueError('Removed dependency: '+' <- '.join(chain))
    seen.add(name)
    content=clean_imports(index[name].read_text(encoding='utf-8'))
    body=re.sub(r'^import .*?;','',content,flags=re.M)
    for registry,field in re.findall(r'\b(SimpleMobsMod\w+)\.(\w+)',body):
        if registry in registry_fields and field in registry_fields[registry] and field not in wanted.setdefault(registry,set()):
            wanted[registry].add(field)
            typ,expr=registry_fields[registry][field]
            if registry!='SimpleMobsModItems':
                for n in set(re.findall(r'\b\w+\b',expr)):
                    if n in index:pending.append(n);parents.setdefault(n,name)
    for n in set(re.findall(r'\b\w+\b',body)):
        if n in index and n not in registry_names and n not in ['SimpleMobsMod','SimpleMobsAnimations'] and n!=name:
            pending.append(n);parents.setdefault(n,name)
for name in sorted(seen):
    if name=='SimpleMobsMod':continue
    p=index[name];t=clean_imports(p.read_text(encoding='utf-8'))
    if p.parent.name=='entity' and name.removesuffix('Entity') in bosses:
        species=catalog[name]['id'].upper()
        t=t.replace('extends Monster implements','extends dev.deepnight.NightMob implements')
        t=t.replace('EntityType<'+name+'> type','EntityType<? extends dev.deepnight.NightMob> type')
        t=t.replace('super(type, world);',f'super(type, world, dev.deepnight.Species.{species});')
    t=convert(t)
    out=O/p.relative_to(J);out.parent.mkdir(parents=True,exist_ok=True);out.write_text(t,encoding='utf-8')
for name,fields in wanted.items():
    orig=index[name].read_text()
    imports='\n'.join(re.findall(r'^import .*?;',orig,re.M))
    declarations=[]
    for field in sorted(fields):
        typ,expr=registry_fields[name][field]
        match=re.search(r'(\w+Entity)::new',expr)
        if match and match[1].removesuffix('Entity') in bosses:
            cls=match[1];species=catalog[cls]['id'].upper()
            declarations.append(f'public static final java.util.function.Supplier<EntityType<{cls}>> {field}=()->(EntityType<{cls}>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.{species}).get();')
        elif name=='SimpleMobsModItems':
            path=re.search(r'"([^"]+)"',expr)[1]
            declarations.append(f'public static final RegistryObject<net.minecraft.world.item.Item> {field}=REGISTRY.register("{path}",()->new net.minecraft.world.item.Item(new net.minecraft.world.item.Item.Properties()));')
        else:declarations.append(f'public static final {typ} {field}={expr};')
    reg=re.search(r'public static final DeferredRegister<.*?;',orig,re.S)
    assert reg,name
    helper=''
    if name.endswith('Entities'):helper='private static <T extends net.minecraft.world.entity.Entity> RegistryObject<EntityType<T>> register(String name,EntityType.Builder<T> builder){return REGISTRY.register(name,()->builder.build(name));}'
    t='package net.mcreator.simplemobs.init;\n'+imports+'\npublic final class '+name+' {\n'+reg[0]+'\n'+'\n'.join(declarations)+'\n'+helper+'\n}\n'
    out=O/'init'/(name+'.java');out.parent.mkdir(parents=True,exist_ok=True);out.write_text(convert(clean_imports(t)),encoding='utf-8')
manifest={'classes':sorted(seen),'registries':{k:sorted(v) for k,v in wanted.items()},'bosses':bosses}
(R/'verification/dawn-source-port-manifest.json').write_text(json.dumps(manifest,indent=2))
shutil.copytree(S/'resources/assets/simple_mobs',R/'src/main/resources/assets/simple_mobs',dirs_exist_ok=True)
print(len(seen),'DawnCraft dependency classes;',sum(len(f) for f in wanted.values()),'registry bindings')
