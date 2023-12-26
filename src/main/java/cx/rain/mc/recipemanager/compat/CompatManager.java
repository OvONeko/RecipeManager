package cx.rain.mc.recipemanager.compat;

import cx.rain.mc.recipemanager.RecipeManager;
import cx.rain.mc.recipemanager.api.compat.IMorePotionCompat;

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
            if (compat.isFit(RecipeManager.getInstance())) {
                compatList.add(compat);
            }
        }
    }

    public void register() {
        for (var compat : compatList) {
            compat.registerCompat(RecipeManager.getInstance());
        }
    }

    public void unregister() {
        for (var compat : compatList) {
            compat.unregisterCompat(RecipeManager.getInstance());
        }
    }
}
