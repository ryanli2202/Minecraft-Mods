/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.Modelelemental_projectile1;
import dev.deepnight.source.dawn.entity.ElementalProjectileEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class ElementalProjectileRenderer
extends MobRenderer<ElementalProjectileEntity, Modelelemental_projectile1<ElementalProjectileEntity>> {
    public ElementalProjectileRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelelemental_projectile1(context.bakeLayer(Modelelemental_projectile1.LAYER_LOCATION)), 0.5f);
        this.addLayer(new EyesLayer<ElementalProjectileEntity, Modelelemental_projectile1<ElementalProjectileEntity>>((RenderLayerParent)this){

            @Override
            public RenderType renderType() {
                return RenderType.eyes(new ResourceLocation("simple_mobs:textures/entities/elemental_projectile1.png"));
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(ElementalProjectileEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/elemental_projectile1.png");
    }
}

