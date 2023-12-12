package cx.rain.mc.morepotions.config;

import cx.rain.mc.morepotions.MorePotions;
import cx.rain.mc.morepotions.brewing.PotionEntry;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CustomBrewingRecipes implements ConfigurationSerializable {
    private Map<NamespacedKey, BrewingEntry> recipes = new HashMap<>();
    private Map<NamespacedKey, PotionEntry> potions = new HashMap<>();

    public CustomBrewingRecipes() {
        potions.put(PotionEntry.EMPTY.getId(), PotionEntry.EMPTY);  // qyl27: An default value to avoid some mistake.

    }

    public static final String KEY_RECIPES = "recipes"; // Required. List can be empty.
    public static final String KEY_POTIONS = "potions"; // Required. List can be empty.

    public CustomBrewingRecipes(Map<String, Object> map) {
        var recipesObj = map.get(KEY_RECIPES);
        if (recipesObj instanceof List<?> recipesList) {
            for (var recipeObj : recipesList) {
                if (recipeObj instanceof BrewingEntry recipe) {
                    recipes.put(recipe.getId(), recipe);
                }
            }
        }

        var potionsObj = map.get(KEY_POTIONS);
        if (potionsObj instanceof List<?> potionsList) {
            for (var potionObj : potionsList) {
                if (potionObj instanceof PotionEntry potion) {
                    potions.put(potion.getId(), potion);
                }
            }
        }
    }

    @Override
    public @Nonnull Map<String, Object> serialize() {
        var map = new HashMap<String, Object>();
        map.put(KEY_RECIPES, recipes.values());
        map.put(KEY_POTIONS, potions.values());
        return map;
    }

    public static class BrewingEntry implements ConfigurationSerializable {

        private final String name;

        private final PotionType baseType;
        private final String basePotionKey;

        private final String ingredient;

        private final PotionType resultType;
        private final String resultPotionKey;

        public BrewingEntry(String name, NamespacedKey basePotion, Material ingredient, org.bukkit.potion.PotionType result) {
            this.name = name;
            this.basePotionKey = basePotion.toString();
            this.baseType = PotionType.fromKey(basePotion);
            this.ingredient = ingredient.name().toLowerCase(Locale.ROOT);
            this.resultType = PotionType.VANILLA;
            this.resultPotionKey = result.getKey().toString();
        }

        public BrewingEntry(String name, NamespacedKey basePotion, Material ingredient, PotionEntry result) {
            this.name = name;
            this.basePotionKey = basePotion.toString();
            this.baseType = PotionType.fromKey(basePotion);
            this.ingredient = ingredient.name().toLowerCase(Locale.ROOT);
            this.resultType = PotionType.CUSTOM;
            this.resultPotionKey = result.getId().toString();
        }

        public static final String KEY_NAME = "name";   // Required.
        public static final String KEY_BASE_TYPE = "baseType";  // Optional, auto detect.
        public static final String KEY_BASE_POTION = "basePotion";  // Required.
        public static final String KEY_INGREDIENT = "ingredient";   // Required.
        public static final String KEY_RESULT_TYPE = "resultType";   // Optional, auto detect.
        public static final String KEY_RESULT = "result";  // Required.

        public BrewingEntry(Map<String, Object> map) {
            // qyl27: Oh, not contains will get a null value.
//            if (!map.containsKey(KEY_NAME)
//                    || !map.containsKey(KEY_BASE_POTION)
//                    || !map.containsKey(KEY_INGREDIENT)
//                    || !map.containsKey(KEY_RESULT)) {
//                throw new RuntimeException("Can not deserialize custom brewing entry!");
//            }

            var nameObj = map.get(KEY_NAME);
            var basePotionObj = map.get(KEY_BASE_POTION);
            var ingredientObj = map.get(KEY_INGREDIENT);
            var resultObj = map.get(KEY_RESULT);

            if (!(nameObj instanceof String)
                    || !(basePotionObj instanceof String)
                    || !(ingredientObj instanceof String)
                    || !(resultObj instanceof String)) {
                throw new RuntimeException("Can not deserialize custom brewing entry!");
            }

            name = (String) nameObj;
            basePotionKey = (String) basePotionObj;
            ingredient = (String) ingredientObj;
            resultPotionKey = (String) resultObj;

            var baseTypeObj = map.get(KEY_BASE_TYPE);
            if (baseTypeObj instanceof String str) {
                baseType = PotionType.fromName(str);
            } else {
                baseType = PotionType.fromKey(NamespacedKey.fromString(basePotionKey));
            }

            var resultTypeObj = map.get(KEY_RESULT_TYPE);
            if (resultTypeObj instanceof String str) {
                resultType = PotionType.fromName(str);
            } else {
                resultType = PotionType.fromKey(NamespacedKey.fromString(resultPotionKey));
            }
        }

        @Override
        public @Nonnull Map<String, Object> serialize() {
            var map = new HashMap<String, Object>();
            map.put(KEY_NAME, name);
            map.put(KEY_BASE_TYPE, baseType.getName());
            map.put(KEY_BASE_POTION, basePotionKey);
            map.put(KEY_INGREDIENT, ingredient);
            map.put(KEY_RESULT_TYPE, resultType.getName());
            map.put(KEY_RESULT, resultPotionKey);
            return map;
        }

        public NamespacedKey getId() {
            return new NamespacedKey(MorePotions.getInstance(), name);
        }

        public PotionType getBaseType() {
            return baseType;
        }

        public NamespacedKey getBasePotion() {
            return NamespacedKey.fromString(basePotionKey);
        }

        public Material getIngredient() {
            return Material.getMaterial(ingredient);
        }

        public PotionType getResultType() {
            return resultType;
        }

        public NamespacedKey getResultId() {
            return NamespacedKey.fromString(resultPotionKey);
        }
    }

    public enum PotionType {
        VANILLA("vanilla"),
        CUSTOM("custom"),
        ;

        private final String name;

        PotionType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        // Fixme: qyl27: Another assumption that vanilla potion id must be under minecraft namespace.
        public static PotionType fromKey(@Nullable NamespacedKey key) {
            if (key != null && "minecraft".equalsIgnoreCase(key.getNamespace())) {
                return VANILLA;
            } else {
                return CUSTOM;
            }
        }

        public static PotionType fromName(String name) {
            if (VANILLA.getName().equalsIgnoreCase(name)) {
                return VANILLA;
            }

            return CUSTOM;
        }
    }
}
