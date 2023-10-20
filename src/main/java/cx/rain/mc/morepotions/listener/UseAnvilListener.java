package cx.rain.mc.morepotions.listener;


import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;

public class UseAnvilListener implements Listener {

    private final Plugin plugin;

    public UseAnvilListener(Plugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    void OnClickAnvilInventory(InventoryClickEvent event) {
        if (event.isCancelled())
            return;
        Inventory inventory = event.getInventory();
        if (!inventory.getType().equals(InventoryType.ANVIL))
            return;
        ItemStack[] items = new ItemStack[3];
        items[0] = inventory.getItem(0);
        items[1] = inventory.getItem(1);
        items[2] = new ItemStack(Material.POTION);
        if ((items[0] == null) || (items[1] == null))
            return;
        if (!((items[0].getType().equals(Material.POTION)) && (items[1].getType().equals(Material.POTION))))
            return;
        ItemMeta[] metas = new ItemMeta[3];
        metas[0] = items[0].getItemMeta();
        metas[1] = items[1].getItemMeta();
        if (!(metas[0] instanceof PotionMeta))
            return;
        if (!(metas[1] instanceof PotionMeta))
            return;
        PotionMeta[] potionMetas = new PotionMeta[3];
        potionMetas[0] = (PotionMeta) metas[0];
        potionMetas[1] = (PotionMeta) metas[1];
        ArrayList<PotionEffect> baseEffect = new ArrayList<PotionEffect>(potionMetas[0].getCustomEffects());
        ArrayList<PotionEffect> additionalEffect = new ArrayList<PotionEffect>(potionMetas[1].getCustomEffects());
        ArrayList<PotionEffect> targetEffect = new ArrayList<>();
        targetEffect.addAll(baseEffect);
        targetEffect.addAll(additionalEffect);
        for (PotionEffect v : targetEffect) {
            potionMetas[2].addCustomEffect(v, true);
        }
        potionMetas[2].setDisplayName("Mixed Potion");
        items[2].setItemMeta(potionMetas[2]);
        inventory.setItem(2, items[2]);
    }
}
