package cx.rain.mc.morepotions;

import cx.rain.mc.morepotions.brewing.PotionEntry;
import cx.rain.mc.morepotions.config.ConfigManager;
import cx.rain.mc.morepotions.config.CustomBrewingRecipes;
import cx.rain.mc.morepotions.listener.DrinkPotionListener;
import cx.rain.mc.morepotions.brewing.EffectEntry;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class MorePotions extends JavaPlugin {
    private static MorePotions INSTANCE;

    static {
        ConfigurationSerialization.registerClass(PotionEntry.class);
        ConfigurationSerialization.registerClass(EffectEntry.class);
        ConfigurationSerialization.registerClass(CustomBrewingRecipes.class);
        ConfigurationSerialization.registerClass(CustomBrewingRecipes.BrewingEntry.class);
    }

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
        if (getConfigManager().allowRandomEffect()) {
            Bukkit.getPluginManager().registerEvents(new DrinkPotionListener(this), this);
        }
    }

    @Override
    public void onDisable() {

    }
}
