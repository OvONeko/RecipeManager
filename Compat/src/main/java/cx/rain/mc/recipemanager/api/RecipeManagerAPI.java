package cx.rain.mc.recipemanager.api;

import org.bukkit.plugin.Plugin;

public class RecipeManagerAPI {
    private static Plugin INSTANCE;

    public static void setInstance(Plugin plugin) {
        INSTANCE = plugin;
    }

    public static Plugin getInstance() {
        return INSTANCE;
    }
}
