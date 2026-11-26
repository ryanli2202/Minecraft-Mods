from pathlib import Path
import re,json
R=Path(__file__).resolve().parents[1];S=R.parent/'deobfuscated/dawncraft_mobs-1.18-1.3.9a/src/main/java/net/mcreator/simplemobs';O=R/'src/main/java/dev/deepnight/source/dawn'
index={p.stem:p for p in S.rglob('*.java')}
entities={p.stem for p in (O/'entity').glob('*.java')}
pending=[n.removesuffix('Entity')+'Renderer' for n in entities];seen=set()
particles=(S/'init/SimpleMobsModParticles.java').read_text()
particle_reg=(O/'init/SimpleMobsModParticleTypes.java').read_text()
particle_lines=[]
for field,cls in re.findall(r'SimpleMobsModParticleTypes\.(\w+)\.get\(\), (\w+)::provider',particles):
 if field+'=' in particle_reg:pending.append(cls);particle_lines.append(f'e.registerSpriteSet((net.minecraft.core.particles.SimpleParticleType)dev.deepnight.source.dawn.init.SimpleMobsModParticleTypes.{field}.get(),dev.deepnight.source.dawn.client.particle.{cls}::provider);')
while pending:
 name=pending.pop()
 if name in seen or name not in index:continue
 seen.add(name);t=index[name].read_text()
 pending.extend(re.findall(r'import net.mcreator.simplemobs.client(?:.model|.renderer)?\.(\w+);',t))
 t=t.replace('net.mcreator.simplemobs','dev.deepnight.source.dawn')
 t=t.replace('com.mojang.math.Matrix3f','org.joml.Matrix3f').replace('com.mojang.math.Matrix4f','org.joml.Matrix4f').replace('com.mojang.math.Vector3f','com.mojang.math.Axis').replace('Vector3f.', 'Axis.')
 t=t.replace('.level.', '.level().')
 t=t.replace('software.bernie.geckolib3.core.IAnimatable','software.bernie.geckolib.animatable.GeoEntity').replace('IAnimatable','GeoEntity')
 t=t.replace('software.bernie.geckolib3.core.event.predicate.AnimationEvent','software.bernie.geckolib.core.animation.AnimationState').replace('AnimationEvent','AnimationState')
 t=t.replace('software.bernie.geckolib3.core.processor.IBone','software.bernie.geckolib.cache.object.GeoBone').replace('IBone','GeoBone')
 t=t.replace('software.bernie.geckolib3.model.AnimatedGeoModel','software.bernie.geckolib.model.GeoModel').replace('AnimatedGeoModel','GeoModel')
 t=t.replace('software.bernie.geckolib3.model.provider.data.EntityModelData','software.bernie.geckolib.model.data.EntityModelData')
 t=t.replace('getModelLocation(', 'getModelResource(').replace('getTextureLocation(', 'getTextureResource(').replace('getAnimationFileLocation(', 'getAnimationResource(')
 t=t.replace('setLivingAnimations(', 'setCustomAnimations(').replace('Integer uniqueID','long uniqueID').replace('(GeoEntity)entity','entity')
 t=re.sub(r'\(EntityModelData\)(\w+)\.getExtraDataOfType\(EntityModelData.class\).get\(0\)',r'\1.getData(software.bernie.geckolib.constant.DataTickets.ENTITY_MODEL_DATA)',t)
 t=t.replace('.headPitch ','.headPitch() ').replace('.netHeadYaw ','.netHeadYaw() ')
 t=t.replace('.setRotationX(','.setRotX(').replace('.setRotationY(','.setRotY(').replace('.setRotationZ(','.setRotZ(')
 t=t.replace('.getRotationX()', '.getRotX()').replace('.getRotationY()', '.getRotY()').replace('.getRotationZ()', '.getRotZ()')
 t=t.replace('software.bernie.geckolib3.geo.render.built.GeoBone','software.bernie.geckolib.cache.object.GeoBone')
 t=t.replace('software.bernie.geckolib3.geo.render.built.GeoModel','software.bernie.geckolib.cache.object.BakedGeoModel')
 t=t.replace('software.bernie.geckolib3.model.provider.GeoModelProvider','software.bernie.geckolib.model.GeoModel').replace('GeoModelProvider','GeoModel')
 t=t.replace('software.bernie.geckolib3.renderers.geo.GeoEntityRenderer','dev.deepnight.source.dawn.client.LegacyGeoRenderer').replace('extends GeoEntityRenderer<','extends LegacyGeoRenderer<')
 t=t.replace('software.bernie.geckolib3.renderers.geo.GeoLayerRenderer','software.bernie.geckolib.renderer.layer.GeoRenderLayer').replace('GeoLayerRenderer','GeoRenderLayer')
 t=re.sub(r'import software.bernie.geckolib3.renderers.geo.IGeoRenderer;','',t)
 is_geo='extends LegacyGeoRenderer<' in t
 if is_geo:t=t.replace('.addLayer(','.addRenderLayer(')
 t=t.replace('GeoModel model,','BakedGeoModel model,')
 # Retain the complete per-entity glow/beam/rotation code, adapting its call signature.
 if is_geo:t=re.sub(r'public void render\(\s*PoseStack poseStack,\s*MultiBufferSource buffer,\s*int packedLight,\s*(\w+) entity,\s*float limbSwing,\s*float limbSwingAmount,\s*float partialTicks,\s*float ageInTicks,\s*float netHeadYaw,\s*float headPitch\s*\)',r'public void render(PoseStack poseStack, \1 entity, software.bernie.geckolib.cache.object.BakedGeoModel bakedModel, RenderType originalType, MultiBufferSource buffer, VertexConsumer originalBuffer, float partialTicks, int packedLight, int packedOverlay)',t)
 t=t.replace('this.getEntityModel()', 'this.getGeoModel()')
 t=re.sub(r'this.getRenderer\(\)\s*.render\(\s*provider.getModel\(provider.getModelResource\((?:\(Object\))?entity\)\),.*?\);', 'this.getRenderer().reRender(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(entity)),poseStack,buffer,entity,renderType,ivertexbuilder,partialTicks,15728640,OverlayTexture.NO_OVERLAY,1,1,1,1);',t,flags=re.S)
 t=t.replace('(IGeoRenderer)this','this').replace('model.topLevelBones','model.topLevelBones()')
 if is_geo:t=t.replace('this.getRenderType(null)', 'this.getRenderer().getRenderType(entity,this.getRenderer().getTextureLocation(entity),buffer,partialTicks)')
 t=t.replace('EntityModelData extraData = customPredicate.getData(', 'EntityModelData extraData = (EntityModelData)customPredicate.getData(')
 t=re.sub(r'GeoBone (\w+) = this.getAnimationProcessor\(\).getBone',r'var \1 = this.getAnimationProcessor().getBone',t)
 t=re.sub(r'new HumanoidModel\(context.bakeLayer\(ModelLayers.PLAYER_OUTER_ARMOR\)\)\)', 'new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),context.getModelManager())',t)
 if 'BakedGeoModel model' in t and 'import software.bernie.geckolib.cache.object.BakedGeoModel;' not in t:t=t.replace('import ', 'import software.bernie.geckolib.cache.object.BakedGeoModel;\nimport ',1)
 # Vanilla renderers still use getTextureLocation (only Gecko models changed name).
 if name.endswith('Renderer'):t=t.replace('getTextureResource(', 'getTextureLocation(')
 p=O/index[name].relative_to(S);p.parent.mkdir(parents=True,exist_ok=True);p.write_text(t)
renderer_reg=(S/'init/SimpleMobsModEntityRenderers.java').read_text()
lines=[]
bosses=set(json.loads((R/'tools/java_creature_selection.json').read_text())['dawn']['boss'].split())
for field,cls in re.findall(r'event.registerEntityRenderer\(SimpleMobsModEntities\.(\w+).get\(\), (\w+)::new\)',renderer_reg):
 if cls in seen:lines.append(f'e.registerEntityRenderer(dev.deepnight.source.dawn.init.SimpleMobsModEntities.{field}.get(),dev.deepnight.source.dawn.client.renderer.{cls}::new);')
mb=R/'src/main/java/dev/deepnight/source/mobosses'
for p in (mb/'entity/client/renderer').glob('*.java'):
 name=p.stem.removesuffix('Renderer');field=re.sub(r'(?<!^)(?=[A-Z])','_',name).upper()
 registry=(mb/'MoBosses.java').read_text()
 m=re.search(r'public static final .*? (\w+)=.*?EntityType<'+name+r'Entity>',registry)
 # Registry fields are read from declared generic types.
 m=re.search(r'(?:Supplier|RegistryObject)<EntityType<'+name+r'Entity>>\s+(\w+)\s*=',registry)
 if m:lines.append(f'e.registerEntityRenderer(dev.deepnight.source.mobosses.MoBosses.{m[1]}.get(),dev.deepnight.source.mobosses.entity.client.renderer.{p.stem}::new);')
layers=[]
for name in seen:
 p=O/index[name].relative_to(S);t=p.read_text()
 if 'ModelLayerLocation LAYER_LOCATION' in t:layers.append(f'e.registerLayerDefinition({t.split("package ")[1].split(";")[0]}.{name}.LAYER_LOCATION,{t.split("package ")[1].split(";")[0]}.{name}::createBodyLayer);')
(O/'client/SourceRenderers.java').write_text('''package dev.deepnight.source.dawn.client;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
@Mod.EventBusSubscriber(modid="deepnight",bus=Mod.EventBusSubscriber.Bus.MOD,value=Dist.CLIENT)
public final class SourceRenderers {
public static void renderers(net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers e){
'''+ '\n'.join(lines)+'''
}
@SubscribeEvent public static void layers(net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions e){
'''+ '\n'.join(layers)+'''
}
@SubscribeEvent public static void particles(net.minecraftforge.client.event.RegisterParticleProvidersEvent e){
'''+ '\n'.join(particle_lines)+'\n}\n}\n')
p=R/'src/main/java/dev/deepnight/client/ClientEvents.java';t=p.read_text().replace('DeepNight.PETS.values().forEach(t->e.registerEntityRenderer(t.get(),NightRenderer::new));','DeepNight.PETS.values().forEach(t->e.registerEntityRenderer(t.get(),NightRenderer::new));\n        dev.deepnight.source.dawn.client.SourceRenderers.renderers(e);');p.write_text(t)
print('Ported',len(seen),'source renderer/model/particle classes;',len(lines),'renderer bindings')
