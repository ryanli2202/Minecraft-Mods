/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.Modelicedragon_proj;
import dev.deepnight.source.dawn.entity.IceDragonProjectileEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class IceDragonProjectileRenderer
extends MobRenderer<IceDragonProjectileEntity, Modelicedragon_proj<IceDragonProjectileEntity>> {
    public IceDragonProjectileRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelicedragon_proj(context.bakeLayer(Modelicedragon_proj.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(IceDragonProjectileEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/icedragon_proj.png");
    }
}

