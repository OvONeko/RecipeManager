package cx.rain.mc.recipemanager.listener;

import cx.rain.mc.recipemanager.RecipeManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;

import java.util.Random;

public class DrinkPotionListener implements Listener {
    private static final Random RAND = new Random();

    @EventHandler
    public void OnDrinkThinkPotion(PlayerItemConsumeEvent event) {
        if (!RecipeManager.getInstance().getConfigManager().allowRandomEffect()) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }
        if (!event.getItem().getType().equals(Material.POTION)) {
            return;
        }
        PotionType type = ((PotionMeta)event.getItem().getItemMeta()).getBasePotionData().getType();
        if (!type.equals(PotionType.MUNDANE)) {
            return;
        }
        event.getPlayer().addPotionEffect(getRandomEffect());
    }

    private PotionEffect getRandomEffect() {
        PotionEffectType type = PotionEffectType.getById(RAND.nextInt(1, PotionEffectType.values().length + 2));    // Bukkit, you have skipped the element with index 0.
        int duration = RAND.nextInt(5, 87) * 20;
        int amplifier = RAND.nextInt(0, 4);
        return new PotionEffect(type, duration, amplifier, false, true, true);
    }
}
