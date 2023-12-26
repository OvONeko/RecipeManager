package cx.rain.mc.recipemanager.api.compat;

import org.bukkit.plugin.Plugin;

/**
 * Allow compat supports.
 * Compat can use Bukkit APIs.
 */
public interface IMorePotionCompat {
    /**
     * Should compat be used?
     * Will be invoked before every actually load.
     * @return A boolean for should or not.
     */
    boolean isFit(Plugin plugin);

    /**
     * Invoke to actually load compat.
     */
    void registerCompat(Plugin plugin);

    /**
     * Invoke to actually unload compat.
     */
    void unregisterCompat(Plugin plugin);
}
