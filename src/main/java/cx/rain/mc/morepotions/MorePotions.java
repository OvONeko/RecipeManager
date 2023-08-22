package cx.rain.mc.morepotions;

import cx.rain.mc.morepotions.listener.DrinkPotionListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MorePotions extends JavaPlugin {
    private static MorePotions INSTANCE;

    public MorePotions() {
        INSTANCE = this;
    }

    public static MorePotions getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new DrinkPotionListener(this), this);

    }

    @Override
    public void onDisable() {

    }
}
