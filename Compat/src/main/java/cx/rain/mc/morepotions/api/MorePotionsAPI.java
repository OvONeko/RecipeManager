package cx.rain.mc.morepotions.api;

import org.bukkit.plugin.Plugin;

public class MorePotionsAPI {
    private static Plugin INSTANCE;

    public static void setInstance(Plugin plugin) {
        INSTANCE = plugin;
    }

    public static Plugin getInstance() {
        return INSTANCE;
    }
}
