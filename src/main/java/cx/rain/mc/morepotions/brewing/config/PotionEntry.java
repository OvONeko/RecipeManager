package cx.rain.mc.morepotions.brewing.config;

import cx.rain.mc.morepotions.MorePotions;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PotionEntry implements ConfigurationSerializable {

    private static final Color DEFAULT_PINK_COLOR = Color.fromRGB(245, 169, 184);

    private final NamespacedKey id;
    private final List<EffectEntry> effects = new ArrayList<>();

    private @Nullable String showName;    // Todo: qyl27: I18n support in future?
    private Color color = DEFAULT_PINK_COLOR;

    public PotionEntry(NamespacedKey id, List<EffectEntry> potionEffects) {
        this.id = id;
        this.effects.addAll(potionEffects);
    }

    public static final String KEY_ID = "id";   // Required.
    public static final String KEY_EFFECTS = "effects"; // Required, empty is allowed.
    public static final String KEY_SHOW_NAME = "name";  // Optional, nullable.
    public static final String KEY_COLOR = "color"; // Optional, default RGB(245, 169, 184).

    public PotionEntry(Map<String, Object> map) {
        var idObj = map.get(KEY_ID);
        var effectsObj = map.get(KEY_EFFECTS);

        if (idObj instanceof String idStr
                && effectsObj instanceof List<?> effectsList) {
            id = NamespacedKey.fromString(idStr, MorePotions.getInstance());
            for (var effectObj : effectsList) {
                if (effectObj instanceof String effectStr) {
                    var effectId = NamespacedKey.fromString(effectStr, MorePotions.getInstance());
                    var effect = MorePotions.getInstance().getBrewingManager().getEffect(effectId);

                    if (effect != null) {
                        effects.add(effect);
                    } else {
                        throw new RuntimeException("Effect '" + effectStr + "' used by '" + idStr + "' was not registered!");
                    }
                }
            }

            var nameObj = map.get(KEY_SHOW_NAME);
            if (nameObj instanceof String name) {
                showName = name;
            }

            var colorObj = map.get(KEY_COLOR);
            if (colorObj instanceof Color c) {
                color = c;
            }
        } else {
            throw new RuntimeException("Illegal potion entry!");
        }
    }

    @Override
    public @Nonnull Map<String, Object> serialize() {
        var map = new HashMap<String, Object>();
        map.put(KEY_ID, id);

        var list = new ArrayList<String>();
        for (var effect : effects) {
            list.add(effect.id().toString());
        }
        map.put(KEY_EFFECTS, list);

        if (showName != null) {
            map.put(KEY_SHOW_NAME, showName);
        }

        if (color != null && !color.equals(DEFAULT_PINK_COLOR)) {
            map.put(KEY_COLOR, color);
        }
        return map;
    }

    public NamespacedKey getId() {
        return id;
    }

    public List<EffectEntry> getEffects() {
        return effects;
    }

    public boolean hasShowName() {
        return showName != null;
    }

    public @Nullable String getShowName() {
        return showName;
    }

    public Color getColor() {
        return color;
    }
}
