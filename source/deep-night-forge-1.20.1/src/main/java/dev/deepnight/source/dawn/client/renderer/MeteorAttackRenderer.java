/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.Modelmeteor;
import dev.deepnight.source.dawn.entity.MeteorAttackEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MeteorAttackRenderer
extends MobRenderer<MeteorAttackEntity, Modelmeteor<MeteorAttackEntity>> {
    public MeteorAttackRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelmeteor(context.bakeLayer(Modelmeteor.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(MeteorAttackEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/meteor.png");
    }
}

