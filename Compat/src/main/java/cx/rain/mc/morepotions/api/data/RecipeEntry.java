package cx.rain.mc.morepotions.api.data;

import cx.rain.mc.morepotions.api.MorePotionsAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class RecipeEntry implements ConfigurationSerializable {

    private final NamespacedKey id;
    private final PotionType type;

    private final PotionCategory baseCategory;
    private final NamespacedKey basePotionKey;

    private final Material ingredient;

    private final PotionCategory resultCategory;
    private final NamespacedKey resultPotionKey;

    public RecipeEntry(NamespacedKey id, PotionType type, NamespacedKey basePotion, Material ingredient, org.bukkit.potion.PotionType result) {
        this.id = id;
        this.type = type;
        this.basePotionKey = basePotion;
        this.baseCategory = PotionCategory.fromKey(basePotion);
        this.ingredient = ingredient;
        this.resultCategory = PotionCategory.VANILLA;
        this.resultPotionKey = result.getKey();
    }

    public RecipeEntry(NamespacedKey id, PotionType type, NamespacedKey basePotion, Material ingredient, PotionEntry result) {
        this.id = id;
        this.type = type;
        this.basePotionKey = basePotion;
        this.baseCategory = PotionCategory.fromKey(basePotion);
        this.ingredient = ingredient;
        this.resultCategory = PotionCategory.CUSTOM;
        this.resultPotionKey = result.getId();
    }

    public static final String KEY_ID = "id";   // Required.
    public static final String KEY_TYPE = "type"; // Optional, default drink.
    public static final String KEY_BASE_CATEGORY = "baseCategory";  // Optional, auto detect.
    public static final String KEY_BASE_POTION = "basePotion";  // Required.
    public static final String KEY_INGREDIENT = "ingredient";   // Required.
    public static final String KEY_RESULT_CATEGORY = "resultCategory";   // Optional, auto detect.
    public static final String KEY_RESULT = "result";  // Required.

    public RecipeEntry(Map<String, Object> map) {
        var idObj = map.get(KEY_ID);
        var basePotionObj = map.get(KEY_BASE_POTION);
        var ingredientObj = map.get(KEY_INGREDIENT);
        var resultObj = map.get(KEY_RESULT);

        if (idObj instanceof String idStr
                && basePotionObj instanceof String basePotionStr
                && ingredientObj instanceof String ingredientStr
                && resultObj instanceof String resultStr) {
            id = NamespacedKey.fromString(idStr, MorePotionsAPI.getInstance());
            basePotionKey = NamespacedKey.fromString(basePotionStr, MorePotionsAPI.getInstance());

            ingredient = Material.getMaterial(ingredientStr);
            if (ingredient == null) {
                throw new RuntimeException("Unknown ingredient '" + ingredientStr + "'!");
            }

            resultPotionKey = NamespacedKey.fromString(resultStr, MorePotionsAPI.getInstance());

            var typeObj = map.get(KEY_TYPE);
            if (typeObj instanceof String typeStr) {
                type = PotionType.fromString(typeStr);
            } else {
                type = PotionType.DRINK;
            }

            var baseTypeObj = map.get(KEY_BASE_CATEGORY);
            if (baseTypeObj instanceof String str) {
                baseCategory = PotionCategory.fromName(str);
            } else {
                baseCategory = PotionCategory.fromKey(basePotionKey);
            }

            var resultTypeObj = map.get(KEY_RESULT_CATEGORY);
            if (resultTypeObj instanceof String str) {
                resultCategory = PotionCategory.fromName(str);
            } else {
                resultCategory = PotionCategory.fromKey(resultPotionKey);
            }
        } else {
            throw new RuntimeException("Not completed brewing recipe!");
        }
    }

    @Override
    public @Nonnull Map<String, Object> serialize() {
        var map = new HashMap<String, Object>();
        map.put(KEY_ID, id.toString());
        map.put(KEY_TYPE, type.getName());
        map.put(KEY_BASE_CATEGORY, baseCategory.getName());
        map.put(KEY_BASE_POTION, basePotionKey.toString());
        map.put(KEY_INGREDIENT, ingredient.name());
        map.put(KEY_RESULT_CATEGORY, resultCategory.getName());
        map.put(KEY_RESULT, resultPotionKey.toString());
        return map;
    }

    public NamespacedKey getId() {
        return id;
    }

    public PotionType getType() {
        return type;
    }

    public PotionCategory getBaseCategory() {
        return baseCategory;
    }

    public NamespacedKey getBasePotion() {
        return basePotionKey;
    }

    public Material getIngredient() {
        return ingredient;
    }

    public PotionCategory getResultCategory() {
        return resultCategory;
    }

    public NamespacedKey getResultId() {
        return resultPotionKey;
    }
}
