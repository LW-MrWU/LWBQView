package vip.gameclub.lwbqview.listener.bq;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerFishEvent;
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event){
        JobUtil.reloadJobScoreboard(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event){
        JobUtil.reloadJobScoreboard(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCrafting(CraftItemEvent event) {
        //TODO 有bug,暂不启用
        Player player = (Player) event.getWhoClicked();
        //JobUtil.reloadJobScoreboard(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSmelting(FurnaceExtractEvent event) {
        JobUtil.reloadJobScoreboard(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTaming(EntityTameEvent event) {
        JobUtil.reloadJobScoreboard((Player) event.getOwner());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPickup(EntityPickupItemEvent event) {
        if(event.getEntity() instanceof Player){
            JobUtil.reloadJobScoreboard((Player) event.getEntity());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFishCatch(PlayerFishEvent event) {
        JobUtil.reloadJobScoreboard(event.getPlayer());
    }

}
