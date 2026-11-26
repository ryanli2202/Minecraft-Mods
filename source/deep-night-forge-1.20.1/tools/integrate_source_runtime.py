from pathlib import Path
import re,json
R=Path(__file__).resolve().parents[1];O=R/'src/main/java/dev/deepnight';D=O/'source/dawn'
def remove_method(t,pattern):
 for m in list(re.finditer(pattern,t))[::-1]:
  a=t.index('{',m.start());depth=1;b=a+1
  while depth:
   if t[b]=='{':depth+=1
   elif t[b]=='}':depth-=1
   b+=1
  t=t[:m.start()]+t[b:]
 return t
for p in D.rglob('*.java'):
 t=p.read_text()
 t=re.sub(r'^import yesman\.epicfight\.gameasset\.Animations;\n','',t,flags=re.M)
 t=t.replace('yesman.epicfight.world.damagesource.EpicFightDamageSource','dev.deepnight.source.SourceDamage').replace('EpicFightDamageSource','SourceDamage')
 t=t.replace('yesman.epicfight.world.damagesource.StunType','dev.deepnight.source.SourceDamage.StunType')
 t=re.sub(r'SourceDamage.commonEntityDamageSource\(\s*"[^"]+",\s*\(LivingEntity\)(\w+),\s*Animations.DUMMY_ANIMATION\s*\)',r'SourceDamage.mob((LivingEntity)\1)',t)
 t=t.replace('com.afunproject.dawncraft.entities.DCBossbar','dev.deepnight.source.dawn.DCBossbar')
 t=t.replace('import com.afunproject.dawncraft.effects.DawnCraftEffects;','').replace('DawnCraftEffects.IMMOBILIZED','dev.deepnight.source.SourceStun.STUN')
 t=remove_method(t,r'\s*@SubscribeEvent\s*public static void addLivingEntityToBiomes\(')
 t=re.sub(r'^import net\.minecraftforge\.event\.level\.BiomeLoadingEvent;\n','',t,flags=re.M)
 t=t.replace('MobSpawnType.MOB_SUMMONED, null);','MobSpawnType.MOB_SUMMONED, null, null);')
 t=t.replace('getController().getAnimationState()', 'getController().getAnimationState()')
 t=t.replace('AnimationController.AnimationState.', 'AnimationController.State.')
 t=t.replace('event.getLimbSwingAmount()', 'event.getLimbSwingAmount()')
 t=t.replace('getController().getCurrentAnimation().animationName', 'getController().getCurrentAnimation().animation().name()')
 t=t.replace('getController().getCurrentAnimation().animationLength', 'getController().getCurrentAnimation().animation().length()')
 t=t.replace('getController().getCurrentAnimation().animation.', 'getController().getCurrentAnimation().animation().')
 t=t.replace('getController().getAnimationState()', 'getController().getAnimationState()')
 t=re.sub(r'\.thenPlay\(([^\n]+), AnimationBuilder\.ILoopType\.EDefaultLoopTypes\.PLAY_ONCE\)',r'.thenPlay(\1)',t)
 t=t.replace('damageSources().source(net.minecraft.world.damagesource.DamageTypes.', 'damageSources().source(net.minecraft.world.damagesource.DamageTypes.')
 # Explicit registry lookup replaces the private DamageSources#source helper.
 t=re.sub(r'(\w+)\.damageSources\(\)\.source\((net.minecraft.world.damagesource.DamageTypes.\w+)\)',r'new DamageSource(\1.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE).getHolderOrThrow(\2))',t)
 p.write_text(t)

# Only original combat classes are constructed for the retained bosses.
sel=json.loads((R/'tools/java_creature_selection.json').read_text())
catalog={Path(v['source_java'].replace('\\','/')).stem:v for v in json.loads((R/'verification/java-import-catalogue.json').read_text()) if v['pack']=='dawn'}
factory='''package dev.deepnight.source;
import dev.deepnight.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
public final class SourceBosses {
public static NightMob create(EntityType<? extends NightMob> type,Level level,Species species){return switch(species){
'''
attributes=[]
for pack in ['dawn','mobosses']:
 for name in sel[pack]['boss'].split()+(['DemonKingMinion'] if pack=='mobosses' else []):
  snake=re.sub(r'(?<!^)(?=[A-Z][a-z]|(?<=[a-z0-9])[A-Z])','_',name).lower()
  species=(pack+'_'+snake).upper();cls=f'dev.deepnight.source.{pack}.entity.'+('custom.' if pack=='mobosses' else '')+name+'Entity'
  factory+=f'case {species} -> new {cls}(type,level);\n'
  attributes.append(f'case {species} -> {cls}.createAttributes();')
factory+='default -> new NightMob(type,level,species);};}\npublic static AttributeSupplier.Builder attributes(Species species){return switch(species){\n'+'\n'.join(attributes)+'\ndefault -> NightMob.attributes(species);};}\n}\n'
(O/'source/SourceBosses.java').write_text(factory)
p=O/'DeepNight.java';t=p.read_text().replace('(t,l)->new NightMob(t,l,s)','(t,l)->dev.deepnight.source.SourceBosses.create(t,l,s)').replace('NightMob.attributes(s).build()','dev.deepnight.source.SourceBosses.attributes(s).build()')
t=t.replace('SoundBank.REGISTRY.register(bus);PetNetwork.register();','SoundBank.REGISTRY.register(bus);PetNetwork.register();\n        dev.deepnight.source.SourceStun.EFFECTS.register(bus);\n        dev.deepnight.source.mobosses.MoBosses.register(bus);\n        dev.deepnight.source.dawn.DawnRuntime.register(bus);')
p.write_text(t)
# Diagnostic limit for mechanical API port errors.
p=R/'build.gradle';t=p.read_text().replace("options.encoding = 'UTF-8'", "options.encoding = 'UTF-8'; options.compilerArgs += ['-Xmaxerrs','2000']");p.write_text(t)
