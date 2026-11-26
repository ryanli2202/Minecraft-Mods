package dev.deepnight.source.dawn.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import com.mojang.math.Axis;
import javax.annotation.Nullable;
import dev.deepnight.source.dawn.client.MossGolemModel;
import dev.deepnight.source.dawn.entity.MossGolemEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import dev.deepnight.source.dawn.client.LegacyGeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class MossGolemRenderer extends LegacyGeoRenderer<MossGolemEntity> {
   private static final ResourceLocation BEAM_LOCATION = new ResourceLocation("simple_mobs:textures/entities/laser_beam.png");
   private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(BEAM_LOCATION);
   protected final RenderType glowTexture = RenderType.eyes(new ResourceLocation("simple_mobs", "textures/brute_eye.png"));
   protected final GeoRenderLayer<MossGolemEntity> glowLayer = new GeoRenderLayer<MossGolemEntity>(this) {
      public RenderType getRenderType(ResourceLocation texture) {
         return MossGolemRenderer.this.glowTexture;
      }

      public void render(PoseStack poseStack, MossGolemEntity entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay) {
         if (entity.tickCount > 1) {
            RenderType renderType = this.getRenderType(null);
            VertexConsumer ivertexbuilder = buffer.getBuffer(renderType);
            GeoModel provider = this.getGeoModel();
            this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);
         }
      }
   };

   public MossGolemRenderer(Context context) {
      super(context, new MossGolemModel());
   }

   public boolean shouldRender(MossGolemEntity golem, Frustum frustum, double p_114838_, double p_114839_, double p_114840_) {
      if (super.shouldRender(golem, frustum, p_114838_, p_114839_, p_114840_)) {
         return true;
      }

      if (golem.hasActiveAttackTarget()) {
         LivingEntity livingentity = golem.getActiveAttackTarget();
         if (livingentity != null) {
            Vec3 vec3 = this.getPosition(livingentity, livingentity.getBbHeight() * 0.5, 1.0F);
            Vec3 vec31 = this.getPosition(golem, golem.getEyeHeight(), 1.0F);
            return frustum.isVisible(new AABB(vec31.x, vec31.y, vec31.z, vec3.x, vec3.y, vec3.z));
         }
      }

      return false;
   }

   private Vec3 getPosition(LivingEntity entity, double p_114804_, float p_114805_) {
      double d0 = Mth.lerp(p_114805_, entity.xOld, entity.getX());
      double d1 = Mth.lerp(p_114805_, entity.yOld, entity.getY()) + p_114804_;
      double d2 = Mth.lerp(p_114805_, entity.zOld, entity.getZ());
      return new Vec3(d0, d1, d2);
   }

   protected float getDeathMaxRotation(MossGolemEntity entityLivingBaseIn) {
      return 0.0F;
   }

   public void render(
      BakedGeoModel model,
      MossGolemEntity animatable,
      float partialTicks,
      RenderType type,
      PoseStack matrixStackIn,
      @Nullable MultiBufferSource renderTypeBuffer,
      @Nullable VertexConsumer vertexBuilder,
      int packedLightIn,
      int packedOverlayIn,
      float red,
      float green,
      float blue,
      float alpha
   ) {
      this.renderEarly(animatable, matrixStackIn, partialTicks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
      if (renderTypeBuffer != null) {
         vertexBuilder = renderTypeBuffer.getBuffer(type);
      }

      this.renderRecursively(
         (GeoBone)model.topLevelBones().get(0),
         matrixStackIn,
         vertexBuilder,
         packedLightIn,
         OverlayTexture.pack(0, OverlayTexture.v(animatable.hurtTime > 0 && animatable.getPersistentData().getBoolean("Blocking"))),
         red,
         green,
         blue,
         alpha
      );
   }

   public void render(MossGolemEntity entity, float p_114830_, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      if (entity.tickCount > 1) {
         super.render(entity, p_114830_, partialTicks, poseStack, bufferSource, packedLight);
      }

      this.renderLaser(entity, partialTicks, poseStack, bufferSource);
   }

   private void renderLaser(MossGolemEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource) {
      LivingEntity livingentity = entity.getActiveAttackTarget();
      if (livingentity != null) {
         float f = entity.getAttackAnimationScale(partialTicks);
         float f1 = (float)entity.level().getGameTime() + partialTicks;
         float f2 = f1 * 0.5F % 1.0F;
         float f3 = entity.getEyeHeight();
         poseStack.pushPose();
         poseStack.translate(0.0, f3, 0.0);
         Vec3 vec3 = this.getPosition(livingentity, livingentity.getBbHeight() * 0.5, partialTicks);
         Vec3 vec31 = this.getPosition(entity, f3, partialTicks);
         Vec3 vec32 = vec3.subtract(vec31);
         float f4 = (float)(vec32.length() + 1.0);
         vec32 = vec32.normalize();
         float f5 = (float)Math.acos(vec32.y);
         float f6 = (float)Math.atan2(vec32.z, vec32.x);
         poseStack.mulPose(Axis.YP.rotationDegrees(((float) (Math.PI / 2) - f6) * (180.0F / (float)Math.PI)));
         poseStack.mulPose(Axis.XP.rotationDegrees(f5 * (180.0F / (float)Math.PI)));
         float f7 = f1 * 0.05F * -1.5F;
         float f8 = f * f;
         int j = 64 + (int)(f8 * 191.0F);
         int k = 32 + (int)(f8 * 191.0F);
         int l = 128 - (int)(f8 * 64.0F);
         float f11 = Mth.cos(f7 + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
         float f12 = Mth.sin(f7 + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
         float f13 = Mth.cos(f7 + (float) (Math.PI / 4)) * 0.282F;
         float f14 = Mth.sin(f7 + (float) (Math.PI / 4)) * 0.282F;
         float f15 = Mth.cos(f7 + ((float) Math.PI * 5.0F / 4.0F)) * 0.282F;
         float f16 = Mth.sin(f7 + ((float) Math.PI * 5.0F / 4.0F)) * 0.282F;
         float f17 = Mth.cos(f7 + ((float) Math.PI * 7.0F / 4.0F)) * 0.282F;
         float f18 = Mth.sin(f7 + ((float) Math.PI * 7.0F / 4.0F)) * 0.282F;
         float f19 = Mth.cos(f7 + (float) Math.PI) * 0.2F;
         float f20 = Mth.sin(f7 + (float) Math.PI) * 0.2F;
         float f21 = Mth.cos(f7 + 0.0F) * 0.2F;
         float f22 = Mth.sin(f7 + 0.0F) * 0.2F;
         float f23 = Mth.cos(f7 + (float) (Math.PI / 2)) * 0.2F;
         float f24 = Mth.sin(f7 + (float) (Math.PI / 2)) * 0.2F;
         float f25 = Mth.cos(f7 + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
         float f26 = Mth.sin(f7 + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
         float f29 = -1.0F + f2;
         float f30 = f4 * 2.5F + f29;
         VertexConsumer vertexconsumer = bufferSource.getBuffer(BEAM_RENDER_TYPE);
         Pose posestack$pose = poseStack.last();
         Matrix4f matrix4f = posestack$pose.pose();
         Matrix3f matrix3f = posestack$pose.normal();
         vertex(vertexconsumer, matrix4f, matrix3f, f19, f4, f20, j, k, l, 0.4999F, f30);
         vertex(vertexconsumer, matrix4f, matrix3f, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
         vertex(vertexconsumer, matrix4f, matrix3f, f21, 0.0F, f22, j, k, l, 0.0F, f29);
         vertex(vertexconsumer, matrix4f, matrix3f, f21, f4, f22, j, k, l, 0.0F, f30);
         vertex(vertexconsumer, matrix4f, matrix3f, f23, f4, f24, j, k, l, 0.4999F, f30);
         vertex(vertexconsumer, matrix4f, matrix3f, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
         vertex(vertexconsumer, matrix4f, matrix3f, f25, 0.0F, f26, j, k, l, 0.0F, f29);
         vertex(vertexconsumer, matrix4f, matrix3f, f25, f4, f26, j, k, l, 0.0F, f30);
         float f31 = 0.0F;
         if (entity.tickCount % 2 == 0) {
            f31 = 0.5F;
         }

         vertex(vertexconsumer, matrix4f, matrix3f, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
         vertex(vertexconsumer, matrix4f, matrix3f, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
         vertex(vertexconsumer, matrix4f, matrix3f, f17, f4, f18, j, k, l, 1.0F, f31);
         vertex(vertexconsumer, matrix4f, matrix3f, f15, f4, f16, j, k, l, 0.5F, f31);
         poseStack.popPose();
      }
   }

   private static void vertex(
      VertexConsumer vertexConsumer,
      Matrix4f matrix4f,
      Matrix3f matrix3f,
      float p_253994_,
      float p_254492_,
      float p_254474_,
      int p_254080_,
      int p_253655_,
      int p_254133_,
      float p_254233_,
      float p_253939_
   ) {
      vertexConsumer.vertex(matrix4f, p_253994_, p_254492_, p_254474_)
         .color(228, 46, 255, 255)
         .uv(p_254233_, p_253939_)
         .overlayCoords(OverlayTexture.NO_OVERLAY)
         .uv2(15728880)
         .normal(matrix3f, 0.0F, 1.0F, 0.0F)
         .endVertex();
   }
}
