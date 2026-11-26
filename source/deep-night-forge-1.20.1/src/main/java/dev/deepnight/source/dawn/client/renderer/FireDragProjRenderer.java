/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.ModelLavaBall;
import dev.deepnight.source.dawn.entity.FireDragProjEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class FireDragProjRenderer
extends MobRenderer<FireDragProjEntity, ModelLavaBall<FireDragProjEntity>> {
    public FireDragProjRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelLavaBall(context.bakeLayer(ModelLavaBall.LAYER_LOCATION)), 0.5f);
        this.addLayer(new EyesLayer<FireDragProjEntity, ModelLavaBall<FireDragProjEntity>>((RenderLayerParent)this){

            @Override
            public RenderType renderType() {
                return RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/lava_ball_glow.png"));
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(FireDragProjEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/lava_ball.png");
    }
}

