package dev.travellerscompanions;

import java.util.*;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;

/** Uses the installed weapon attributes, including third-party datapack overrides. */
public final class CombatBridge {
    public record Strike(String animation,int duration,int windup,double range,double multiplier) {}
    public static Strike strike(CompanionEntity e,int combo){
        var attributes=WeaponRegistry.getAttributes(e.getMainHandItem());
        double speed=4;
        for(var m:e.getMainHandItem().getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_SPEED))speed+=m.getAmount();
        int duration=(int)Math.max(10,Math.min(60,20/Math.max(.35,speed)));
        if(attributes!=null&&attributes.attacks()!=null){
            var attacks=Arrays.stream(attributes.attacks()).filter(a->allowed(a,e)).toList();
            if(!attacks.isEmpty()){
                var a=attacks.get(Math.floorMod(combo,attacks.size()));
                return new Strike(a.animation(),duration,Math.max(1,(int)(duration*a.upswing())),Math.max(2,Math.min(7,attributes.attackRange())),a.damageMultiplier());
            }
        }
        return new Strike("bettercombat:one_handed_slash_horizontal_right",duration,Math.max(1,duration/2),2.7,1);
    }
    private static boolean allowed(WeaponAttributes.Attack a,CompanionEntity e){
        if(a.conditions()==null)return true;
        for(var c:a.conditions()){
            boolean ok=switch(c){
                case NOT_DUAL_WIELDING,MAIN_HAND_ONLY->true;
                case DUAL_WIELDING_ANY,DUAL_WIELDING_SAME,DUAL_WIELDING_SAME_CATEGORY,OFF_HAND_ONLY->false;
                case NO_OFFHAND_ITEM->e.getOffhandItem().isEmpty();
                case OFF_HAND_SHIELD->e.getOffhandItem().getItem() instanceof ShieldItem;
                case MOUNTED->e.isPassenger();case NOT_MOUNTED->!e.isPassenger();
            };if(!ok)return false;
        }return true;
    }
}
