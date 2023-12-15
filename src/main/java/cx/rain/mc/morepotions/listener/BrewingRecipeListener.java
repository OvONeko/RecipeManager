package cx.rain.mc.morepotions.listener;

import cx.rain.mc.morepotions.MorePotions;
import cx.rain.mc.morepotions.brewing.BrewingTicker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;

public class BrewingRecipeListener implements Listener {
    @EventHandler
    public void onStartBrew(InventoryClickEvent event) {
        var inv = event.getClickedInventory();

        if (inv == null
                || inv.getType() != InventoryType.BREWING) {
            return;
        }

        if (inv instanceof BrewerInventory brewer) {
            if (brewer.getIngredient() == null) {
                return;
            }

            if (brewer.getHolder().getFuelLevel() <= 0) {
                return;
            }

            var recipe = MorePotions.getInstance().getBrewingManager().getRecipe(brewer);
            if (recipe != null) {
                new BrewingTicker(recipe, brewer).start();
            }
        }
    }
}
