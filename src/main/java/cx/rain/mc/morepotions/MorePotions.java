package cx.rain.mc.morepotions;

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

    }

    @Override
    public void onDisable() {

    }
}
