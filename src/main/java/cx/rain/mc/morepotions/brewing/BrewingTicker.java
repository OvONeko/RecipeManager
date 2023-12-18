package cx.rain.mc.morepotions.brewing;

import cx.rain.mc.morepotions.MorePotions;
import cx.rain.mc.morepotions.api.data.RecipeEntry;
import cx.rain.mc.morepotions.api.event.MorePotionBrewEndEvent;
import cx.rain.mc.morepotions.api.event.MorePotionBrewStartEvent;
import cx.rain.mc.morepotions.utility.PotionItemStackHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
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
    private final Player player;

    private int remainTime = INITIAL_REMAIN_TIME;

    public BrewingTicker(RecipeEntry recipe, BrewerInventory inventory, @Nullable Player player) {
        this.recipe = recipe;
        this.ingredient = recipe.getIngredient();
        this.inventory = inventory;
        this.block = inventory.getHolder();
        this.player = player;
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
            var newFuelLevel = block.getFuelLevel() - 1;

            var map = new HashMap<Integer, ItemStack>(3);
            for (var i = 0; i < 3; i++) {
                var item = inventory.getItem(i);
                if (recipe.getBasePotion().equals(PotionItemStackHelper.getPotionId(item, recipe.getBaseCategory()))) {
                    map.put(i, PotionItemStackHelper.getPotionStack(recipe.getType(), recipe.getResultId(),
                            recipe.getResultCategory()));
                }
            }

            var event = new MorePotionBrewEndEvent(block.getBlock(), map, newFuelLevel, inventory, recipe, player);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                block.setFuelLevel(event.getFuelLevel());
                block.update();

                var newIngredient = inventory.getIngredient().clone();
                newIngredient.setAmount(newIngredient.getAmount() - 1);
                inventory.setIngredient(newIngredient);

                for (var entry : event.getResultsMap().entrySet()) {
                    inventory.setItem(entry.getKey(), entry.getValue());
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
            if (viewer instanceof Player p) {
                p.updateInventory();
            }
        }
    }

    public void start() {
        // qyl27: Don't optimize too early.
        // Fixme: qyl27: I don't know the meaning of source of BrewingStartEvent, it maybe a mistake.
        if (player != null) {
            var event = new MorePotionBrewStartEvent(block.getBlock(), new ItemStack(ingredient), inventory, recipe, player);
            Bukkit.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                add(this);
                runTaskTimer(MorePotions.getInstance(), 0, 1);
            }
        }
    }

    public void stop() {
//        block.setBrewingTime(INITIAL_REMAIN_TIME);
        cancel();
        purge();
    }
}
