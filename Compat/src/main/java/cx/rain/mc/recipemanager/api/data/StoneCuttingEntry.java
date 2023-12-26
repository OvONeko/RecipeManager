package cx.rain.mc.recipemanager.api.data;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class StoneCuttingEntry implements ConfigurationSerializable {
    private final NamespacedKey id;
    private final Material from;
    private final Material result;
    private final int amount;

    public StoneCuttingEntry(NamespacedKey id, Material from, Material result, int amount) {
        this.id = id;
        this.from = from;
        this.result = result;
        this.amount = amount;
    }

    public static final String KEY_ID = "id";
    public static final String KEY_FROM = "from";
    public static final String KEY_RESULT = "result";
    public static final String KEY_AMOUNT = "amount";

    public StoneCuttingEntry(Map<String, Object> map) {
        var idObj = map.get(KEY_ID);
        var fromObj = map.get(KEY_FROM);
        var resultObj = map.get(KEY_RESULT);
        var amountObj = map.get(KEY_AMOUNT);

        if (idObj instanceof String idStr && fromObj instanceof String fromStr && resultObj instanceof String resultStr && amountObj instanceof Integer amountInt) {
            id = NamespacedKey.fromString(idStr);
            from = Material.getMaterial(fromStr);
            result = Material.getMaterial(resultStr);
            amount = amountInt;
        }
        else {
            throw new RuntimeException("Not completed stone cutting recipe!");
        }
    }

    @Override
    public @Nonnull Map<String, Object> serialize() {
        var map = new HashMap<String, Object>();
        map.put(KEY_ID, id.toString());
        map.put(KEY_FROM, from.name());
        map.put(KEY_RESULT, result.name());
        map.put(KEY_AMOUNT, amount);
        return map;
    }

    public NamespacedKey getId() {
        return id;
    }

    public Material getFrom() {
        return from;
    }

    public Material getResult() {
        return result;
    }

    public int getAmount() {
        return amount;
    }
}
