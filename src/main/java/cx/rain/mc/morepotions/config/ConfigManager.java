package cx.rain.mc.morepotions.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
    private final FileConfiguration config;

    public ConfigManager(Plugin plugin) {
        plugin.saveDefaultConfig();

        config = plugin.getConfig();
    }

    public boolean Enabled() {
        return config.getBoolean("enabled", true);
    }

    public boolean AllowRandomEffect() {
        return config.getBoolean("allowRandomEffect", true);
    }
}
