package cx.rain.mc.recipemanager.furnace;

import cx.rain.mc.recipemanager.api.data.FurnaceEntry;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class FurnaceManager {
    private final Plugin plugin;

    public FurnaceManager(Plugin plugin) {
        this.plugin = plugin;
    }

    private List<NamespacedKey> furnaceRecipeList = new ArrayList<>();
    private List<NamespacedKey> blastRecipeList = new ArrayList<>();

    public void registry(FurnaceEntry entry) {
        Bukkit.addRecipe(new FurnaceRecipe(entry.getId(), entry.getResult(), entry.getFrom(), entry.getExperience(), entry.getTime()));
        furnaceRecipeList.add(entry.getId());
        if (entry.isAllowBlast()) {
            Bukkit.addRecipe(new BlastingRecipe(NamespacedKey.fromString(entry.getId().toString() + "_blast"), entry.getResult(), entry.getFrom(), entry.getExperience(), entry.getTime() / 2));
            blastRecipeList.add(NamespacedKey.fromString(entry.getId().toString() + "_blast"));
        }
    }

    public void clear() {
        if (!furnaceRecipeList.isEmpty()) {
            for (var v : furnaceRecipeList) {
                Bukkit.removeRecipe(v);
            }
        }
        if (!blastRecipeList.isEmpty()) {
            for (var v : blastRecipeList) {
                Bukkit.removeRecipe(v);
            }
        }
        furnaceRecipeList.clear();
        blastRecipeList.clear();
    }
}