/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.ModelFireball;
import dev.deepnight.source.dawn.entity.FireballEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class FireballRenderer
extends MobRenderer<FireballEntity, ModelFireball<FireballEntity>> {
    public FireballRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelFireball(context.bakeLayer(ModelFireball.LAYER_LOCATION)), 0.5f);
        this.addLayer(new EyesLayer<FireballEntity, ModelFireball<FireballEntity>>((RenderLayerParent)this){

            @Override
            public RenderType renderType() {
                return RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/fireball2.png"));
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(FireballEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/fireball_solid.png");
    }
}

