package vip.gameclub.lwbqview.listener.bq;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import pl.betoncraft.betonquest.api.MobKillNotifier;
import vip.gameclub.lwbqview.util.JobUtil;
import vip.gameclub.lwlib.listener.BaseListener;

/**
 * bq事件监听
 *
 * @author LW-MrWU
 * @date 创建时间 2021/2/3 16:23
 */
public class BQObjectiveListener extends BaseListener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMobKill(MobKillNotifier.MobKilledEvent event) {
        JobUtil.reloadJobScoreboard(event.getPlayer());
    }
}
