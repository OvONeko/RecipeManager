package cx.rain.mc.morepotions.brewing.persistence;

import cx.rain.mc.morepotions.MorePotions;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PotionContainerType implements PersistentDataType<PersistentDataContainer, CustomPotionData> {
    public static final PotionContainerType INSTANCE = new PotionContainerType();
    public static final NamespacedKey KEY_DATA_TYPE = new NamespacedKey(MorePotions.getInstance(), "customPotion");

    public static final NamespacedKey KEY_ID = new NamespacedKey(MorePotions.getInstance(), "potionId");

    @Override
    public @Nonnull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @Nonnull Class<CustomPotionData> getComplexType() {
        return CustomPotionData.class;
    }

    @Override
    public @Nonnull PersistentDataContainer toPrimitive(@Nonnull CustomPotionData complex,
                                                        @Nonnull PersistentDataAdapterContext context) {
        var container = context.newPersistentDataContainer();
        container.set(KEY_ID, STRING, complex.getCustomPotionId().toString());
        return container;
    }

    @Override
    public @Nonnull CustomPotionData fromPrimitive(@Nonnull PersistentDataContainer primitive,
                                                   @Nonnull PersistentDataAdapterContext context) {
        var data = new CustomPotionData();
        var idStr = primitive.get(KEY_ID, STRING);
        data.setCustomPotionId(NamespacedKey.fromString(Objects.requireNonNull(idStr)));
        return data;
    }
}
