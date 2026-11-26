package dev.deepnight.source.dawn;
public final class DCBossbar extends net.minecraft.server.level.ServerBossEvent {
 private final net.minecraft.world.entity.LivingEntity entity;
 public DCBossbar(net.minecraft.world.entity.LivingEntity entity,net.minecraft.world.BossEvent.BossBarColor color){super(entity.getDisplayName(),color,net.minecraft.world.BossEvent.BossBarOverlay.PROGRESS);this.entity=entity;}
 public void update(){setProgress(entity.getHealth()/entity.getMaxHealth());}
}
