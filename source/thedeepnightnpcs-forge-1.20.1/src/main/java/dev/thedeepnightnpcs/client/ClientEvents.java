package dev.thedeepnightnpcs.client;

import dev.thedeepnightnpcs.DeepNightNpcs;
import dev.thedeepnightnpcs.entity.NpcEntity;
import dev.thedeepnightnpcs.network.NpcNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=DeepNightNpcs.ID, bus=Mod.EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
public final class ClientEvents {
    @SubscribeEvent public static void renderers(EntityRenderersEvent.RegisterRenderers event) {
        for (var type : DeepNightNpcs.allNpcs()) event.registerEntityRenderer(type.get(), context -> new NpcRenderer(context,
            java.util.Set.of("sera","mara","elian","iona","tamsin","ysolde").contains(type.getId().getPath())));
    }
    public static void open(NpcNetwork.View view) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || !(mc.level.getEntity(view.entityId()) instanceof NpcEntity)) return;
        if (mc.screen instanceof DialogueScreen screen && screen.entityId == view.entityId()) screen.update(view);
        else mc.setScreen(new DialogueScreen(view));
    }
    public static final class NpcRenderer extends MobRenderer<NpcEntity, PlayerModel<NpcEntity>> {
        public NpcRenderer(EntityRendererProvider.Context context, boolean slim) {
            super(context, new PlayerModel<>(context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim), 0.45F);
            addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        }
        @Override public void render(NpcEntity npc, float yaw, float partial, com.mojang.blaze3d.vertex.PoseStack pose,
                                     net.minecraft.client.renderer.MultiBufferSource buffers, int light) {
            model.rightArmPose = npc.isGuard() ? HumanoidModel.ArmPose.ITEM : HumanoidModel.ArmPose.EMPTY;
            model.leftArmPose = npc.isGuard() ? (npc.isUsingItem() ? HumanoidModel.ArmPose.BLOCK : HumanoidModel.ArmPose.ITEM) : HumanoidModel.ArmPose.EMPTY;
            super.render(npc, yaw, partial, pose, buffers, light);
        }
        @Override public ResourceLocation getTextureLocation(NpcEntity npc) {
            return new ResourceLocation(DeepNightNpcs.ID, "textures/entity/" + (npc.isGuard() ? "spireguard" : npc.kind()) + ".png");
        }
    }
}
