package cx.rain.mc.morepotions.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.*;

public class DrinkPotionListener implements Listener {

    private final Plugin plugin;

    public DrinkPotionListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnDrinkThinkPotion(PlayerItemConsumeEvent event) {
        if (event.isCancelled())
            return;
        if (!event.getItem().getType().equals(Material.POTION))
            return;
        PotionType type = ((PotionMeta)event.getItem().getItemMeta()).getBasePotionData().getType();
        if (!type.equals(PotionType.THICK))
            return;
        event.getPlayer().addPotionEffect(getRandomEffect());
    }

    public PotionEffect getRandomEffect() {
        PotionEffectType type = PotionEffectType.getById((int)(Math.random() * 33) + 1);
        int duation = (int)(Math.random() * 19000) + 20;
        int amplifier = (int)(Math.random() * 6);
        return new PotionEffect(type, duation, amplifier, false, true, true);
    }
}
