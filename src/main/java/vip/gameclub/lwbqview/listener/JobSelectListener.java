package vip.gameclub.lwbqview.listener;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.*;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.Journal;
import pl.betoncraft.betonquest.Pointer;
import pl.betoncraft.betonquest.config.Config;
import pl.betoncraft.betonquest.config.ConfigPackage;
import pl.betoncraft.betonquest.database.PlayerData;
import pl.betoncraft.betonquest.utils.LogUtils;
import pl.betoncraft.betonquest.utils.PlayerConverter;
import vip.gameclub.lwbqview.MainPlugin;
import vip.gameclub.lwbqview.service.JobService;
import vip.gameclub.lwlib.listener.BaseListener;

import java.util.List;
import java.util.logging.Level;

/**
 * TODO
 *
 * @author LW-MrWU
 * @date 创建时间 2021/1/31 14:59
 */
public class JobSelectListener extends BaseListener {

    @EventHandler
    public void jobInvClick(InventoryClickEvent event){
        if(event.getWhoClicked() instanceof Player == false) { return;}
        Player player = (Player)event.getWhoClicked();
        InventoryView inventoryView = event.getView();
        String title = inventoryView.getTitle();
        if(StringUtils.isNotEmpty(title) && "任务列表".equalsIgnoreCase(title)){
            ItemStack itemStack = event.getCurrentItem();
            if(itemStack == null){
                return;
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            String jobName = itemMeta.getDisplayName();
            String playerID = MainPlugin.getInstance().getBasePlayerService().getID(player);
            final PlayerData playerData = PlayerConverter.getPlayer(playerID) == null ? new PlayerData(playerID) : BetonQuest.getInstance().getPlayerData(playerID);
            final Journal journal = playerData.getJournal();

            List<Pointer> pointerList = journal.getPointers();
            Pointer pointer = null;
            for (Pointer p : pointerList){
                String pointerName = p.getPointer();
                String jobNameOfPointerName = pointerName.substring(pointerName.indexOf(".")+1, pointerName.indexOf("_"));
                if(jobName.equalsIgnoreCase(jobNameOfPointerName)){
                    pointer = p;
                    break;
                }
            }

            if(pointer == null){
                return;
            }

            if(event.isLeftClick()){
                //左键跟踪任务
                //判断是否已有跟踪任务
                Scoreboard scoreboard = player.getScoreboard();
                if(scoreboard == null){
                    System.out.println("a");
                }
                System.out.println("b");

                Objective objective = scoreboard.registerNewObjective("test", "dummy", "§a----测试----");

                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                String text = JobService.getInstance().getPointerText(pointer);
                String[] strs = text.split("\n");
                objective.getScore("------"+jobName+"------").setScore(strs.length);
                for (int i = strs.length-1; i>=0; i--){
                    objective.getScore(strs[i]).setScore(i);
                }
                player.setScoreboard(scoreboard);
            }else if(event.isRightClick()){
                //右键取消跟踪任务
            }
            event.setCancelled(true);
            System.out.println("d");
        }
    }
}
