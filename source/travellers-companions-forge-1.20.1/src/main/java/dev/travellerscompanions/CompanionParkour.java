package dev.travellerscompanions;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** Server-authoritative parkour for companions, with swept collision and landing checks. */
public final class CompanionParkour {
    public static final int NONE = 0, FAST_RUN = 1, CLIMB = 2, ROLL = 3;
    public static final int ROLL_TICKS = 9, CLIMB_TICKS = 14;
    private final CompanionEntity entity;
    private Vec3 start, destination, previousOwner;
    private int elapsed, dodgeCooldown, climbCooldown;
    private double ownerSpeed;

    public CompanionParkour(CompanionEntity entity) { this.entity = entity; }
    public boolean enabled() { return ParCoolBridge.installed() && TravellersCompanions.PARKOUR.get(); }
    public boolean busy() { return entity.parkourAction() == CLIMB || entity.parkourAction() == ROLL; }
    private boolean canMove() {
        return enabled() && entity.isAlive() && !entity.isNoAi() && !entity.holding()
            && !entity.isPassenger() && !entity.isInWaterOrBubble() && !entity.isInLava();
    }
    private boolean combatBusy() { return entity.isCasting() || entity.katana.active() || entity.guarding() || entity.attackPending(); }

    public void tick() {
        if (dodgeCooldown > 0) dodgeCooldown--;
        if (climbCooldown > 0) climbCooldown--;
        var owner = entity.getOwner();
        if (owner != null) {
            double speed = previousOwner == null ? 0 : owner.position().subtract(previousOwner).horizontalDistance();
            // Ignore teleports and smooth packet-sized movement updates.
            ownerSpeed = speed > 1.5 ? 0 : Mth.lerp(.35, ownerSpeed, speed);
            previousOwner = owner.position();
        } else { previousOwner = null; ownerSpeed = 0; }
        if (!canMove()) { cancel(); return; }
        if (busy()) { advance(); return; }
        boolean fast = owner != null && entity.getTarget() == null && !combatBusy()
            && !entity.isUsingItem() && !owner.isPassenger() && !owner.isFallFlying()
            && entity.distanceToSqr(owner) > 9 && (owner.isSprinting() || ownerSpeed > .26 || ParCoolBridge.fastRunning(owner));
        if (fast) {
            if (entity.parkourAction() != FAST_RUN) entity.setParkourState(FAST_RUN, 0, entity.getYRot());
        } else if (entity.parkourAction() == FAST_RUN) cancel();
    }

    public double followSpeed() {
        return entity.parkourAction() == FAST_RUN ? Mth.clamp(Math.max(1.65, ownerSpeed / .31 * 1.2), 1.65, 2.8) : 1.1;
    }

    public void moveTo(LivingEntity target, double speed) {
        if (busy()) return;
        entity.getNavigation().moveTo(target, speed);
        if (!canMove() || combatBusy() || !entity.onGround() || climbCooldown > 0) return;
        Vec3 heading = target.position().subtract(entity.position()).multiply(1, 0, 1);
        var path = entity.getNavigation().getPath();
        if (path != null && !path.isDone()) heading = path.getNextEntityPos(entity).subtract(entity.position()).multiply(1, 0, 1);
        if (heading.lengthSqr() < .04) return;
        heading = heading.normalize();
        // Only intervene at an obstruction; ordinary pathfinding handles open ground.
        if (!entity.horizontalCollision && clear(entity.getBoundingBox().move(heading.scale(.55)).deflate(.01))) return;
        tryClimb(heading);
    }

    public boolean tryClimb(Vec3 heading) {
        if (!canMove() || busy() || combatBusy() || !entity.onGround() || climbCooldown > 0) return false;
        heading = heading.multiply(1, 0, 1).normalize();
        if (heading.lengthSqr() < .5) return false;
        Vec3 origin = entity.position();
        for (double distance : new double[]{.8, 1.2, 1.6, 2.0}) {
            Vec3 ahead = origin.add(heading.scale(distance));
            for (int dy = 0; dy <= 2; dy++) {
                BlockPos support = BlockPos.containing(ahead.x, origin.y + dy - .05, ahead.z);
                if (!entity.level().hasChunkAt(support)) continue;
                var shape = entity.level().getBlockState(support).getCollisionShape(entity.level(), support);
                if (shape.isEmpty()) continue;
                double top = support.getY() + shape.max(net.minecraft.core.Direction.Axis.Y);
                double rise = top - origin.y;
                if (rise < .45 || rise > 2.01) continue;
                Vec3 landing = new Vec3(ahead.x, top, ahead.z);
                Vec3 raised = new Vec3(origin.x, top + .04, origin.z);
                if (!safeStanding(landing) || !clearSweep(origin, raised) || !clearSweep(raised, landing.add(0, .04, 0))) continue;
                startAction(CLIMB, landing, CLIMB_TICKS);
                climbCooldown = 30;
                return true;
            }
        }
        climbCooldown = 8;
        return false;
    }

    /** A successful roll evades the triggering hit only; it grants no blanket invulnerability. */
    public boolean dodge(DamageSource source) {
        if (!canMove() || busy() || combatBusy() || entity.isUsingItem() || entity.stance() == 2
            || !entity.onGround() || dodgeCooldown > 0 || entity.invulnerableTime > 0
            || source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) || source.is(DamageTypeTags.IS_EXPLOSION)
            || source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypeTags.BYPASSES_ARMOR)
            || !(source.getEntity() instanceof LivingEntity attacker) || !attacker.isAlive() || !entity.canAttack(attacker)) return false;
        if (!(source.getDirectEntity() instanceof Projectile) && entity.distanceToSqr(attacker) > 16) return false;
        // Failed attempts are also throttled, so a crowd cannot turn 22% into a guaranteed dodge.
        dodgeCooldown = 20;
        if (entity.getRandom().nextDouble() >= TravellersCompanions.DODGE_CHANCE.get()) return false;
        Vec3 away = entity.position().subtract(attacker.position()).multiply(1, 0, 1).normalize();
        if (away.lengthSqr() < .5) return false;
        Vec3 side = new Vec3(-away.z, 0, away.x).scale(entity.getRandom().nextBoolean() ? 1 : -1);
        for (Vec3 direction : new Vec3[]{side, side.scale(-1), away}) {
            Vec3 landing = entity.position().add(direction.scale(2.1));
            if (!safeRollPath(entity.position(), landing)) continue;
            entity.setTarget(attacker);
            startAction(ROLL, landing, ROLL_TICKS);
            dodgeCooldown = TravellersCompanions.DODGE_COOLDOWN.get();
            return true;
        }
        return false;
    }

    private void startAction(int action, Vec3 landing, int duration) {
        start = entity.position(); destination = landing; elapsed = 0;
        entity.interruptAttack(); entity.getNavigation().stop(); entity.setDeltaMovement(Vec3.ZERO);
        Vec3 direction = landing.subtract(start);
        float yaw = (float)(Mth.atan2(-direction.x, direction.z) * 180 / Math.PI);
        entity.setYRot(yaw); entity.setYBodyRot(yaw);
        entity.setParkourState(action, duration, yaw);
        // Discard a finished combat animation on all tracking clients, including the initiating hit.
        CompanionNetwork.animate(entity, new CombatBridge.Strike("", 1, 0, 0, 1));
    }

    private void advance() {
        if (start == null || destination == null || !safeStanding(destination)) { cancel(); return; }
        float progress = Math.min(1, ++elapsed / (float)entity.parkourDuration());
        Vec3 next;
        if (entity.parkourAction() == CLIMB) {
            Vec3 raised = new Vec3(start.x, destination.y + .04, start.z);
            next = progress <= .5f ? start.lerp(raised, progress * 2) : raised.lerp(destination, (progress - .5) * 2);
        } else {
            next = start.lerp(destination, 1 - (1 - progress) * (1 - progress));
            if (!safeRollPath(entity.position(), next)) { cancel(); return; }
        }
        if (!clearSweep(entity.position(), next)) { cancel(); return; }
        entity.getNavigation().stop(); entity.setDeltaMovement(Vec3.ZERO);
        entity.move(MoverType.SELF, next.subtract(entity.position()));
        entity.fallDistance = 0;
        entity.setYRot(entity.parkourYaw()); entity.setYBodyRot(entity.parkourYaw());
        if (progress >= 1) { entity.setOnGround(true); cancel(); }
    }

    public void cancel() {
        if (busy()) entity.setDeltaMovement(Vec3.ZERO);
        if (entity.parkourAction() != NONE) entity.setParkourState(NONE, 0, entity.getYRot());
        start = null; destination = null; elapsed = 0;
    }

    private AABB boxAt(Vec3 feet) { return entity.getBoundingBox().move(feet.subtract(entity.position())).deflate(.005); }
    private boolean clear(AABB box) {
        var world = entity.level();
        if (!world.getWorldBorder().isWithinBounds(box) || box.minY < world.getMinBuildHeight() || box.maxY >= world.getMaxBuildHeight()) return false;
        for (BlockPos pos : BlockPos.betweenClosed(Mth.floor(box.minX), Mth.floor(box.minY), Mth.floor(box.minZ), Mth.floor(box.maxX), Mth.floor(box.maxY), Mth.floor(box.maxZ))) {
            if (!world.hasChunkAt(pos) || !world.getFluidState(pos).isEmpty()) return false;
            var type = WalkNodeEvaluator.getBlockPathTypeStatic(world, pos.mutable());
            if (type.getMalus() > 0 || type == BlockPathTypes.POWDER_SNOW) return false;
        }
        return world.noCollision(entity, box);
    }
    private boolean clearSweep(Vec3 from, Vec3 to) { return clear(boxAt(from).expandTowards(to.subtract(from))); }
    private boolean safeStanding(Vec3 feet) {
        if (!clear(boxAt(feet))) return false;
        var world = entity.level();
        double edge = entity.getBbWidth() / 2 - .06;
        for (double dx : new double[]{-edge, edge}) for (double dz : new double[]{-edge, edge}) {
            BlockPos below = BlockPos.containing(feet.x + dx, feet.y - .04, feet.z + dz);
            if (!world.hasChunkAt(below) || !world.getFluidState(below).isEmpty()) return false;
            var probe = new AABB(feet.x + dx - .01, feet.y - .06, feet.z + dz - .01, feet.x + dx + .01, feet.y, feet.z + dz + .01);
            if (world.noCollision(entity, probe)) return false;
            var type = WalkNodeEvaluator.getBlockPathTypeStatic(world, BlockPos.containing(feet.x + dx, feet.y + .05, feet.z + dz).mutable());
            // Partial-height surfaces can be BLOCKED at the feet, but never allow damaging support.
            if (type != BlockPathTypes.WALKABLE && type != BlockPathTypes.OPEN && type != BlockPathTypes.BLOCKED) return false;
            var supportType = WalkNodeEvaluator.getBlockPathTypeStatic(world, below.mutable());
            if (supportType != BlockPathTypes.BLOCKED && supportType != BlockPathTypes.WALKABLE) return false;
        }
        return world.getEntitiesOfClass(LivingEntity.class, boxAt(feet), e -> e != entity && e.isAlive()).isEmpty();
    }
    private boolean safeRollPath(Vec3 from, Vec3 to) {
        if (!clearSweep(from, to)) return false;
        int samples = Math.max(1, (int)Math.ceil(from.distanceTo(to) / .2));
        for (int i = 1; i <= samples; i++) if (!safeStanding(from.lerp(to, i / (double)samples))) return false;
        return true;
    }
}
