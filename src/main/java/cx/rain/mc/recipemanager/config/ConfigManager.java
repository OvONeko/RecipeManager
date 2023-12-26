package cx.rain.mc.recipemanager.config;

import cx.rain.mc.recipemanager.RecipeManager;
import cx.rain.mc.recipemanager.api.data.EffectEntry;
import cx.rain.mc.recipemanager.api.data.FurnaceEntry;
import cx.rain.mc.recipemanager.api.data.PotionEntry;
import cx.rain.mc.recipemanager.api.data.RecipeEntry;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

public class ConfigManager {
    private final Plugin plugin;

    private FileConfiguration config;

    private static final String BREWING_RECIPES_DIR_NAME = "brewing";
    private static final String CUSTOM_POTIONS_DIR_NAME = "potions";
    private static final String CUSTOM_EFFECTS_DIR_NAME = "effects";

    private static final String EXAMPLE_BREWING_RECIPES_NAME = "example_recipe.yml";
    private static final String EXAMPLE_BREWING_RECIPES_RESOURCE_PATH = "brewing/example_recipe.yml";
    private static final String EXAMPLE_CUSTOM_POTIONS_NAME = "example_potions.yml";
    private static final String EXAMPLE_CUSTOM_POTIONS_RESOURCE_PATH = "brewing/potions/example_potions.yml";
    private static final String EXAMPLE_CUSTOM_EFFECTS_NAME = "example_effects.yml";
    private static final String EXAMPLE_CUSTOM_EFFECTS_RESOURCE_PATH = "brewing/effects/example_effects.yml";

    private static final String FURNACE_RECIPES_DIR_NAME = "furnace";

    private static final String EXAMPLE_FURNACE_RECIPES_NAME = "example_recipe.yml";
    private static final String EXAMPLE_FURNACE_RECIPES_RESOURCE_PATH = "furnace/example_recipe.yml";

    private File brewingRecipeDir;
    private File potionsDir;
    private File effectsDir;
    private File furnaceDir;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        plugin.saveDefaultConfig();
        config = plugin.getConfig();

        if (allowCustomBrewingRecipe()) {
            brewingRecipeDir = new File(plugin.getDataFolder(), BREWING_RECIPES_DIR_NAME);
            potionsDir = new File(brewingRecipeDir, CUSTOM_POTIONS_DIR_NAME);
            effectsDir = new File(brewingRecipeDir, CUSTOM_EFFECTS_DIR_NAME);

            if (!brewingRecipeDir.exists()) {
                brewingRecipeDir.mkdirs();
                saveDefaultConfig(new File(brewingRecipeDir, EXAMPLE_BREWING_RECIPES_NAME), EXAMPLE_BREWING_RECIPES_RESOURCE_PATH);
            }

            if (!potionsDir.exists()) {
                potionsDir.mkdirs();
                saveDefaultConfig(new File(potionsDir, EXAMPLE_CUSTOM_POTIONS_NAME), EXAMPLE_CUSTOM_POTIONS_RESOURCE_PATH);
            }

            if (!effectsDir.exists()) {
                effectsDir.mkdirs();
                saveDefaultConfig(new File(effectsDir, EXAMPLE_CUSTOM_EFFECTS_NAME), EXAMPLE_CUSTOM_EFFECTS_RESOURCE_PATH);
            }

            loadBrewingRecipes();
        }

        if (allowCustomFurnaceRecipe()) {
            furnaceDir = new File(plugin.getDataFolder(), FURNACE_RECIPES_DIR_NAME);

            if (!furnaceDir.exists()) {
                furnaceDir.mkdirs();
                saveDefaultConfig(new File(furnaceDir, EXAMPLE_FURNACE_RECIPES_NAME), EXAMPLE_FURNACE_RECIPES_RESOURCE_PATH);
            }

            loadFurnaceRecipes();
        }
    }

    public boolean allowRandomEffect() {
        return config.getBoolean("features.mundaneRandomEffect", true);
    }

    public boolean allowCustomBrewingRecipe() {
        return config.getBoolean("features.customBrewingRecipe", true);
    }

    public boolean allowCustomFurnaceRecipe() {
        return config.getBoolean("features.customFurnaceRecipe", true);
    }

    public boolean allowAnvilMixture() {
        // Todo: qyl27: Anything better?
        return config.getBoolean("features.anvilMixPotion", true);
    }

    public boolean allowBrewingCompat() {
        return config.getBoolean("features.brewingCompat", true);
    }

    public void reload() {
        plugin.reloadConfig();
        config = plugin.getConfig();

        if (allowCustomBrewingRecipe()) {
            loadBrewingRecipes();
        }
        if (allowCustomFurnaceRecipe()) {
            loadFurnaceRecipes();
        }
    }

    /**
     * Used for loading or reloading.
     */
    private void loadBrewingRecipes() {
        if (!brewingRecipeDir.exists() || !brewingRecipeDir.isDirectory()) {
            plugin.getLogger().warning("Brewing recipes dir is not exists! Why?");
            return;
        }

        if (!effectsDir.exists() || !effectsDir.isDirectory()) {
            plugin.getLogger().warning("Effects dir is not exists! Why?");
            return;
        }

        if (!potionsDir.exists() || !potionsDir.isDirectory()) {
            plugin.getLogger().warning("Potions dir is not exists! Why?");
            return;
        }

        RecipeManager.getInstance().getBrewingManager().clear();

        for (var file : effectsDir.listFiles()) {
            if (!file.isDirectory()) {
                var effects = load(file);
                if (effects.getBoolean("enabled", true)) {
                    // qyl27: So, Bukkit, **** you! Why not to use the generic parameter?
                    var list = effects.getList("effects", List.of());
                    for (var effect : list) {
                        if (effect instanceof EffectEntry entry) {
                            RecipeManager.getInstance().getBrewingManager().addEffect(entry);
                        }
                    }
                }
            }
        }

        for (var file : potionsDir.listFiles()) {
            if (!file.isDirectory()) {
                var potions = load(file);
                if (potions.getBoolean("enabled", true)) {
                    var list = potions.getList("potions", List.of());
                    for (var potion : list) {
                        if (potion instanceof PotionEntry entry) {
                            RecipeManager.getInstance().getBrewingManager().addPotion(entry);
                        }
                    }
                }
            }
        }

        for (var file : brewingRecipeDir.listFiles()) {
            if (!file.isDirectory()) {
                var recipes = load(file);
                if (recipes.getBoolean("enabled", true)) {
                    var list = recipes.getList("recipes", List.of());
                    for (var recipe : list) {
                        if (recipe instanceof RecipeEntry entry) {
                            RecipeManager.getInstance().getBrewingManager().addRecipe(entry);
                        }
                    }
                }
            }
        }
    }

    private void loadFurnaceRecipes() {
        if (!furnaceDir.exists() || !furnaceDir.isDirectory()) {
            plugin.getLogger().warning("Furnace recipes dir is not exists, why?");
            return;
        }

        RecipeManager.getInstance().getFurnaceManager().clear();

        for (var file : furnaceDir.listFiles()) {
            if (!file.isDirectory()) {
                var recipes = load(file);
                if (recipes.getBoolean("enabled", true)) {
                    var list = recipes.getList("recipes", List.of());
                    for (var recipe : list) {
                        if (recipe instanceof FurnaceEntry entry) {
                            RecipeManager.getInstance().getFurnaceManager().registry(entry);
                        }
                    }
                }
            }
        }
    }

    /**
     * Release a config file.
     * Do nothing when file is already exists.
     * @param file Destination.
     * @param resourcePath Source path.
     */
    private void saveDefaultConfig(@Nonnull File file, @Nonnull String resourcePath) {
        if (!file.exists()) {
            plugin.saveResource(resourcePath, false);
        }
    }

    private YamlConfiguration loadOrDefault(@Nonnull File file, @Nonnull String alternativeResourcePath) {
        saveDefaultConfig(file, alternativeResourcePath);
        return YamlConfiguration.loadConfiguration(file);
    }

    private YamlConfiguration load(@Nonnull File file) {
        return YamlConfiguration.loadConfiguration(file);
    }
}
