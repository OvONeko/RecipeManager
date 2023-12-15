package cx.rain.mc.morepotions.brewing;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class BrewingManager {
    private final Plugin plugin;

    private final Map<NamespacedKey, EffectEntry> effects = new HashMap<>();
    private final Map<NamespacedKey, PotionEntry> potions = new HashMap<>();
    private final Map<NamespacedKey, RecipeEntry> recipes = new HashMap<>();

    public BrewingManager(Plugin plugin) {
        this.plugin = plugin;

//        // qyl27: An default value to avoid some mistake.
//        var emptyPotion = new PotionEntry(new NamespacedKey(MorePotions.getInstance(), "empty"), List.of());
//        potions.put(emptyPotion.getId(), emptyPotion);
    }

    public void addEffect(@Nonnull EffectEntry entry) {
        effects.put(entry.id(), entry);
    }

    public void addPotion(@Nonnull PotionEntry entry) {
        potions.put(entry.getId(), entry);
    }

    public void addRecipe(@Nonnull RecipeEntry entry) {
        recipes.put(entry.getId(), entry);
    }

    public @Nullable EffectEntry getEffect(@Nonnull NamespacedKey effectId) {
        return effects.get(effectId);
    }

    public @Nullable PotionEntry getPotion(@Nonnull NamespacedKey potionId) {
        return potions.get(potionId);
    }

    public @Nullable RecipeEntry getRecipe(@Nonnull NamespacedKey recipeId) {
        return recipes.get(recipeId);
    }
}
