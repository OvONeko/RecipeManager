package cx.rain.mc.recipemanager.brewing.persistence;

import org.bukkit.NamespacedKey;

public class CustomPotionData {
    private NamespacedKey customPotionId;

    public NamespacedKey getCustomPotionId() {
        return customPotionId;
    }

    public void setCustomPotionId(NamespacedKey customPotionId) {
        this.customPotionId = customPotionId;
    }
}
