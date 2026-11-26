from pathlib import Path
import re
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
 t=t.replace('new BlockPos(', 'BlockPos.containing(').replace('.performCommand(','.performPrefixedCommand(')
 t=t.replace('DamageTypes.ANVIL','DamageTypes.FALLING_ANVIL').replace('ForgeRegistries.ENTITIES','ForgeRegistries.ENTITY_TYPES')
 t=t.replace('Registry.DIMENSION_REGISTRY','net.minecraft.core.registries.Registries.DIMENSION')
 t=t.replace('BlockInteraction.BREAK','net.minecraft.world.level.Level.ExplosionInteraction.MOB')
 t=re.sub(r'\.getMaterial\(\)\s*== Material.LEAVES', '.is(net.minecraft.tags.BlockTags.LEAVES)',t)
 t=re.sub(r'\.getMaterial\(\)\s*== Material.WOOD', '.is(net.minecraft.tags.BlockTags.LOGS)',t)
 t=re.sub(r'\.getMaterial\(\)\s*== Material.AIR', '.isAir()',t)
 t=t.replace('DawnCraftEffects.TREMOR','dev.deepnight.source.SourceStun.TREMOR')
 t=t.replace('import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;','').replace('EffectHandler.FROZEN','dev.deepnight.source.SourceStun.FROZEN.get()')
 t=t.replace('import net.minecraft.world.level.storage.loot.LootContext.Builder;','import net.minecraft.world.level.storage.loot.LootParams.Builder;')
 t=t.replace('LootContext.Builder','net.minecraft.world.level.storage.loot.LootParams.Builder')
 t=re.sub(r'\.getMaterial\(\)\s*== Material.LAVA', '.getFluidState().is(net.minecraft.tags.FluidTags.LAVA)',t)
 t=re.sub(r'\.getMaterial\(\)\s*!= Material.WATER', '.getFluidState().is(net.minecraft.tags.FluidTags.WATER)==false',t)
 t=re.sub(r'\.getMaterial\(\)\s*== Material.PLANT', '.is(net.minecraft.tags.BlockTags.REPLACEABLE_BY_TREES)',t)
 t=t.replace('Random random','net.minecraft.util.RandomSource random')
 t=t.replace('._bs.setValue(', '._bs.setValue(').replace('_bs.setValue(_property, entry.getValue())','_bs.setValue(_property, (Comparable)entry.getValue())')
 t=re.sub(r'DamageSource.mobAttack\(\(LivingEntity\)(\w+)\)',r'\1.damageSources().mobAttack((LivingEntity)\1)',t).replace('DamageSource.GENERIC','entity.damageSources().generic()')
 t=t.replace('this.damageSources().indirectMagic(this.golem','this.golem.damageSources().indirectMagic(this.golem')
 t=t.replace('this.damageSources().mobAttack(this.golem','this.golem.damageSources().mobAttack(this.golem')
 if p.name=='MeteorAttackEntity.java':t=t.replace('entity.setNoGravity(true);','this.setNoGravity(true);')
 if p.parent.name=='entity':
  t=re.sub(r'(double [xyz] = )entity\.get([XYZ])\(\);',r'\1this.get\2();',t)
  t=t.replace('float f = entity.getYRot();','float f = this.getYRot();')
 if p.parent.name=='potion':
  t=remove_method(t,r'\s*@Override\s*public void initializeClient\(')
  t='\n'.join(l for l in t.splitlines() if 'import net.minecraftforge.client.EffectRenderer' not in l and 'import net.minecraft.client.gui.GuiComponent' not in l)+'\n'
 p.write_text(t)
p=O/'client/NightRenderer.java';t=p.read_text();t=re.sub(r'double tongue=.*?;', 'double tongue=1;',t,flags=re.S);p.write_text(t)
# Original respawn rules do not apply to campaign kills; deleted bosses stay defeated.
(D/'init/SimpleMobsModGameRules.java').write_text('''package dev.deepnight.source.dawn.init;
import net.minecraft.world.level.GameRules;
public final class SimpleMobsModGameRules {
public static final GameRules.Key<GameRules.BooleanValue> DODCBOSSESRESPAWN=GameRules.register("deepnightSourceBossesRespawn",GameRules.Category.MOBS,GameRules.BooleanValue.create(false));
}
''')
