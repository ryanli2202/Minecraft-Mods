from pathlib import Path
import re
R=Path(__file__).resolve().parents[1];O=R/'src/main/java/dev/deepnight';p=O/'NightMob.java';t=p.read_text()
t=re.sub(r'    private static final EntityDataAccessor<Float> RESERVE=.*?;\n','',t)
t=re.sub(r'    private static final EntityDataAccessor<CompoundTag> BOSS_ATTACK=.*?;\n','',t)
t=t.replace('    private Vec3 mark=Vec3.ZERO;\n','').replace('    private UUID attackTarget;\n','')
t=re.sub(r'    private int cooldown=.*?;','    private int deserted;',t)
a=t.index('    public float encounterHealth()');b=t.index('    public void bind(',a)
t=t[:a]+'''    public float encounterHealth(){return getHealth();}
    public void prepareDamage(float damage){}
'''+t[b:]
t=t.replace('    public int phase() {return phase;}\n','')
a=t.index('    @Override public boolean attacking()');b=t.index('    @Override public boolean removeWhenFarAway',a)
t=t[:a]+'''    @Override public boolean attacking(){return entityData.get(ATTACK)>0;}
'''+t[b:]
t=t.replace('entityData.define(RESERVE,0f);entityData.define(BOSS_ATTACK,new CompoundTag());','')
a=t.index('        goalSelector.addGoal(2,new MeleeAttackGoal(');b=t.index('        goalSelector.addGoal(4,',a)
t=t[:a]+'''        goalSelector.addGoal(2,new MeleeAttackGoal(this,1,true));
'''+t[b:]
t=re.sub(r'        if\(!sourceCombat\(\)&&species.boss\(\)&&attacking\(\)\)\{.*?\n        }\n','',t,flags=re.S)
a=t.index('        if(species.boss()){',t.index('public boolean doHurtTarget'));b=t.index('        boolean hit=',a)
t=t[:a]+t[b:]
t=re.sub(r'            if\(source.is\(DamageTypes.IN_WALL\).*?participants.add\(p.getUUID\(\)\);', '''            ServerPlayer p=creditedPlayer(source.getEntity());
            if(p!=null && !p.isCreative() && !p.isSpectator()) participants.add(p.getUUID());''',t,flags=re.S)
t=re.sub(r'        if\(!sourceCombat\(\)&&species.boss\(\)&&attacking\(\)\)lockAttackFacing\(\);\n','',t)
t=t.replace('{cancelBossAttack();teleportTo(', '{teleportTo(')
a=t.index('        if(sourceCombat())return;\n        int newPhase=');b=t.index('    @Override public void die(',a)
t=t[:a]+'''        if(!hasSourceBossBar())bar.setProgress(getHealth()/getMaxHealth());
    }
    private boolean hasSourceBossBar(){return sourceCombat()&&switch(species){
        case DAWN_FIRE_DRAGON,DAWN_ICE_DRAGON,DAWN_LIGHTNING_DRAGON,DAWN_LOST_EYE_OGRE,DAWN_MOSS_GOLEM -> false;
        default -> true;
    };}
'''+t[b:]
t=t.replace('        if(species.boss())cancelBossAttack();\n','')
t=t.replace('species.boss()&&!sourceCombat()', 'species.boss()&&!hasSourceBossBar()')
t=re.sub(r'        t.putInt\("Phase".*?;\n','        t.putInt("Deserted",deserted);\n',t)
t=re.sub(r'        phase=t.getInt\("Phase".*?;\n','        deserted=t.getInt("Deserted");\n',t)
t=re.sub(r'        if\(species.boss\(\)\)cancelBossAttack\(\);[^\n]+\n','',t)
a=t.index('    @Override public void registerControllers(')
t=t[:a]+'''    @Override public void registerControllers(AnimatableManager.ControllerRegistrar c) {
        c.add(new AnimationController<NightMob>(this,"body",4,state -> state.setAndContinue(
            isDeadOrDying()?RawAnimation.begin().thenPlay("death")
            :hurtTime>0?RawAnimation.begin().thenPlay("hurt")
            :attacking()?RawAnimation.begin().thenLoop("attack")
            :tickCount<20?RawAnimation.begin().thenPlay("spawn")
            :RawAnimation.begin().thenLoop(state.isMoving()?"walk":"idle"))));
    }
    @Override public AnimatableInstanceCache getAnimatableInstanceCache(){return cache;}
}
'''
p.write_text(t)
(O/'BossAttackProfile.java').unlink()
(R/'src/main/resources/data/deepnight/boss_attacks.json').unlink(missing_ok=True)
p=O/'source/SourceBosses.java';t=p.read_text().replace('||switch(s){case ->true;default->false;}','');p.write_text(t)
p=R/'src/main/resources/META-INF/mods.toml';p.write_text(p.read_text().replace('version="1.1.1"','version="1.2.0"').replace('[4.4.7,5)','[4.8.4,5)'))
