package cx.rain.mc.morepotions.brewing;

import cx.rain.mc.morepotions.MorePotions;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PotionEntry implements ConfigurationSerializable {

    public static final PotionEntry EMPTY = new PotionEntry(new NamespacedKey(MorePotions.getInstance(), "empty"), List.of());

    private static final Color DEFAULT_PINK_COLOR = Color.fromRGB(245, 169, 184);

    private final String id;
    private final List<EffectEntry> effects = new ArrayList<>();

    private String showName;    // Todo: qyl27: I18n support in future?
    private Color color = DEFAULT_PINK_COLOR;

    public PotionEntry(NamespacedKey id, List<EffectEntry> potionEffects) {
        this.id = id.toString();
        this.effects.addAll(potionEffects);
    }

    public static final String KEY_ID = "id";   // Required.
    public static final String KEY_EFFECTS = "effects"; // Required, empty is allowed.
    public static final String KEY_SHOW_NAME = "name";  // Optional, nullable.
    public static final String KEY_COLOR = "color"; // Optional, default RGB(245, 169, 184).

    public PotionEntry(Map<String, Object> map) {
        var idObj = map.get(KEY_ID);
        var effectsObj = map.get(KEY_EFFECTS);

        if (!(idObj instanceof String) || !(effectsObj instanceof List<?>)) {
            throw new RuntimeException("Illegal potion entry!");
        }

        id = (String) idObj;

        for (var effectObj : (List<?>) effectsObj) {
            if (effectObj instanceof EffectEntry) {
                effects.add((EffectEntry) effectObj);
            }
        }

        var nameObj = map.get(KEY_SHOW_NAME);
        if (nameObj instanceof String) {
            showName = (String) nameObj;
        }

        var colorObj = map.get(KEY_COLOR);
        if (colorObj instanceof Color) {
            color = (Color) colorObj;
        }
    }

    @Override
    public @Nonnull Map<String, Object> serialize() {
        var map = new HashMap<String, Object>();
        map.put(KEY_ID, id);
        map.put(KEY_EFFECTS, effects);

        if (showName != null) {
            map.put(KEY_SHOW_NAME, showName);
        }

        if (color != null && !color.equals(DEFAULT_PINK_COLOR)) {
            map.put(KEY_COLOR, color);
        }
        return map;
    }

    public NamespacedKey getId() {
        return NamespacedKey.fromString(id);
    }

    public List<EffectEntry> getEffects() {
        return effects;
    }

    public String getShowName() {
        return showName;
    }

    public Color getColor() {
        return color;
    }
}
