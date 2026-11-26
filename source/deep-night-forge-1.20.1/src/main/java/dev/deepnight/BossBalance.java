package dev.deepnight;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

@Mod.EventBusSubscriber(modid=DeepNight.ID)
public final class BossBalance {
    public static final float DAMAGE_MULTIPLIER=3.0f;

    public static void raiseHealthLimit() {
        // Vanilla clamps MAX_HEALTH to 1024. Keep any larger modpack limit.
        RangedAttribute health=(RangedAttribute)Attributes.MAX_HEALTH;
        if(health.getMaxValue()<1000000)
            ObfuscationReflectionHelper.setPrivateValue(RangedAttribute.class,health,1000000.0,"f_22308_");
    }

    private static boolean bossAttack(Entity attacker) {
        if(attacker instanceof NightMob mob)return mob.species.boss()||mob.species.kind==Species.Kind.HELPER;
        // Ported attack effects/projectiles are separate entities, not NightMobs.
        return attacker!=null && (attacker.getClass().getName().startsWith("dev.deepnight.source.dawn.entity.")
            || attacker.getClass().getName().startsWith("dev.deepnight.source.mobosses.entity.custom."));
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public static void damage(LivingHurtEvent event) {
        if(bossAttack(event.getSource().getEntity()) || bossAttack(event.getSource().getDirectEntity()))
            event.setAmount(event.getAmount()*DAMAGE_MULTIPLIER);
    }
}
