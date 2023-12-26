package cx.rain.mc.recipemanager.listener;

import cx.rain.mc.recipemanager.RecipeManager;
import cx.rain.mc.recipemanager.brewing.BrewingTicker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.Inventory;

public class BrewingRecipeListener implements Listener {
    @EventHandler
    public void onPlaceIngredient(InventoryClickEvent event) {
        if (!RecipeManager.getInstance().getConfigManager().allowCustomBrewingRecipe()) {
            return;
        }

        var inv = event.getClickedInventory();

        if (inv == null || inv.getType() != InventoryType.BREWING) {
            return;
        }

        if (event.isLeftClick()) {
            var slotItem = event.getCurrentItem();
            var cursorItem = event.getCursor();
            if (cursorItem == null || cursorItem.getType() == Material.AIR) {
                return;
            }

            if (slotItem != null && slotItem.getType() != Material.AIR) {
                return;
            }

            Bukkit.getScheduler().runTask(RecipeManager.getInstance(), () -> {
                var nextCursor = event.getWhoClicked().getItemOnCursor();
                var nextSlot = event.getClickedInventory().getItem(event.getSlot());

                event.setCursor(slotItem);  // qyl27: I have not found a better way.

                if (nextCursor == null || nextCursor.getType() == Material.AIR) {
                    return;
                }
                if (nextSlot != null && nextSlot.getType() != Material.AIR) {
                    return;
                }

                inv.setItem(event.getSlot(), cursorItem.clone());

                if (event.getWhoClicked() instanceof Player player) {
                    start(inv, player);
                } else {
                    start(inv, null);
                }
            });

            var clicker = event.getWhoClicked();
            if (clicker instanceof Player player) {
                player.updateInventory();
            }
        }
    }

    private void start(Inventory inv, Player player) {
        if (inv instanceof BrewerInventory brewer) {
            if (brewer.getIngredient() == null) {
                return;
            }

            if (brewer.getHolder().getFuelLevel() <= 0) {
                return;
            }

            var recipe = RecipeManager.getInstance().getBrewingManager().getRecipe(brewer);
            if (recipe != null) {
                new BrewingTicker(recipe, brewer, player).start();
            }
        }
    }

    @EventHandler
    public void onStartBrew(InventoryClickEvent event) {
        if (!RecipeManager.getInstance().getConfigManager().allowCustomBrewingRecipe()) {
            return;
        }

        var inv = event.getClickedInventory();

        if (inv == null || inv.getType() != InventoryType.BREWING) {
            return;
        }

        Bukkit.getScheduler().runTask(RecipeManager.getInstance(), () -> {
            var clicker = event.getWhoClicked();
            if (clicker instanceof Player player) {
                start(inv, player);
            } else {
                start(inv, null);
            }
        });
    }
}
