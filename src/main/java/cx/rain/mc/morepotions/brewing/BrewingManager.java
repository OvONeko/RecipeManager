package cx.rain.mc.morepotions.brewing;

import cx.rain.mc.morepotions.brewing.config.EffectEntry;
import cx.rain.mc.morepotions.brewing.config.PotionEntry;
import cx.rain.mc.morepotions.brewing.config.RecipeEntry;
import cx.rain.mc.morepotions.utility.Pair;
import cx.rain.mc.morepotions.utility.PotionItemStackHelper;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private List<RecipeEntry> getRecipesByIngredient(Material ingredient) {
        return recipes.values().stream()
                .filter(r -> r.getIngredient().equals(ingredient))
                .toList();
    }

//    public List<RecipeEntry> getRecipesByBasePotion(NamespacedKey basePotion, PotionType type) {
//        return recipes.values().stream()
//                .filter(r -> r.getBasePotion().equals(basePotion) && r.getBaseType().equals(type))
//                .toList();
//    }

    // Fixme: qyl27: According to vanilla, the recipes with same ingredient should all apply.
    public @Nullable RecipeEntry getRecipe(BrewerInventory brewer) {
        var ingredient = brewer.getIngredient();

        if (ingredient == null) {
            return null;
        }
        var candidateRecipes = getRecipesByIngredient(ingredient.getType());

        if (candidateRecipes.isEmpty()) {
            return null;
        }

        List<Pair<PotionCategory, NamespacedKey>> bases = new ArrayList<>();
        for (var i = 0; i < 3; i++) {
            var base = brewer.getItem(i);
            var id = PotionItemStackHelper.getPotionId(base, PotionCategory.CUSTOM);
            if (id != null) {
                bases.add(new Pair<>(PotionCategory.CUSTOM, id));
                continue;
            }

            id = PotionItemStackHelper.getPotionId(base, PotionCategory.VANILLA);
            if (id != null) {
                bases.add(new Pair<>(PotionCategory.VANILLA, id));
            }
        }

        if (bases.isEmpty()) {
            return null;
        }

        for (var recipe : candidateRecipes) {
            if (bases.contains(new Pair<>(recipe.getBaseCategory(), recipe.getBasePotion()))) {
                return recipe;
            }
        }

        return null;
    }
}
