package cx.rain.mc.morepotions;

import cx.rain.mc.morepotions.config.ConfigManager;
import cx.rain.mc.morepotions.listener.DrinkPotionListener;
import cx.rain.mc.morepotions.listener.UseAnvilListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MorePotions extends JavaPlugin {
    private static MorePotions INSTANCE;

    private final ConfigManager configManager;

    public MorePotions() {
        INSTANCE = this;
        configManager = new ConfigManager(this);
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public static MorePotions getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        if (!getConfigManager().Enabled())
            return;
        if (getConfigManager().AllowRandomEffect()) Bukkit.getPluginManager().registerEvents(new DrinkPotionListener(this), this);
        if (getConfigManager().AllowMixByAnvil()) Bukkit.getPluginManager().registerEvents(new UseAnvilListener(this), this);
    }

    @Override
    public void onDisable() {

    }
}
