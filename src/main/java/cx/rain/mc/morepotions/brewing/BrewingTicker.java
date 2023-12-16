package cx.rain.mc.morepotions.brewing;

import cx.rain.mc.morepotions.MorePotions;
import cx.rain.mc.morepotions.brewing.config.RecipeEntry;
import cx.rain.mc.morepotions.utility.PotionItemStackHelper;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class BrewingTicker extends BukkitRunnable {
    private static final List<BrewingTicker> INSTANCES = new ArrayList<>();

    private synchronized static void add(BrewingTicker ticker) {
        INSTANCES.add(ticker);
    }

    private synchronized static void purge() {
        INSTANCES.removeIf(BukkitRunnable::isCancelled);
    }

    public synchronized static void stopAll() {
        for (var ticker : INSTANCES) {
            if (!ticker.isCancelled()) {
                ticker.cancel();
            }
        }

        purge();
    }

    private static final int INITIAL_REMAIN_TIME = 400; // Todo: qyl27: Custom brewing length?

    private final RecipeEntry recipe;
    private final Material ingredient;
    private final BrewerInventory inventory;
    private final BrewingStand block;

    private int remainTime = INITIAL_REMAIN_TIME;

    public BrewingTicker(RecipeEntry recipe, BrewerInventory inventory) {
        this.recipe = recipe;
        this.ingredient = recipe.getIngredient();
        this.inventory = inventory;
        this.block = inventory.getHolder();
    }

    @Override
    public void run() {
        if (block.getLocation().getBlock().getType() != Material.BREWING_STAND) {
            stop();
            return;
        }

        if (inventory.getIngredient() == null) {
            stop();
            return;
        }

        if (!ingredient.equals(inventory.getIngredient().getType())) {
            stop();
            return;
        }

        var canContinue = false;
        for (var i = 0; i < 3; i++) {
            var item = inventory.getItem(i);
            if (recipe.getBasePotion().equals(PotionItemStackHelper.getPotionId(item, recipe.getBaseCategory()))) {
                canContinue = true;
                break;
            }
        }
        if (!canContinue) {
            stop();
            return;
        }

        if (remainTime <= 0) {
            block.setFuelLevel(block.getFuelLevel() - 1);

            inventory.getIngredient().setAmount(inventory.getIngredient().getAmount() - 1);

            for (var i = 0; i < 3; i++) {
                var item = inventory.getItem(i);
                if (recipe.getBasePotion().equals(PotionItemStackHelper.getPotionId(item, recipe.getBaseCategory()))) {
                    inventory.setItem(i, PotionItemStackHelper.getPotionStack(recipe.getType(), recipe.getResultId(),
                            recipe.getResultCategory()));
                }
            }

            stop();
            return;
        }

        remainTime -= 1;
        block.setBrewingTime(remainTime);
        block.update();

        var viewers = inventory.getViewers();
        for (var viewer : viewers) {
            if (viewer instanceof Player player) {
                player.updateInventory();
            }
        }
    }

    public void start() {
        // qyl27: Don't optimize too early.
//        runTaskTimerAsynchronously(MorePotions.getInstance(), 0, 1);
        add(this);
        runTaskTimer(MorePotions.getInstance(), 0, 1);
    }

    public void stop() {
        block.setBrewingTime(INITIAL_REMAIN_TIME);
        cancel();
        purge();
    }
}
