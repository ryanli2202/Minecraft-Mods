from pathlib import Path
import re,json
r=Path(__file__).resolve().parents[1];j=r/'src/main/java/dev/deepnight';res=r/'src/main/resources'
p=j/'Species.java';p.write_text(re.sub(r'^    DAWN_LOST_EYE_OGRE[^\n]*\n','',p.read_text(),flags=re.M))
for name in ['source/SourceBosses.java','LegacyBossRanks.java']:
 p=j/name;p.write_text(re.sub(r'^case DAWN_LOST_EYE_OGRE[^\n]*\n','',p.read_text(),flags=re.M))
p=j/'NightMob.java';p.write_text(p.read_text().replace('DAWN_LOST_EYE_OGRE,',''))
p=j/'source/dawn/entity/LostEyeOgreEntity.java';t=p.read_text().replace('extends dev.deepnight.NightMob','extends Monster').replace('EntityType<? extends dev.deepnight.NightMob>','EntityType<LostEyeOgreEntity>').replace('super(type, world, dev.deepnight.Species.DAWN_LOST_EYE_OGRE);','super(type, world);');p.write_text(t)
p=j/'source/dawn/init/SimpleMobsModEntities.java';t=p.read_text();t=re.sub(r'^public static final java.util.function.Supplier<EntityType<LostEyeOgreEntity>>[^\n]*', 'public static final RegistryObject<EntityType<LostEyeOgreEntity>> LOST_EYE_OGRE=register("lost_eye_ogre",Builder.<LostEyeOgreEntity>of(LostEyeOgreEntity::new,MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(LostEyeOgreEntity::new).sized(0.6F,0.6F));',t,flags=re.M);p.write_text(t)
p=j/'source/dawn/DawnRuntime.java';t=p.read_text().replace('e.put(dev.deepnight.source.dawn.init.SimpleMobsModEntities.AIR.get()', 'e.put(dev.deepnight.source.dawn.init.SimpleMobsModEntities.LOST_EYE_OGRE.get(),dev.deepnight.source.dawn.entity.LostEyeOgreEntity.createAttributes().build());\ne.put(dev.deepnight.source.dawn.init.SimpleMobsModEntities.AIR.get()');p.write_text(t)
for p in (j/'source/dawn').rglob('*.java'):
 t=p.read_text();n=t.replace('deepnight:dawn_lost_eye_ogre','simple_mobs:lost_eye_ogre');
 if n!=t:p.write_text(n)
p=j/'qa/SourceCombatTests.java';t=p.read_text().replace('BOSS_COUNT==23','BOSS_COUNT==22').replace('16 DawnCraft','15 DawnCraft').replace('i<16;i++','i<15;i++').replace('stage==18','stage==17');p.write_text(t)
p=j/'GuideItem.java';p.write_text(p.read_text().replace('16 DawnCraft','15 DawnCraft'))
# Remove former standalone helper rewards/assets from Deep Night's namespace.
for p in (res/'assets/deepnight').rglob('*'):
 if p.is_file() and 'dawn_lost_eye_ogre' in p.name:p.unlink()
for p in (res/'data/deepnight').rglob('*.json'):
 if 'dawn_lost_eye_ogre' in p.name:p.unlink()
for name in ['assets/deepnight/lang/en_us.json','data/deepnight/creature_sounds.json']:
 p=res/name;d=json.loads(p.read_text());d={k:v for k,v in d.items() if 'dawn_lost_eye_ogre' not in k};p.write_text(json.dumps(d,ensure_ascii=False,indent=2))
p=r/'README.txt';p.write_text(p.read_text().replace('exactly 23 bosses: 16','exactly 22 bosses: 15')+'\nLost Eye Ogre remains the Corrupted Ogre\'s native combat helper; it is not a\nstandalone encounter and cannot block campaign progression.\n')
p=r/'IMPORTED-CREATURES.txt';lines=p.read_text().splitlines();kept=[];index=0
for l in lines:
 if 'deepnight:dawn_lost_eye_ogre' in l:continue
 if 'deepnight:' in l:index+=1;l=f'{index:2} |'+l.split('|',1)[1]
 kept.append(l.replace('16 DawnCraft','15 DawnCraft'))
p.write_text('\n'.join(kept)+'\n')
p=r/'verification/dawn-animation-audit.json';d=json.loads(p.read_text());
for row in d:
 if row['name']=='LostEyeOgre':row['kind']='combat helper'
p.write_text(json.dumps(d,indent=2))
p=r/'build.gradle';p.write_text(p.read_text().replace("run-source-tests-1.2'","run-source-tests-1.2-final'"))
print('Moved Lost Eye Ogre from campaign to its original helper registry.')
