package cx.rain.mc.recipemanager.stonecutting;

import cx.rain.mc.recipemanager.api.data.StoneCuttingEntry;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.StonecuttingRecipe;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class StoneCuttingManager {
    private final Plugin plugin;

    public StoneCuttingManager(Plugin plugin) {
        this.plugin = plugin;
    }

    private List<NamespacedKey> stoneCuttingRecipeList = new ArrayList<>();

    public void registry(StoneCuttingEntry entry) {
        Bukkit.addRecipe(new StonecuttingRecipe(entry.getId(), new ItemStack(entry.getResult(), entry.getAmount()), entry.getFrom()));
        stoneCuttingRecipeList.add(entry.getId());
    }

    public void clear() {
        if (!stoneCuttingRecipeList.isEmpty()) {
            for (var v : stoneCuttingRecipeList) {
                Bukkit.removeRecipe(v);
            }
        }
        stoneCuttingRecipeList.clear();
    }
}
