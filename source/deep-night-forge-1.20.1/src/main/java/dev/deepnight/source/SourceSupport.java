package dev.deepnight.source;
import java.util.Comparator;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
public final class SourceSupport {
 public static Comparator<Entity> byDistance(Vec3 center){return Comparator.comparingDouble(e->e.distanceToSqr(center));}
}
