from pathlib import Path
import re,json
R=Path(__file__).resolve().parents[1];O=R/'src/main/java/dev/deepnight';D=O/'source/dawn'
reg=(D/'init/SimpleMobsModEntities.java').read_text()
attributes=[]
for field,cls in re.findall(r'public static final RegistryObject<EntityType<(\w+)>> (\w+)',reg):
 # generic first, registry field second
 if 'createAttributes()' in (D/'entity'/(field+'.java')).read_text():attributes.append(f'e.put(dev.deepnight.source.dawn.init.SimpleMobsModEntities.{cls}.get(),dev.deepnight.source.dawn.entity.{field}.createAttributes().build());')
registries=[p.stem for p in (D/'init').glob('*.java')]
(D/'DawnRuntime.java').write_text('''package dev.deepnight.source.dawn;
public final class DawnRuntime {
 public static void register(net.minecraftforge.eventbus.api.IEventBus bus){
'''+''.join(f'dev.deepnight.source.dawn.init.{name}.REGISTRY.register(bus);\n' for name in registries)+'''bus.addListener(DawnRuntime::attributes);
 }
 private static void attributes(net.minecraftforge.event.entity.EntityAttributeCreationEvent e){
'''+ '\n'.join(attributes)+'\n}\n}\n')
(D/'DCBossbar.java').write_text('''package dev.deepnight.source.dawn;
public final class DCBossbar extends net.minecraft.server.level.ServerBossEvent {
 private final net.minecraft.world.entity.LivingEntity entity;
 public DCBossbar(net.minecraft.world.entity.LivingEntity entity,net.minecraft.world.BossEvent.BossBarColor color){super(entity.getDisplayName(),color,net.minecraft.world.BossEvent.BossBarOverlay.PROGRESS);this.entity=entity;}
 public void update(){setProgress(entity.getHealth()/entity.getMaxHealth());}
}
''')
(D/'SimpleMobsMod.java').write_text('''package dev.deepnight.source.dawn;
import java.util.function.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;
public final class SimpleMobsMod {
 public static final org.apache.logging.log4j.Logger LOGGER=org.apache.logging.log4j.LogManager.getLogger("DeepNightSource");
 public static final SimpleChannel PACKET_HANDLER=NetworkRegistry.newSimpleChannel(dev.deepnight.DeepNight.id("dawn_state"),()->"1","1"::equals,"1"::equals);
 private static int id;
 public static <T> void addNetworkMessage(Class<T> type,BiConsumer<T,FriendlyByteBuf> encoder,Function<FriendlyByteBuf,T> decoder,BiConsumer<T,Supplier<NetworkEvent.Context>> handler){PACKET_HANDLER.registerMessage(id++,type,encoder,decoder,handler,java.util.Optional.of(NetworkDirection.PLAY_TO_CLIENT));}
}
''')
# Guard the shared layer: all source entities own movement, melee, specials and phases.
p=O/'NightMob.java';t=p.read_text()
t=t.replace('this.species=species;','this.species=species;')
t=t.replace('if(species.boss()){setPersistenceRequired();entityData.set(RESERVE,(float)Math.max(0,species.health-1000));}','if(species.boss()){setPersistenceRequired();}')
t=t.replace('    @Override protected void registerGoals() {','    public boolean sourceCombat(){return getClass()!=NightMob.class;}\n    @Override protected void registerGoals() {\n        if(sourceCombat())return;')
t=t.replace('if(species.boss()&&attacking())','if(!sourceCombat()&&species.boss()&&attacking())')
t=t.replace('        if(entity instanceof Player p && (p.isCreative()||p.isSpectator())) return false;','        if(sourceCombat())return super.doHurtTarget(entity);\n        if(entity instanceof Player p && (p.isCreative()||p.isSpectator())) return false;')
t=t.replace('if(!source.is(DamageTypes.GENERIC_KILL) && !source.is(DamageTypes.FELL_OUT_OF_WORLD)) damage=', 'if(!sourceCombat()&&!source.is(DamageTypes.GENERIC_KILL) && !source.is(DamageTypes.FELL_OUT_OF_WORLD)) damage=')
t=t.replace('if(species.ranged&&!species.passive()', 'if(!sourceCombat()&&species.ranged&&!species.passive()')
t=t.replace('        if(owner==null || !Objects.equals(data.active,getUUID()))', '        if(owner==null)return;\n        if(!Objects.equals(data.active,getUUID()))')
t=t.replace('        int newPhase=', '        if(sourceCombat())return;\n        int newPhase=')
t=t.replace('if(level().isClientSide||!species.boss()', 'if(sourceCombat()||level().isClientSide||!species.boss()')
t=t.replace('if(species.boss()){bar.addPlayer(p);', 'if(species.boss()&&!sourceCombat()){bar.addPlayer(p);')
p.write_text(t)
# Retired QA assumes removed species and the retired generic attack engine.
for name in ['ImportedCreatureTests.java','BossAttackTests.java','BossAttackGallery.java','ClientSmokeTest.java']:
 p=O/'qa'/name
 if p.exists():p.rename(R/'verification'/('retired-'+name+'.txt'))
