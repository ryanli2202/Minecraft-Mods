package dev.deepnight.qa;
import com.mojang.blaze3d.vertex.PoseStack;
import software.bernie.geckolib.cache.object.*;
import software.bernie.geckolib.util.RenderUtils;
import dev.deepnight.*;
import java.util.*;
import java.nio.file.*;
/** Measure the actual animated Java mesh to verify Bedrock import transforms. */
final class ModelBounds {
    private static final Map<String,float[]> RESULTS=new TreeMap<>();
    static void record(Species species,BakedGeoModel model){
        float[] b={Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY,Float.POSITIVE_INFINITY,Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY,Float.NEGATIVE_INFINITY};
        PoseStack p=new PoseStack();for(GeoBone bone:model.topLevelBones())walk(p,bone,b);RESULTS.put(species.id,b);
    }
    private static void walk(PoseStack p,GeoBone bone,float[] b){
        if(bone.getScaleX()==0||bone.getScaleY()==0||bone.getScaleZ()==0)return;
        p.pushPose();RenderUtils.prepMatrixForBone(p,bone);
        if(!bone.isHidden())for(GeoCube cube:bone.getCubes()){
            p.pushPose();RenderUtils.translateToPivotPoint(p,cube);RenderUtils.rotateMatrixAroundCube(p,cube);RenderUtils.translateAwayFromPivotPoint(p,cube);
            for(GeoQuad q:cube.quads())if(q!=null)for(GeoVertex v:q.vertices()){
                var at=p.last().pose().transformPosition(new org.joml.Vector3f(v.position()));
                b[0]=Math.min(b[0],at.x);b[1]=Math.min(b[1],at.y);b[2]=Math.min(b[2],at.z);
                b[3]=Math.max(b[3],at.x);b[4]=Math.max(b[4],at.y);b[5]=Math.max(b[5],at.z);
            }p.popPose();
        }
        if(!bone.isHidingChildren())for(GeoBone child:bone.getChildBones())walk(p,child,b);p.popPose();
    }
    static void save() throws Exception{Files.writeString(Path.of("../verification/render-bounds.json"),new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(RESULTS));}
}
