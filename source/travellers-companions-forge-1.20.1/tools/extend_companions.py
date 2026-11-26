from pathlib import Path
root=Path(__file__).resolve().parents[1]
src=root/'src/main/java/dev/travellerscompanions'
def edit(p,pairs):
 s=p.read_text()
 for old,new in pairs:
  assert old in s,(p,old)
  s=s.replace(old,new)
 p.write_text(s)

edit(src/'CompanionEntity.java',[
 ('extends TamableAnimal {','extends TamableAnimal implements io.redspace.ironsspellbooks.api.entity.IMagicEntity {'),
 ('private boolean restoring,exiting;', '''public final SimpleContainer spellInventory=new SimpleContainer(CompanionSpells.SLOTS){@Override public void setChanged(){super.setChanged();if(!level().isClientSide){spells().loadoutChanged();checkpoint();}}};
    private CompanionSpells spellcasting;
    public final KatanaGuard guard=new KatanaGuard(this);
    public final CommonKatanaCombat katana=new CommonKatanaCombat(this);
    private static final EntityDataAccessor<Boolean> GUARD=SynchedEntityData.defineId(CompanionEntity.class,EntityDataSerializers.BOOLEAN);
    public CompanionSpells spells(){if(spellcasting==null)spellcasting=new CompanionSpells(this);return spellcasting;}
    public boolean guarding(){return entityData.get(GUARD);}
    public void setGuarding(boolean value){entityData.set(GUARD,value);}
    public void interruptAttack(){pending=null;windup=0;stopUsingItem();}
    @Override public io.redspace.ironsspellbooks.api.magic.MagicData getMagicData(){return spells().magic();}
    @Override public void setSyncedSpellData(io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData data){if(level().isClientSide)spells().clientSync(data);}
    @Override public boolean isCasting(){return getMagicData().isCasting();}
    @Override public void initiateCastSpell(io.redspace.ironsspellbooks.api.spells.AbstractSpell spell,int level){spells().initiate(spell,level);}
    @Override public void cancelCast(){if(!level().isClientSide)spells().cancel(true);}
    @Override public void castComplete(){if(!level().isClientSide)spells().cancel(false);}
    @Override public void notifyDangerousProjectile(Projectile projectile){}
    @Override public boolean setTeleportLocationBehindTarget(int distance){return false;}
    @Override public void setBurningDashDirectionData(){}
    @Override public boolean isDrinkingPotion(){return false;}
    @Override public boolean getHasUsedSingleAttack(){return false;}
    @Override public void setHasUsedSingleAttack(boolean value){}
    @Override public void startDrinkingPotion(){}
    private boolean restoring,exiting;'''),
 ('entityData.define(HOLD,false);','entityData.define(HOLD,false);entityData.define(GUARD,false);'),
 ('public void setStance(int value){','public void setStance(int value){cancelCast();katana.cancel();'),
 ('case MARA->{','case LEONARDO->{setItemSlot(EquipmentSlot.MAINHAND,starterItem("irons_spellbooks:graybeard_staff",Items.STICK));spells().starter();}\n            case YURIKO->setItemSlot(EquipmentSlot.MAINHAND,starterItem("simplyswords:iron_katana",Items.IRON_SWORD));\n            case YUSHIRO->{setItemSlot(EquipmentSlot.MAINHAND,starterItem("common_weapons:test",Items.IRON_SWORD));if(level() instanceof ServerLevel server)software.bernie.geckolib.animatable.GeoItem.getOrAssignId(getMainHandItem(),server);}\n            case MARA->{'),
 ('public CompoundTag snapshot(){','public static ItemStack starterItem(String id,Item fallback){var item=ForgeRegistries.ITEMS.getValue(new net.minecraft.resources.ResourceLocation(id));return new ItemStack(item==null||item==Items.AIR?fallback:item);}\n    public CompoundTag snapshot(){'),
 ('n.put("Gear",gear);return n;', 'n.put("Gear",gear);var spells=new ListTag();for(int i=0;i<CompanionSpells.SLOTS;i++){var item=spellInventory.getItem(i).save(new CompoundTag());item.putInt("Slot",i);spells.add(item);}n.put("Spells",spells);if(kind()==CharacterKind.LEONARDO)n.put("Magic",spells().save());return n;'),
 ('public void restore(CompoundTag n){restoring=true;', 'public void restore(CompoundTag n){restoring=true;spellInventory.clearContent();for(Tag raw:n.getList("Spells",Tag.TAG_COMPOUND)){var item=(CompoundTag)raw;int i=item.getInt("Slot");if(i>=0&&i<CompanionSpells.SLOTS)spellInventory.setItem(i,ItemStack.of(item));}if(kind()==CharacterKind.LEONARDO)spells().load(n.getCompound("Magic"));'),
 ('boolean result=super.hurt(source,amount);','if(!level().isClientSide&&guard.block(source))return false;\n        boolean result=super.hurt(source,amount);'),
 ('@Override public void die(DamageSource source){if(level().isClientSide)return;', '@Override public void die(DamageSource source){if(level().isClientSide)return;cancelCast();katana.cancel();'),
 ('if(combatTicks>0)combatTicks--;','guard.tick();katana.tick();spells().tick();\n        if(combatTicks>0)combatTicks--;'),
 ('public void dismiss(){checkpoint();','public void dismiss(){cancelCast();katana.cancel();checkpoint();'),
 ('if(pending!=null){','if(kind()==CharacterKind.LEONARDO){\n                pending=null;stopUsingItem();if(isCasting()){getNavigation().stop();return;}\n                if(target!=null&&target.isAlive()&&canAttack(target)&&stance()!=2){double range=spells().preferredRange();if(distanceToSqr(target)>range*range||!getSensing().hasLineOfSight(target)){if(!holding())getNavigation().moveTo(target,1.1);}else getNavigation().stop();return;}\n            }\n            if(katana.active()||guarding()){getNavigation().stop();return;}\n            if(pending!=null){'),
 ('if(ranged()&&!ammo().isEmpty()){', 'if(katana.equipped()){if(distanceToSqr(target)>10||!getSensing().hasLineOfSight(target)){if(!holding())getNavigation().moveTo(target,1.1);}else{getNavigation().stop();katana.start();}return;}\n                if(ranged()&&!ammo().isEmpty()){'),
])
edit(src/'CompanionNetwork.java',[
 ('()->"2","2"::equals,"2"::equals','()->"3","3"::equals,"3"::equals'),
 ('String error="";', 'if(m.action==12||m.action==13){CompanionService.openInventory(p,m.entity,m.action==12);return;}\n        String error="";'),
])
edit(src/'CompanionService.java',[
 ('public static String train(ServerPlayer p,int kind){', '''public static void openInventory(ServerPlayer p,int id,boolean spells){
        if(!(p.level().getEntity(id) instanceof CompanionEntity e)||!e.ownedBy(p)||!e.validLease()||p.distanceToSqr(e)>64||!p.isAlive()||spells&&e.kind()!=CharacterKind.LEONARDO)return;
        if(spells)net.minecraftforge.network.NetworkHooks.openScreen(p,new net.minecraft.world.SimpleMenuProvider((window,inv,player)->new SpellMenu(window,inv,e),Component.literal("Leonardo - Spells")),b->b.writeVarInt(id));
        else net.minecraftforge.network.NetworkHooks.openScreen(p,new net.minecraft.world.SimpleMenuProvider((window,inv,player)->new CompanionMenu(window,inv,e),Component.literal(e.kind().display+" - Equipment")),b->b.writeVarInt(id));
    }
    public static String train(ServerPlayer p,int kind){'''),
])
edit(src/'CompanionEvents.java',[
 ('@SubscribeEvent public static void catchUp', '''@SubscribeEvent public static void deflect(net.minecraftforge.event.entity.ProjectileImpactEvent event){if(!event.getProjectile().level().isClientSide&&event.getRayTraceResult() instanceof net.minecraft.world.phys.EntityHitResult hit&&hit.getEntity() instanceof CompanionEntity companion&&companion.guard.deflect(event.getProjectile()))event.setCanceled(true);}
    @SubscribeEvent public static void catchUp'''),
])
edit(src/'TravellersCompanions.java',[
 ('public static final ForgeConfigSpec SPEC;', 'public static final RegistryObject<MenuType<SpellMenu>> SPELLS=MENUS.register("spells",()->IForgeMenuType.create((id,inv,data)->new SpellMenu(id,inv,(CompanionEntity)inv.player.level().getEntity(data.readVarInt()))));\n    public static final ForgeConfigSpec SPEC;'),
])
edit(src/'client/ClientEvents.java',[
 ('e.enqueueWork(()->MenuScreens.register(TravellersCompanions.EQUIPMENT.get(),EquipmentScreen::new));','e.enqueueWork(()->{MenuScreens.register(TravellersCompanions.EQUIPMENT.get(),EquipmentScreen::new);MenuScreens.register(TravellersCompanions.SPELLS.get(),SpellScreen::new);});'),
])
edit(src/'client/EquipmentScreen.java',[
 ('@Override protected void renderBg', '''@Override protected void init(){super.init();if(menu.companion!=null&&menu.companion.kind()==dev.travellerscompanions.CharacterKind.LEONARDO)addRenderableWidget(net.minecraft.client.gui.components.Button.builder(Component.literal("Spells"),b->dev.travellerscompanions.CompanionNetwork.CHANNEL.sendToServer(new dev.travellerscompanions.CompanionNetwork.Action(12,menu.companion.kind().ordinal(),menu.companion.getId()))).bounds(leftPos+117,topPos+48,51,14).build());}
    @Override protected void renderBg'''),
 ('"Backpack / food & arrows"', 'menu.companion!=null&&menu.companion.kind()==dev.travellerscompanions.CharacterKind.LEONARDO?"Backpack":"Backpack / food & arrows"'),
])
edit(src/'client/CompanionRenderer.java',[
 ('public static final class Model', '''public static dev.kosmx.playerAnim.core.data.KeyframeAnimation animation(String name){if(name==null||name.isEmpty())return null;var result=AnimationRegistry.animations.get(name);var id=ResourceLocation.tryParse(name);return result!=null?result:id==null?null:dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry.getAnimation(id);}
    public static final class Model'''),
 ('var data=name==null?null:AnimationRegistry.animations.get(name);','var data=CompanionRenderer.animation(name);'),
 ('if(data!=null&&(!e.isUsingItem()||attacking)){','if(data!=null&&!e.guarding()&&(!e.isUsingItem()||attacking)){'),
 ('hat.copyFrom(head);', '''if(e.guarding()){rightArm.xRot=-1.35f;rightArm.yRot=-.55f;rightArm.zRot=-.5f;leftArm.xRot=-1.2f;leftArm.yRot=.65f;leftArm.zRot=.3f;}
            hat.copyFrom(head);'''),
])
print('Entity, spell access, networking and renderer integrations updated.')
