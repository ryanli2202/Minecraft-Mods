/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.entity.IceDragMultiPart6Entity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class IceDragMultiPart6Renderer
extends HumanoidMobRenderer<IceDragMultiPart6Entity, HumanoidModel<IceDragMultiPart6Entity>> {
    public IceDragMultiPart6Renderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER)), 0.0f);
        this.addLayer(new HumanoidArmorLayer(this, new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(IceDragMultiPart6Entity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

