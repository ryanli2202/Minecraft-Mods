package dev.deepnightguide.compat;
import dev.deepnightguide.client.JeiBridge;
import mezz.jei.api.*;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
@JeiPlugin
public final class GuideJeiPlugin implements IModPlugin {
    @Override public ResourceLocation getPluginUid(){return new ResourceLocation("deepnightguide","guide");}
    @Override public void onRuntimeAvailable(IJeiRuntime runtime) {
        JeiBridge.recipes=stack->runtime.getRecipesGui().show(runtime.getJeiHelpers().getFocusFactory().createFocus(RecipeIngredientRole.OUTPUT,VanillaTypes.ITEM_STACK,stack));
        JeiBridge.uses=stack->runtime.getRecipesGui().show(runtime.getJeiHelpers().getFocusFactory().createFocus(RecipeIngredientRole.INPUT,VanillaTypes.ITEM_STACK,stack));
    }
    @Override public void onRuntimeUnavailable(){JeiBridge.recipes=null;JeiBridge.uses=null;}
}
