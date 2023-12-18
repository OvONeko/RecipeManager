package cx.rain.mc.morepotions.compat.mcmmo;

import com.gmail.nossr50.datatypes.skills.alchemy.PotionStage;
import com.gmail.nossr50.util.player.UserManager;
import cx.rain.mc.morepotions.api.event.MorePotionBrewEndEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class McMMOCompatListener implements Listener {
    @EventHandler
    public void onBrewEnd(MorePotionBrewEndEvent event) {
         var player = event.getPlayer();

        for (var ignored : event.getResultsMap().entrySet()) {
            if (UserManager.hasPlayerDataKey(player)) {
                UserManager.getPlayer(player)
                        .getAlchemyManager()
                        .handlePotionBrewSuccesses(PotionStage.TWO, 1);
            }
        }
    }
}
