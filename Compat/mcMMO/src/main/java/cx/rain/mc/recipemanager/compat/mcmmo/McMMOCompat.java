package cx.rain.mc.recipemanager.compat.mcmmo;

import cx.rain.mc.recipemanager.api.compat.IMorePotionCompat;
import cx.rain.mc.recipemanager.api.event.MorePotionBrewEndEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class McMMOCompat implements IMorePotionCompat {
    private Listener listener;

    @Override
    public boolean isFit(Plugin plugin) {
        return Bukkit.getPluginManager().getPlugin("mcMMO") != null;
    }

    @Override
    public void registerCompat(Plugin plugin) {
        if (listener == null) {
            listener = new McMMOCompatListener();
        }

        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    @Override
    public void unregisterCompat(Plugin plugin) {
        MorePotionBrewEndEvent.getHandlerList().unregister(listener);
        listener = null;
    }
}
