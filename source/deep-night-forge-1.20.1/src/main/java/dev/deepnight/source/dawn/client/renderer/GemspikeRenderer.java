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
import dev.deepnight.source.dawn.client.GemspikeModel;
import dev.deepnight.source.dawn.entity.GemspikeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class GemspikeRenderer
extends LegacyGeoRenderer<GemspikeEntity> {
    protected final RenderType glowTexture = RenderType.eyes(new ResourceLocation("simple_mobs", "textures/gem_glow.png"));
    protected final GeoRenderLayer<GemspikeEntity> glowLayer = new GeoRenderLayer<GemspikeEntity>(this){

        public RenderType getRenderType(ResourceLocation texture) {
            return GemspikeRenderer.this.glowTexture;
        }

        public void render(PoseStack poseStack, GemspikeEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
            RenderType renderType = this.getRenderType(null);
            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
        }
    };
    protected final GeoRenderLayer<GemspikeEntity> transparentLayer = new GeoRenderLayer<GemspikeEntity>(this){

        public void render(PoseStack poseStack, GemspikeEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
            RenderType renderType = this.getRenderer().getRenderType(entity,this.getRenderer().getTextureLocation(entity),buffer,partialTicks);
            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
        }
    };

    public GemspikeRenderer(EntityRendererProvider.Context context) {
        super(context, (GeoModel)new GemspikeModel());
        this.addRenderLayer(this.glowLayer);
    }

    protected float getDeathMaxRotation(GemspikeEntity entityLivingBaseIn) {
        return 0.0f;
    }
}

