package cx.rain.mc.morepotions.brewing;

import cx.rain.mc.morepotions.MorePotions;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class RecipeEntry implements ConfigurationSerializable {

    private final NamespacedKey id;

    private final PotionType baseType;
    private final NamespacedKey basePotionKey;

    private final Material ingredient;

    private final PotionType resultType;
    private final NamespacedKey resultPotionKey;

    public RecipeEntry(NamespacedKey id, NamespacedKey basePotion, Material ingredient, org.bukkit.potion.PotionType result) {
        this.id = id;
        this.basePotionKey = basePotion;
        this.baseType = PotionType.fromKey(basePotion);
        this.ingredient = ingredient;
        this.resultType = PotionType.VANILLA;
        this.resultPotionKey = result.getKey();
    }

    public RecipeEntry(NamespacedKey id, NamespacedKey basePotion, Material ingredient, PotionEntry result) {
        this.id = id;
        this.basePotionKey = basePotion;
        this.baseType = PotionType.fromKey(basePotion);
        this.ingredient = ingredient;
        this.resultType = PotionType.CUSTOM;
        this.resultPotionKey = result.getId();
    }

    public static final String KEY_ID = "id";   // Required.
    public static final String KEY_BASE_TYPE = "baseType";  // Optional, auto detect.
    public static final String KEY_BASE_POTION = "basePotion";  // Required.
    public static final String KEY_INGREDIENT = "ingredient";   // Required.
    public static final String KEY_RESULT_TYPE = "resultType";   // Optional, auto detect.
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
            id = NamespacedKey.fromString(idStr, MorePotions.getInstance());
            basePotionKey = NamespacedKey.fromString(basePotionStr, MorePotions.getInstance());

            ingredient = Material.getMaterial(ingredientStr);
            if (ingredient == null) {
                throw new RuntimeException("Unknown ingredient '" + ingredientStr + "'!");
            }

            resultPotionKey = NamespacedKey.fromString(resultStr, MorePotions.getInstance());

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
        } else {
            throw new RuntimeException("Not completed brewing recipe!");
        }
    }

    @Override
    public @Nonnull Map<String, Object> serialize() {
        var map = new HashMap<String, Object>();
        map.put(KEY_ID, id.toString());
        map.put(KEY_BASE_TYPE, baseType.getName());
        map.put(KEY_BASE_POTION, basePotionKey.toString());
        map.put(KEY_INGREDIENT, ingredient.name());
        map.put(KEY_RESULT_TYPE, resultType.getName());
        map.put(KEY_RESULT, resultPotionKey.toString());
        return map;
    }

    public NamespacedKey getId() {
        return id;
    }

    public PotionType getBaseType() {
        return baseType;
    }

    public NamespacedKey getBasePotion() {
        return basePotionKey;
    }

    public Material getIngredient() {
        return ingredient;
    }

    public PotionType getResultType() {
        return resultType;
    }

    public NamespacedKey getResultId() {
        return resultPotionKey;
    }
}
