package cx.rain.mc.morepotions.compat;

import cx.rain.mc.morepotions.MorePotions;
import cx.rain.mc.morepotions.api.MorePotionsAPI;
import cx.rain.mc.morepotions.api.compat.IMorePotionCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class CompatManager {
    private final List<IMorePotionCompat> compatList = new ArrayList<>();

    public CompatManager() {
    }

    public void load() {
        ServiceLoader<IMorePotionCompat> compatLoader = ServiceLoader.load(IMorePotionCompat.class, this.getClass().getClassLoader());

        compatList.clear();
        for (var compat : compatLoader) {
            if (compat.isFit(MorePotions.getInstance())) {
                compatList.add(compat);
            }
        }
    }

    public void register() {
        for (var compat : compatList) {
            compat.registerCompat(MorePotions.getInstance());
        }
    }

    public void unregister() {
        for (var compat : compatList) {
            compat.unregisterCompat(MorePotions.getInstance());
        }
    }
}
