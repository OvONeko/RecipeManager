package cx.rain.mc.recipemanager.utility;

import cx.rain.mc.recipemanager.RecipeManager;
import cx.rain.mc.recipemanager.api.data.EffectEntry;
import cx.rain.mc.recipemanager.api.data.PotionEntry;

import java.util.ArrayList;
import java.util.List;

public class PotionHelper {
    public static List<EffectEntry> getEffects(PotionEntry potion) {
        var list = new ArrayList<EffectEntry>();

        for (var id : potion.getEffects()) {
            var effect = RecipeManager.getInstance().getBrewingManager().getEffect(id);

            if (effect != null) {
                list.add(effect);
            } else {
                throw new RuntimeException("Effect '" + id + "' used by '" + potion.getId() + "' was not registered!");
            }
        }

        return list;
    }
}
