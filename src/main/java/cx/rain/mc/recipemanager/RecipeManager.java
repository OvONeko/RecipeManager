package cx.rain.mc.recipemanager;

import cx.rain.mc.recipemanager.api.RecipeManagerAPI;
import cx.rain.mc.recipemanager.api.data.*;
import cx.rain.mc.recipemanager.brewing.BrewingTicker;
import cx.rain.mc.recipemanager.command.MorePotionsCommand;
import cx.rain.mc.recipemanager.compat.CompatManager;
import cx.rain.mc.recipemanager.config.ConfigManager;
import cx.rain.mc.recipemanager.brewing.BrewingManager;
import cx.rain.mc.recipemanager.furnace.FurnaceManager;
import cx.rain.mc.recipemanager.listener.BrewingRecipeListener;
import cx.rain.mc.recipemanager.listener.DrinkPotionListener;
import cx.rain.mc.recipemanager.stonecutting.StoneCuttingManager;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public class RecipeManager extends JavaPlugin {
    private static RecipeManager INSTANCE;

    static {
        ConfigurationSerialization.registerClass(PotionEntry.class);
        ConfigurationSerialization.registerClass(EffectEntry.class);
        ConfigurationSerialization.registerClass(RecipeEntry.class);
        ConfigurationSerialization.registerClass(FurnaceEntry.class);
        ConfigurationSerialization.registerClass(StoneCuttingEntry.class);
    }

    private final BrewingManager brewingManager;

    private final ConfigManager configManager;

    private final CompatManager compatManager;

    private final FurnaceManager furnaceManager;
    private final StoneCuttingManager stoneCuttingManager;

    public RecipeManager() {
        INSTANCE = this;
        RecipeManagerAPI.setInstance(this);

        brewingManager = new BrewingManager(this);
        configManager = new ConfigManager(this);
        compatManager = new CompatManager();
        furnaceManager = new FurnaceManager(this);
        stoneCuttingManager = new StoneCuttingManager(this);
    }

    public BrewingManager getBrewingManager() {
        return brewingManager;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public CompatManager getCompatManager() {
        return compatManager;
    }

    public FurnaceManager getFurnaceManager() {
        return furnaceManager;
    }

    public StoneCuttingManager getStoneCuttingManager() {
        return stoneCuttingManager;
    }

    public static RecipeManager getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new DrinkPotionListener(), this);
        getServer().getPluginManager().registerEvents(new BrewingRecipeListener(), this);

        var pluginCommand = getCommand("morepotions");
        if (pluginCommand != null) {
            var command = new MorePotionsCommand();
            pluginCommand.setExecutor(command);
            pluginCommand.setTabCompleter(command);
        }

        if (getConfigManager().allowBrewingCompat()) {
            getCompatManager().load();
            getCompatManager().register();
        }
    }

    public void onReload() {
        getCompatManager().unregister();

        getConfigManager().reload();

        if (getConfigManager().allowBrewingCompat()) {
            getCompatManager().load();
            getCompatManager().register();
        }
    }

    @Override
    public void onDisable() {
        getCompatManager().unregister();
        BrewingTicker.stopAll();
    }
}
