/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.Modelthrowncube;
import dev.deepnight.source.dawn.entity.CubeEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class CubeRenderer
extends MobRenderer<CubeEntity, Modelthrowncube<CubeEntity>> {
    public CubeRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelthrowncube(context.bakeLayer(Modelthrowncube.LAYER_LOCATION)), 0.5f);
        this.addLayer(new EyesLayer<CubeEntity, Modelthrowncube<CubeEntity>>((RenderLayerParent)this){

            @Override
            public RenderType renderType() {
                return RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/throwncube.png"));
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(CubeEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/throwncube.png");
    }
}

