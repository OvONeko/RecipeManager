package cx.rain.mc.recipemanager.api.event;

import cx.rain.mc.recipemanager.api.data.RecipeEntry;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MorePotionBrewStartEvent extends BrewingStartEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;

    private final BrewerInventory inventory;
    private final RecipeEntry recipe;
    private final Player player;

    public MorePotionBrewStartEvent(@Nonnull Block block, @Nonnull ItemStack ingredient,
                                    @Nonnull BrewerInventory inventory, @Nonnull RecipeEntry recipe,
                                    @Nullable Player player) {
        super(block, ingredient, 400);  // Todo: qyl27: customizable brewingTime.
        this.inventory = inventory;
        this.recipe = recipe;
        this.player = player;
    }

    public BrewerInventory getInventory() {
        return inventory;
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

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
