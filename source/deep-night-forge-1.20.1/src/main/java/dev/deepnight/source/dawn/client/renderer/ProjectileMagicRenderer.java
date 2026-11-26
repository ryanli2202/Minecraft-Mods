/*
 * Decompiled with CFR 0.152.
 */
package dev.deepnight.source.dawn.client.renderer;

import dev.deepnight.source.dawn.client.model.Modelprojectile1;
import dev.deepnight.source.dawn.entity.ProjectileMagicEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ProjectileMagicRenderer
extends MobRenderer<ProjectileMagicEntity, Modelprojectile1<ProjectileMagicEntity>> {
    public ProjectileMagicRenderer(EntityRendererProvider.Context context) {
        super(context, new Modelprojectile1(context.bakeLayer(Modelprojectile1.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(ProjectileMagicEntity entity) {
        return new ResourceLocation("simple_mobs:textures/entities/a.png");
    }
}

