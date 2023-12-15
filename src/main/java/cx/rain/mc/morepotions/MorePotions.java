package cx.rain.mc.morepotions;

import cx.rain.mc.morepotions.brewing.BrewingTicker;
import cx.rain.mc.morepotions.brewing.config.PotionEntry;
import cx.rain.mc.morepotions.brewing.config.RecipeEntry;
import cx.rain.mc.morepotions.config.ConfigManager;
import cx.rain.mc.morepotions.brewing.BrewingManager;
import cx.rain.mc.morepotions.listener.BrewingRecipeListener;
import cx.rain.mc.morepotions.listener.DrinkPotionListener;
import cx.rain.mc.morepotions.brewing.config.EffectEntry;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class MorePotions extends JavaPlugin {
    private static MorePotions INSTANCE;

    static {
        ConfigurationSerialization.registerClass(PotionEntry.class);
        ConfigurationSerialization.registerClass(EffectEntry.class);
        ConfigurationSerialization.registerClass(RecipeEntry.class);
    }

    private final BrewingManager brewingManager;

    private final ConfigManager configManager;

    public MorePotions() {
        INSTANCE = this;

        brewingManager = new BrewingManager(this);
        configManager = new ConfigManager(this);
    }

    public BrewingManager getBrewingManager() {
        return brewingManager;
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
            getServer().getPluginManager().registerEvents(new DrinkPotionListener(), this);
        }

        if (getConfigManager().allowCustomBrewingRecipe()) {
            getServer().getPluginManager().registerEvents(new BrewingRecipeListener(), this);
        }
    }

    @Override
    public void onDisable() {
        BrewingTicker.stopAll();
    }
}
