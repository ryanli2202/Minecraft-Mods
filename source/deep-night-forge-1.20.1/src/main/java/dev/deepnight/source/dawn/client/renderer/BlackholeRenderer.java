/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.Modelblackhole;
import dev.deepnight.source.dawn.entity.BlackholeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BlackholeRenderer
extends MobRenderer<BlackholeEntity, Modelblackhole<BlackholeEntity>> {
    public BlackholeRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelblackhole(context.bakeLayer(Modelblackhole.LAYER_LOCATION)), 0.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(BlackholeEntity entity) {
        int tick = entity.tickCount;
        if (tick % 16 < 4) {
            return new ResourceLocation("simple_mobs:textures/entities/blackhole/blackhole_1.png");
        }
        if (tick % 16 < 8) {
            return new ResourceLocation("simple_mobs:textures/entities/blackhole/blackhole_2.png");
        }
        if (tick % 16 < 12) {
            return new ResourceLocation("simple_mobs:textures/entities/blackhole/blackhole_3.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/blackhole/blackhole_4.png");
    }
}

