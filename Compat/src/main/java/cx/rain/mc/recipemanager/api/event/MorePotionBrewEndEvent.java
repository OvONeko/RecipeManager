package cx.rain.mc.recipemanager.api.event;

import cx.rain.mc.recipemanager.api.data.RecipeEntry;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class MorePotionBrewEndEvent extends BrewEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Map<Integer, ItemStack> results;
    private final RecipeEntry recipe;
    private final Player player;

    public MorePotionBrewEndEvent(@Nonnull Block block, @Nonnull Map<Integer, ItemStack> results, int fuelLevel,
                                  @Nonnull BrewerInventory inventory, @Nonnull RecipeEntry recipe,
                                  @Nullable Player player) {
        super(block, inventory, results.values().stream().toList(), fuelLevel);
        this.results = results;
        this.recipe = recipe;
        this.player = player;
    }

    public Map<Integer, ItemStack> getResultsMap() {
        return results;
    }

    public RecipeEntry getRecipe() {
        return recipe;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public @Nonnull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @Nonnull HandlerList getHandlerList() {
        return HANDLERS;
    }
}
