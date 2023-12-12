package cx.rain.mc.morepotions.brewing;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * A potion effect entry.
 * @param type Effect type.
 * @param length Duration (seconds).
 * @param level Amplifier + 1. So the value must between 1 and 256. (Let's be more friendly).
 * @param showParticles
 * @param showIcon
 * @param byBeacon Same with ambient.
 */
public record EffectEntry(PotionEffectType type, int length, short level,
                          boolean showParticles, boolean showIcon, boolean byBeacon) implements ConfigurationSerializable {

    public EffectEntry(PotionEffectType type, int length) {
        this(type, length, (short) 1);
    }

    public EffectEntry(PotionEffectType type, int length, short level) {
        this(type, length, level, true);
    }

    public EffectEntry(PotionEffectType type, int length, short level, boolean showParticles) {
        this(type, length, level, showParticles, true, false);
    }

    public int getDuration() {
        return length / 20; // Fixme: qyl27: An assumption about 1 seconds equals 20 ticks, will be broken when using something accelerates tick speed.
    }

    public int getAmplifier() {
        return level - 1;
    }

    public static final String KEY_TYPE = "type";   // Required
    public static final String KEY_LENGTH = "length";   // Required
    public static final String KEY_LEVEL = "level"; // Optional, default 1
    public static final String KEY_SHOW_PARTICLES = "showParticles";    // Optional, default true
    public static final String KEY_SHOW_ICON = "showIcon";  // Optional, default true
    public static final String KEY_BY_BEACON = "byBeacon";  // Optional, default false

    @Override
    public @Nonnull Map<String, Object> serialize() {
        var map = new HashMap<String, Object>();
        map.put(KEY_TYPE, type.getKey().toString());
        map.put(KEY_LENGTH, length);
        map.put(KEY_LEVEL, level);
        map.put(KEY_SHOW_PARTICLES, showParticles);
        map.put(KEY_SHOW_ICON, showIcon);
        map.put(KEY_BY_BEACON, byBeacon);
        return map;
    }

    public static EffectEntry deserialize(Map<String, Object> map) {
        var typeObj = map.get(KEY_TYPE);
        var lengthObj = map.get(KEY_LENGTH);

        if (!(typeObj instanceof String) || !(lengthObj instanceof Integer)) {
            throw new RuntimeException("Bad effect entry!");
        }
        var type = PotionEffectType.getByKey(NamespacedKey.fromString((String) typeObj));
        if (type == null) {
            throw new RuntimeException("Effect '" + typeObj + "' not found!");
        }
        var length = (int) lengthObj;

        var levelObj = map.get(KEY_LEVEL);
        short level = 1;
        if (levelObj instanceof Integer) {
            level = (short) levelObj;
        }

        var particlesObj = map.get(KEY_SHOW_PARTICLES);
        var showParticles = true;
        if (particlesObj instanceof Boolean) {
            showParticles = (boolean) particlesObj;
        }

        var iconObj = map.get(KEY_SHOW_ICON);
        var showIcon = true;
        if (iconObj instanceof Boolean) {
            showIcon = (boolean) iconObj;
        }

        var beaconObj = map.get(KEY_BY_BEACON);
        var byBeacon = false;
        if (beaconObj instanceof Boolean) {
            byBeacon = (boolean) beaconObj;
        }

        return new EffectEntry(type, length, level, showParticles, showIcon, byBeacon);
    }
}
