package dev.thedeepnightnpcs.entity;

import dev.thedeepnightnpcs.DeepNightNpcs;
import dev.thedeepnightnpcs.network.NpcNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;

public final class NpcEntity extends PathfinderMob implements net.minecraft.world.item.trading.Merchant {
    private Vec3 home;
    private Vec3 lastSafe;
    private int attackDelay;
    private Player customer;
    private net.minecraft.world.item.trading.MerchantOffers offers = new net.minecraft.world.item.trading.MerchantOffers();
    public NpcEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        setPersistenceRequired();
        if (type == DeepNightNpcs.GUARD.get()) {
            setItemSlot(EquipmentSlot.MAINHAND, Items.IRON_SWORD.getDefaultInstance());
            setItemSlot(EquipmentSlot.OFFHAND, Items.SHIELD.getDefaultInstance());
        }
        for (EquipmentSlot slot : EquipmentSlot.values()) setDropChance(slot, 0);
    }
    public static AttributeSupplier.Builder attributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 100).add(Attributes.MOVEMENT_SPEED, 0.23)
            .add(Attributes.FOLLOW_RANGE, 12).add(Attributes.ATTACK_DAMAGE, 6).add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }
    public String kind() {
        return net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(getType()).getPath();
    }
    public boolean isGuard() { return getType() == DeepNightNpcs.GUARD.get(); }
    public Vec3 home() { return home == null ? position() : home; }
    public boolean withinPatrol(Vec3 position) {
        Vec3 origin = home();
        return Math.pow(position.x - origin.x, 2) + Math.pow(position.z - origin.z, 2) <= 25.0 && Math.abs(position.y - origin.y) <= 3;
    }
    @Override protected void registerGoals() {
        goalSelector.addGoal(0, new PatrolGoal());
        goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 10.0F, 1.0F));
        goalSelector.addGoal(2, new RandomLookAroundGoal(this));
    }
    @Override protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND && player instanceof ServerPlayer serverPlayer) NpcNetwork.open(serverPlayer, this);
        return InteractionResult.sidedSuccess(level().isClientSide);
    }
    @Override public void tick() {
        if (!level().isClientSide && home == null) { home = position(); lastSafe = home; }
        super.tick();
        if (level().isClientSide) return;
        if (!isGuard()) {
            getNavigation().stop();
            setDeltaMovement(0, getDeltaMovement().y, 0);
            setPos(home.x, getY(), home.z);
            // Keep the placement height in the save after settling onto the floor.
            if (onGround()) home = position();
        } else {
            if (!withinPatrol(position())) {
                getNavigation().stop(); setTarget(null); stopUsingItem();
                Vec3 safe = lastSafe == null ? home : lastSafe;
                teleportTo(safe.x, safe.y, safe.z); setDeltaMovement(Vec3.ZERO);
            } else if (onGround()) lastSafe = position();
            guardNearby();
        }
        Player player = level().getNearestPlayer(this, 10);
        if (player != null && getTarget() == null) {
            getLookControl().setLookAt(player, 25, 25);
            if (!isGuard()) setYRot(getYHeadRot());
        }
    }
    private void guardNearby() {
        if (attackDelay > 0) attackDelay--;
        if (tickCount % 20 == 0) {
            LivingEntity hostile = level().getEntitiesOfClass(Mob.class, getBoundingBox().inflate(5),
                mob -> mob.isAlive() && mob instanceof Enemy && withinPatrol(mob.position())).stream()
                .filter(this::hasLineOfSight).min(java.util.Comparator.comparingDouble(this::distanceToSqr)).orElse(null);
            setTarget(hostile);
        }
        LivingEntity target = getTarget();
        if (target == null || !target.isAlive() || !withinPatrol(target.position()) || !hasLineOfSight(target)) {
            setTarget(null); if (isUsingItem()) stopUsingItem(); return;
        }
        getLookControl().setLookAt(target, 30, 30);
        getNavigation().moveTo(target, 1.0);
        if (distanceToSqr(target) <= 6 && attackDelay == 0) {
            stopUsingItem(); swing(InteractionHand.MAIN_HAND); doHurtTarget(target); attackDelay = 24;
        } else if (!isUsingItem() && attackDelay < 18) startUsingItem(InteractionHand.OFF_HAND);
    }
    @Override public boolean isPushable() { return false; }
    // The native trading menu expects its merchant to be an Entity (for trade sounds).
    // Offers are references to the current customer's saved stock, never NPC-global stock.
    @Override public void setTradingPlayer(Player player) { customer = player; }
    @Override public Player getTradingPlayer() { return customer; }
    @Override public net.minecraft.world.item.trading.MerchantOffers getOffers() { return offers; }
    @Override public void overrideOffers(net.minecraft.world.item.trading.MerchantOffers value) { offers = value; }
    @Override public void notifyTrade(net.minecraft.world.item.trading.MerchantOffer offer) {
        offer.increaseUses();
        if (customer instanceof ServerPlayer player) dev.thedeepnightnpcs.trade.JorgeStock.get(player.server).setDirty();
    }
    @Override public void notifyTradeUpdated(ItemStack stack) {}
    @Override public int getVillagerXp() { return 0; }
    @Override public void overrideXp(int value) {}
    @Override public boolean showProgressBar() { return false; }
    @Override public boolean canRestock() { return false; }
    @Override public net.minecraft.sounds.SoundEvent getNotifyTradeSound() { return net.minecraft.sounds.SoundEvents.VILLAGER_YES; }
    @Override public boolean isClientSide() { return level().isClientSide; }
    @Override protected void doPush(Entity entity) {}
    @Override public void push(double x, double y, double z) {}
    @Override public boolean canBeLeashed(Player player) { return false; }
    @Override protected boolean canRide(Entity vehicle) { return false; }
    @Override public boolean canChangeDimensions() { return false; }
    @Override public boolean removeWhenFarAway(double distance) { return false; }
    @Override public boolean hurt(DamageSource source, float amount) {
        // Essential NPCs are protected; /kill remains an administrator removal tool.
        return source.is(net.minecraft.world.damagesource.DamageTypes.GENERIC_KILL) && super.hurt(source, amount);
    }
    @Override public boolean checkSpawnRules(LevelAccessor level, MobSpawnType reason) {
        return reason == MobSpawnType.SPAWN_EGG || reason == MobSpawnType.COMMAND || reason == MobSpawnType.DISPENSER;
    }
    @Override public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (home != null) { tag.putDouble("HomeX", home.x); tag.putDouble("HomeY", home.y); tag.putDouble("HomeZ", home.z); }
    }
    @Override public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("HomeX")) home = new Vec3(tag.getDouble("HomeX"), tag.getDouble("HomeY"), tag.getDouble("HomeZ"));
        lastSafe = home;
    }
    private final class PatrolGoal extends Goal {
        PatrolGoal() { setFlags(EnumSet.of(Flag.MOVE)); }
        @Override public boolean canUse() { return isGuard() && getTarget() == null && random.nextInt(90) == 0; }
        @Override public boolean canContinueToUse() { return isGuard() && getTarget() == null && !getNavigation().isDone(); }
        @Override public void start() {
            Vec3 origin = home();
            for (int attempt = 0; attempt < 8; attempt++) {
                double angle = random.nextDouble() * Math.PI * 2, radius = 1 + random.nextDouble() * 3.5;
                var path = getNavigation().createPath(net.minecraft.core.BlockPos.containing(origin.x + Math.cos(angle) * radius, origin.y, origin.z + Math.sin(angle) * radius), 0);
                if (path == null || !path.canReach()) continue;
                boolean valid = true;
                for (int i = 0; i < path.getNodeCount(); i++) if (!withinPatrol(path.getNodePos(i).getCenter())) { valid = false; break; }
                if (valid) { getNavigation().moveTo(path, 0.65); break; }
            }
        }
        @Override public void stop() { getNavigation().stop(); }
    }
}
