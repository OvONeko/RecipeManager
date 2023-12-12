package cx.rain.mc.morepotions.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.File;

public class ConfigManager {
    private final Plugin plugin;

    private final FileConfiguration config;

    private final File brewingRecipeFile;   // Todo: qyl27: No local variable, maybe we have a auto-save feature?
    private final YamlConfiguration brewingRecipe;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        plugin.saveDefaultConfig();
        config = plugin.getConfig();

        // Fixme: qyl27: Split into multiple config files?
        brewingRecipeFile = new File(plugin.getDataFolder(), "brewing.yml");
        brewingRecipe = loadConfig(brewingRecipeFile, "/brewing.yml");
    }

    public boolean allowRandomEffect() {
        return config.getBoolean("features.mundaneRandomEffect", true);
    }

    public boolean allowCustomBrewingRecipe() {
        return config.getBoolean("features.customBrewingRecipe", true);
    }

    public boolean allowAnvilMixture() {
        return config.getBoolean("features.anvilMixPotion", true);
    }

    public CustomBrewingRecipes getCustomBrewingRecipe() {
        if (!allowCustomBrewingRecipe()) {
            plugin.getLogger().warning("Brewing recipe is not allowed by config file.");
            return new CustomBrewingRecipes();
        }

        // qyl27: So, Bukkit, **** you! Why not to add a generic parameter?
        var objRecipe = brewingRecipe.get("", new CustomBrewingRecipes());   // Fixme: qyl27: Test it?
        if (objRecipe instanceof CustomBrewingRecipes) {
            return (CustomBrewingRecipes) objRecipe;
        }

        plugin.getLogger().warning("Bad brewing recipe file.");
        return new CustomBrewingRecipes();
    }

    private YamlConfiguration loadConfig(@Nonnull File file, @Nonnull String alternativeResourcePath) {
        if (!file.exists()) {
            plugin.saveResource(alternativeResourcePath, false);
        }

        return YamlConfiguration.loadConfiguration(file);
    }
}
