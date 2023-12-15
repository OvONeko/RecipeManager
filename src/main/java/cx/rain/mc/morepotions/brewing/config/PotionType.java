package cx.rain.mc.morepotions.brewing.config;

import javax.annotation.Nullable;

public enum PotionType {
    DRINK("drink"),
    SPLASH("splash"),
    LINGERING("lingering"),
    ;

    private final String name;

    PotionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static PotionType fromString(@Nullable String string) {
        if (SPLASH.getName().equalsIgnoreCase(string)) {
            return SPLASH;
        }

        if (LINGERING.getName().equalsIgnoreCase(string)) {
            return LINGERING;
        }

        return DRINK;
    }
}
