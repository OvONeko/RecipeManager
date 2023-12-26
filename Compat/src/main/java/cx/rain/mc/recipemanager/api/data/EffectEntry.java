package cx.rain.mc.recipemanager.api.data;

import cx.rain.mc.recipemanager.api.RecipeManagerAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * A potion effect entry.
 * @param id EffectEntry id.
 * @param type Effect type.
 * @param length Duration (seconds).
 * @param level Amplifier + 1. So the value must between 1 and 256. (Let's be more friendly).
 * @param showParticles
 * @param showIcon
 * @param byBeacon Same with ambient.
 */
public record EffectEntry(NamespacedKey id, PotionEffectType type, int length, short level,
                          boolean showParticles, boolean showIcon, boolean byBeacon) implements ConfigurationSerializable {

    public EffectEntry(NamespacedKey id, PotionEffectType type, int length) {
        this(id, type, length, (short) 1);
    }

    public EffectEntry(NamespacedKey id, PotionEffectType type, int length, short level) {
        this(id, type, length, level, true);
    }

    public EffectEntry(NamespacedKey id, PotionEffectType type, int length, short level, boolean showParticles) {
        this(id, type, length, level, showParticles, true, false);
    }

    public int getDuration() {
        return length * 20; // Fixme: qyl27: An assumption about 1 seconds equals 20 ticks, will be broken when using something accelerates tick speed.
    }

    public int getAmplifier() {
        return level - 1;
    }

    public static final String KEY_ID = "id";   // Required
    public static final String KEY_TYPE = "type";   // Required
    public static final String KEY_LENGTH = "length";   // Required
    public static final String KEY_LEVEL = "level"; // Optional, default 1
    public static final String KEY_SHOW_PARTICLES = "showParticles";    // Optional, default true
    public static final String KEY_SHOW_ICON = "showIcon";  // Optional, default true
    public static final String KEY_BY_BEACON = "byBeacon";  // Optional, default false

    @Override
    public @Nonnull Map<String, Object> serialize() {
        var map = new HashMap<String, Object>();
        map.put(KEY_ID, id.toString());
        map.put(KEY_TYPE, type.getKey().toString());
        map.put(KEY_LENGTH, length);
        map.put(KEY_LEVEL, level);
        map.put(KEY_SHOW_PARTICLES, showParticles);
        map.put(KEY_SHOW_ICON, showIcon);
        map.put(KEY_BY_BEACON, byBeacon);
        return map;
    }

    public static EffectEntry deserialize(Map<String, Object> map) {
        var idObj = map.get(KEY_ID);
        var typeObj = map.get(KEY_TYPE);
        var lengthObj = map.get(KEY_LENGTH);

        if (idObj instanceof String idStr
                && typeObj instanceof String typeStr
                && lengthObj instanceof Integer length) {
            var id = NamespacedKey.fromString(idStr, RecipeManagerAPI.getInstance());

            var type = PotionEffectType.getByKey(NamespacedKey.fromString(typeStr));
            if (type == null) {
                throw new RuntimeException("Effect '" + typeStr + "' not found!");
            }

            var levelObj = map.get(KEY_LEVEL);
            short level = 1;
            if (levelObj instanceof Integer l) {
                level = (short) Math.max(Math.min(l, 256), 1);
            }

            var particlesObj = map.get(KEY_SHOW_PARTICLES);
            var showParticles = true;
            if (particlesObj instanceof Boolean particles) {
                showParticles = particles;
            }

            var iconObj = map.get(KEY_SHOW_ICON);
            var showIcon = true;
            if (iconObj instanceof Boolean icon) {
                showIcon = icon;
            }

            var beaconObj = map.get(KEY_BY_BEACON);
            var byBeacon = false;
            if (beaconObj instanceof Boolean beacon) {
                byBeacon = beacon;
            }


            return new EffectEntry(id, type, length, level, showParticles, showIcon, byBeacon);
        } else {
            throw new RuntimeException("Bad effect entry!");
        }
    }
}
