/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.Modelninetail_projectile;
import dev.deepnight.source.dawn.entity.TailDartEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class TailDartRenderer
extends MobRenderer<TailDartEntity, Modelninetail_projectile<TailDartEntity>> {
    public TailDartRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelninetail_projectile(context.bakeLayer(Modelninetail_projectile.LAYER_LOCATION)), 0.5f);
        this.addLayer(new EyesLayer<TailDartEntity, Modelninetail_projectile<TailDartEntity>>((RenderLayerParent)this){

            @Override
            public RenderType renderType() {
                return RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/ninetail_projectile_glow.png"));
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(TailDartEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/ninetail_projectile.png");
    }
}

