package cx.rain.mc.recipemanager.api.data;

import org.bukkit.NamespacedKey;

import javax.annotation.Nullable;

public enum PotionCategory {
    VANILLA("vanilla"),
    CUSTOM("custom"),
    ;

    private final String name;

    PotionCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Fixme: qyl27: Another assumption that vanilla potion id must be under minecraft namespace.
    public static PotionCategory fromKey(@Nullable NamespacedKey key) {
        if (key != null && "minecraft".equalsIgnoreCase(key.getNamespace())) {
            return VANILLA;
        } else {
            return CUSTOM;
        }
    }

    public static PotionCategory fromName(String name) {
        if (VANILLA.getName().equalsIgnoreCase(name)) {
            return VANILLA;
        }

        return CUSTOM;
    }
}
