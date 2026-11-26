package dev.nordui.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import com.mojang.blaze3d.systems.RenderSystem;

public final class NordHud {
    public static void render(ForgeGui gui,GuiGraphics g,float partial,int w,int h) {
        var mc=Minecraft.getInstance();var p=mc.player;
        if(p==null||mc.options.hideGui||!ClientConfig.active())return;
        if(ClientConfig.COMPASS.get()&&!(ClientConfig.HUD_OWNER.get()==ClientConfig.HudOwner.AUTO&&dev.nordui.compat.UiCompat.rpgHud()))compass(g,p,w,partial);
        if(!ClientConfig.ownsHud()||p.isSpectator())return;
        if(!p.isCreative()) {
            int center=Math.min(190,(w-42)/3),side=Math.min(142,(w-48)/3),y=Math.max(90,h-25-dev.nordui.compat.UiCompat.hudLift());
            // Skyrim-style positions; these remain Minecraft health, XP and food.
            Theme.ornateBar(g,w/2-center/2,y,center,p.getHealth()/p.getMaxHealth(),Theme.HEALTH);
            Theme.centered(g,"HEALTH  "+Mth.ceil(p.getHealth())+" / "+Mth.ceil(p.getMaxHealth())+(p.getAbsorptionAmount()>0?"  +"+Mth.ceil(p.getAbsorptionAmount()):""),w/2,y-8,Theme.WHITE);
            Theme.ornateBar(g,8,y,side,p.experienceProgress,Theme.XP);
            Theme.text(g,"LEVEL "+p.experienceLevel+"    /    "+Math.round(p.experienceProgress*100)+"%",19,y-8,Theme.WHITE);
            Theme.ornateBar(g,w-side-8,y,side,p.getFoodData().getFoodLevel()/20f,Theme.FOOD);
            Theme.centered(g,"HUNGER  "+p.getFoodData().getFoodLevel()+" / 20",w-side/2-8,y-8,Theme.WHITE);
            if(p.getArmorValue()>0)Theme.centered(g,"ARMOUR "+p.getArmorValue(),w/2,y+17,Theme.MUTED);
            int extraY=y-27;
            if(p.isUnderWater()||p.getAirSupply()<p.getMaxAirSupply()){
                Theme.ornateBar(g,w/2-center/2,extraY,center,(float)p.getAirSupply()/p.getMaxAirSupply(),Theme.XP);
                Theme.centered(g,"BREATH",w/2,extraY-8,Theme.WHITE);extraY-=27;
            }
            if(p.getVehicle() instanceof net.minecraft.world.entity.LivingEntity mount){
                Theme.ornateBar(g,w/2-center/2,extraY,center,mount.getHealth()/mount.getMaxHealth(),Theme.HEALTH);
                Theme.centered(g,"MOUNT  "+Mth.ceil(mount.getHealth())+" / "+Mth.ceil(mount.getMaxHealth()),w/2,extraY-8,Theme.WHITE);extraY-=27;
            }
            if(p.getJumpRidingScale()>0){
                Theme.ornateBar(g,w/2-center/2,extraY,center,p.getJumpRidingScale(),Theme.XP);
                Theme.centered(g,"JUMP",w/2,extraY-8,Theme.WHITE);
            }
        }
        if(ClientConfig.HOTBAR.get())hotbar(g,p,w,h,partial);
        if(dev.nordui.compat.IronsCompat.loaded())dev.nordui.compat.irons.IronsClient.mana(g,w,h);
    }
    private static void compass(GuiGraphics g,Player p,int w,float partial) {
        int cw=Math.min(320,w-44),x=(w-cw)/2,y=Mth.clamp(dev.nordui.compat.UiCompat.compassY(),4,Minecraft.getInstance().getWindow().getGuiScaledHeight()-30);
        g.fillGradient(x,y-3,x+cw,y+22,0x26070C11,0x75070C11);Theme.rule(g,x,y+18,cw);
        float heading=Mth.positiveModulo(p.getViewYRot(partial)+180,360);
        String[] directions={"N","NE","E","SE","S","SW","W","NW"};
        g.enableScissor(x+5,y-1,x+cw-5,y+18);
        for(int i=0;i<24;i++){
            float delta=Mth.wrapDegrees(i*15-heading);int px=w/2+Math.round(delta*cw/150f);
            if(Math.abs(delta)>76)continue;
            if(i%3==0)Theme.centered(g,directions[i/3],px,y+2,i%6==0?Theme.WHITE:Theme.MUTED);
            else g.vLine(px,y+8,y+12,0xAAABB3B1);
        }
        g.disableScissor();RenderSystem.enableBlend();g.blit(Theme.MARKER,w/2-4,y+16,0,0,9,7,9,7);RenderSystem.disableBlend();
    }
    private static void hotbar(GuiGraphics g,Player p,int w,int h,float partial) {
        boolean compact=h<300;int ox=compact?w-78:w-33,oy=compact?h-121:(h-198)/2;
        ox=Mth.clamp(ox+ClientConfig.HOTBAR_X.get(),10,w-(compact?71:24));
        oy=Mth.clamp(oy+ClientConfig.HOTBAR_Y.get(),4,h-(compact?72:226));
        for(int i=0;i<9;i++){
            int x=ox+(compact?i%3*23:0),y=oy+(compact?i/3*23:i*22);
            RenderSystem.enableBlend();g.blit(i==p.getInventory().selected?Theme.QUICK_SELECTED:Theme.QUICK_SLOT,x,y,0,0,23,23,23,23);RenderSystem.disableBlend();
            var stack=p.getInventory().getItem(i);g.renderItem(stack,x+4,y+3);g.renderItemDecorations(Minecraft.getInstance().font,stack,x+4,y+3);
            if(!compact)Theme.text(g,Integer.toString(i+1),x-8,y+7,i==p.getInventory().selected?Theme.GOLD:Theme.MUTED);
        }
        if(!p.getOffhandItem().isEmpty()){
            int x=compact?ox-24:ox,y=compact?oy+46:oy+202;
            RenderSystem.enableBlend();g.blit(Theme.QUICK_SLOT,x,y,0,0,23,23,23,23);RenderSystem.disableBlend();g.renderItem(p.getOffhandItem(),x+4,y+3);g.renderItemDecorations(Minecraft.getInstance().font,p.getOffhandItem(),x+4,y+3);
        }
        if(Minecraft.getInstance().options.attackIndicator().get()==net.minecraft.client.AttackIndicatorStatus.HOTBAR){
            int x=ox-3,y=oy;g.fill(x,y,x+2,y+20,0xAA10151A);int fill=Math.round(20*p.getAttackStrengthScale(partial));g.fill(x,y+20-fill,x+2,y+20,0xFFC5C9C0);
        }
    }
}
