/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  software.bernie.geckolib.model.GeoModel
 *  software.bernie.geckolib.model.GeoModel
 *  dev.deepnight.source.dawn.client.LegacyGeoRenderer
 *  software.bernie.geckolib.renderer.layer.GeoRenderLayer
 *  software.bernie.geckolib3.renderers.geo.IGeoRenderer
 */
package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.deepnight.source.dawn.client.Spell1Model;
import dev.deepnight.source.dawn.entity.Spell1Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class Spell1Renderer
extends LegacyGeoRenderer<Spell1Entity> {
    protected final RenderType glowTexture = RenderType.eyes(new ResourceLocation("simple_mobs", "textures/spell.png"));
    protected final GeoRenderLayer<Spell1Entity> glowLayer = new GeoRenderLayer<Spell1Entity>(this){

        public RenderType getRenderType(ResourceLocation texture) {
            return Spell1Renderer.this.glowTexture;
        }

        public void render(PoseStack poseStack, Spell1Entity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
            RenderType renderType = this.getRenderType(null);
            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
        }
    };

    public Spell1Renderer(EntityRendererProvider.Context context) {
        super(context, (GeoModel)new Spell1Model());
        this.addRenderLayer(this.glowLayer);
    }
}

