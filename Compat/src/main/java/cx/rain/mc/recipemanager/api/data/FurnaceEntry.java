package cx.rain.mc.recipemanager.api.data;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class FurnaceEntry implements ConfigurationSerializable {
    private final NamespacedKey id;
    private final Material from;
    private final ItemStack result;
    private final float experience;
    private final int time;
    private final boolean allowBlast;

    public FurnaceEntry(NamespacedKey id, Material from, ItemStack result, float experience, int time, boolean allowBlast) {
        this.id = id;
        this.from = from;
        this.result = result;
        this.experience = experience;
        this.time = time;
        this.allowBlast = allowBlast;
    }

    public static final String KEY_ID = "id";
    public static final String KEY_FROM = "from";
    public static final String KEY_RESULT = "result";
    public static final String KEY_EXPERIENCE = "experience";
    public static final String KEY_TIME = "time";
    public static final String KEY_ALLOW_BLAST = "allowBlast";

    public FurnaceEntry(Map<String, Object> map) {
        var idObj = map.get(KEY_ID);
        var fromObj = map.get(KEY_FROM);
        var resultObj = map.get(KEY_RESULT);

        if (idObj instanceof String idStr && fromObj instanceof String fromStr && resultObj instanceof String resultStr) {
            id = NamespacedKey.fromString(idStr);
            from = Material.getMaterial(fromStr);
            if ((from == null) || (Material.getMaterial(resultStr) == null))
                throw new RuntimeException("Unknown ingredient");
            result = new ItemStack(Material.getMaterial(resultStr));

            var experienceObj = map.get(KEY_EXPERIENCE);
            if (experienceObj instanceof Float experienceFloat) {
                experience = experienceFloat;
            }
            else if (experienceObj instanceof Double experienceDouble) {
                experience = experienceDouble.floatValue();
            }
            else {
                experience = 0.7F;
            }

            var timeObj = map.get(KEY_TIME);
            if (timeObj instanceof Integer timeInt) {
                time = timeInt;
            }
            else {
                time = 200;
            }

            var allowBlastObj = map.get(KEY_ALLOW_BLAST);
            if (allowBlastObj instanceof String allowBlastStr) {
                allowBlast = Boolean.getBoolean(allowBlastStr);
            }
            else {
                allowBlast = false;
            }
        }
        else {
            throw new RuntimeException("Not completed furnace recipe!");
        }
    }

    @Override
    public @Nonnull Map<String, Object> serialize() {
        var map = new HashMap<String, Object>();
        map.put(KEY_ID, id.toString());
        map.put(KEY_FROM, from.name());
        map.put(KEY_RESULT, result.getType().name());
        map.put(KEY_EXPERIENCE, experience);
        map.put(KEY_TIME, time);
        map.put(KEY_ALLOW_BLAST, allowBlast);
        return map;
    }

    public NamespacedKey getId() {
        return id;
    }

    public Material getFrom() {
        return from;
    }

    public ItemStack getResult() {
        return result;
    }

    public float getExperience() {
        return experience;
    }

    public int getTime() {
        return time;
    }

    public boolean isAllowBlast() {
        return allowBlast;
    }
}