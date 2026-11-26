"""Mechanical Forge 1.20.1 port of Mo'Bosses combat, with Deep Night lifecycle."""
from pathlib import Path
import re,json,shutil
R=Path(__file__).resolve().parents[1]
S=R.parent/'deobfuscated/mobosses-1.0.7/src/main'
J=S/'java/net/hell/mobosses'
O=R/'src/main/java/dev/deepnight/source/mobosses'
main=(J/'MoBosses.java').read_text()
bosses=json.loads((R/'tools/java_creature_selection.json').read_text())['mobosses']['boss'].split()
names=bosses+['DemonKingMinion']
def snake(s):return re.sub(r'(?<!^)(?=[A-Z][a-z]|(?<=[a-z0-9])[A-Z])','_',s).lower()
def convert(t):
    t=t.replace('net.hell.mobosses','dev.deepnight.source.mobosses')
    t=t.replace('software.bernie.geckolib.animation.','software.bernie.geckolib.core.animation.')
    t=t.replace('software.bernie.geckolib.animatable.instance.','software.bernie.geckolib.core.animatable.instance.')
    t=t.replace('software.bernie.geckolib.core.animation.PlayState','software.bernie.geckolib.core.object.PlayState')
    t=t.replace('.igniteForTicks(','.setRemainingFireTicks(')
    t=t.replace('MobSpawnType.MOB_SUMMONED, null);','MobSpawnType.MOB_SUMMONED, null, null);')
    t=t.replace('SoundEvents.GENERIC_EXPLODE.value()','SoundEvents.GENERIC_EXPLODE')
    t=t.replace('!effect.getEffect().is(MobEffects.POISON)','effect.getEffect()!=MobEffects.POISON')
    t=t.replace('ResourceLocation.fromNamespaceAndPath(', 'new ResourceLocation(').replace('ResourceLocation.parse(', 'new ResourceLocation(')
    t=t.replace('import net.minecraft.network.syncher.SynchedEntityData.Builder;','')
    t=t.replace('defineSynchedData(Builder builder)', 'defineSynchedData()').replace('super.defineSynchedData(builder);','super.defineSynchedData();').replace('builder.define(', 'this.entityData.define(')
    t=t.replace('new ResourceLocation("mobosses", "entities/', 'new ResourceLocation("deepnight", "entities/mobosses_')
    t=re.sub(r'public ResourceKey<LootTable> getDefaultLootTable\(\) \{\s*return ResourceKey.create\(Registries.LOOT_TABLE, (.*?)\);\s*}',r'public ResourceLocation getDefaultLootTable() { return \1; }',t,flags=re.S)
    return t
for p in (J/'entity').rglob('*.java'):
    t=convert(p.read_text())
    if p.stem.removesuffix('Entity') in names and '/custom/' in p.as_posix():
        name=p.stem.removesuffix('Entity')
        t=t.replace('extends Monster implements GeoEntity','extends dev.deepnight.NightMob implements GeoEntity')
        t=t.replace('EntityType<? extends Monster> type','EntityType<? extends dev.deepnight.NightMob> type')
        t=t.replace('super(type, level);',f'super(type, level, dev.deepnight.Species.MOBOSSES_{snake(name).upper()});')
    out=O/p.relative_to(J);out.parent.mkdir(parents=True,exist_ok=True);out.write_text(t,encoding='utf-8')
fields=[]
for kind,typ,field,expr in re.findall(r'public static final DeferredHolder<(SoundEvent|EntityType<\?>), (.*?)> (\w+) = (.*?);',main,re.S):
    if kind=='SoundEvent':
        path=re.search(r'"([^"]+)"',expr)[1]
        fields.append(f'public static final net.minecraftforge.registries.RegistryObject<SoundEvent> {field}=SOUNDS.register("{path}",()->SoundEvent.createVariableRangeEvent(new ResourceLocation("mobosses","{path}")));')
    else:
        entity=re.search(r'Builder.of\((\w+)::new',expr)[1]
        name=entity.removesuffix('Entity')
        if name in names:
            fields.append(f'public static final java.util.function.Supplier<EntityType<{entity}>> {field}=()->(EntityType<{entity}>)(EntityType<?>)dev.deepnight.DeepNight.HOSTILES.get(dev.deepnight.Species.MOBOSSES_{snake(name).upper()}).get();')
        else:
            expr=convert(expr).replace('ENTITY_TYPES.register(', 'dev.deepnight.DeepNight.ENTITIES.register(').replace('Builder.of(', 'EntityType.Builder.of(')
            expr=re.sub(r'^(.*?register\(\s*")',r'\1mobosses_',expr)
            fields.append(f'public static final net.minecraftforge.registries.RegistryObject<{typ}> {field}={expr};')
header='''package dev.deepnight.source.mobosses;
import dev.deepnight.source.mobosses.entity.custom.*;
import net.minecraft.world.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
public final class MoBosses {
public static final net.minecraftforge.registries.DeferredRegister<SoundEvent> SOUNDS=net.minecraftforge.registries.DeferredRegister.create(net.minecraftforge.registries.ForgeRegistries.SOUND_EVENTS,"mobosses");
'''
(O/'MoBosses.java').write_text(header+'\n'.join(fields)+'\npublic static void register(net.minecraftforge.eventbus.api.IEventBus bus){SOUNDS.register(bus);}\n}\n',encoding='utf-8')
factory='''package dev.deepnight.source.mobosses;
import dev.deepnight.*;
import dev.deepnight.source.mobosses.entity.custom.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
public final class BossFactory {
public static NightMob create(EntityType<? extends NightMob> type,Level level,Species species){return switch(species){
'''
factory+='\n'.join(f'case MOBOSSES_{snake(n).upper()} -> new {n}Entity(type,level);' for n in names)
factory+='\ndefault -> new NightMob(type,level,species);\n};}\n}\n'
(O/'BossFactory.java').write_text(factory)
shutil.copytree(S/'resources/assets/mobosses',R/'src/main/resources/assets/mobosses',dirs_exist_ok=True)
print('Ported',len(list((O/'entity').rglob('*.java'))),'MoBosses entity/AI/model/renderer files')
