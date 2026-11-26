/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.Modelshogunate_effect5;
import dev.deepnight.source.dawn.entity.ShogunateEffect5Entity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ShogunateEffect5Renderer
extends MobRenderer<ShogunateEffect5Entity, Modelshogunate_effect5<ShogunateEffect5Entity>> {
    public ShogunateEffect5Renderer(EntityRendererProvider.Context context) {
        super(context, new Modelshogunate_effect5(context.bakeLayer(Modelshogunate_effect5.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(ShogunateEffect5Entity entity) {
        int tick = entity.tickCount;
        if (tick % 20 < 3) {
            return new ResourceLocation("simple_mobs:textures/entities/shogunate_effect5/shogunate_effect5_1.png");
        }
        if (tick % 20 < 6) {
            return new ResourceLocation("simple_mobs:textures/entities/shogunate_effect5/shogunate_effect5_2.png");
        }
        if (tick % 20 < 9) {
            return new ResourceLocation("simple_mobs:textures/entities/shogunate_effect5/shogunate_effect5_3.png");
        }
        if (tick % 20 < 12) {
            return new ResourceLocation("simple_mobs:textures/entities/shogunate_effect5/shogunate_effect5_4.png");
        }
        return new ResourceLocation("simple_mobs:textures/entities/shogunate_effect5/shogunate_effect5_5.png");
    }
}

