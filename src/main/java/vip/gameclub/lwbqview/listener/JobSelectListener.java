package vip.gameclub.lwbqview.listener;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.Journal;
import pl.betoncraft.betonquest.Pointer;
import pl.betoncraft.betonquest.database.PlayerData;
import pl.betoncraft.betonquest.utils.PlayerConverter;
import vip.gameclub.lwbqview.MainPlugin;
import vip.gameclub.lwbqview.model.enumModel.LanguageEnum;
import vip.gameclub.lwbqview.model.scoreboard.JobScoreboard;
import vip.gameclub.lwbqview.service.JobService;
import vip.gameclub.lwlib.listener.BaseListener;

import java.util.List;

/**
 * TODO
 *
 * @author LW-MrWU
 * @date 创建时间 2021/1/31 14:59
 */
public class JobSelectListener extends BaseListener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent event){

    }

    @EventHandler
    public void jobInvClick(InventoryClickEvent event){
        if(event.getWhoClicked() instanceof Player == false) { return;}
        Player player = (Player)event.getWhoClicked();
        InventoryView inventoryView = event.getView();
        String title = inventoryView.getTitle();
        if(StringUtils.isNotEmpty(title) && MainPlugin.getInstance().getBaseStringService().chatColorCodes(LanguageEnum.JOB_TITLE.getValue()).equalsIgnoreCase(title)){
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

            JobScoreboard jobScoreboard = JobScoreboard.getInstance(player, LanguageEnum.JOB_TITLE.getValue());
            if(event.isLeftClick()){
                //左键跟踪任务
                String text = JobService.getInstance().getPointerText(pointer);
                String[] strs = text.split("\n");
                jobScoreboard.addTeam(jobName, strs);
            }else if(event.isRightClick()){
                //右键取消跟踪任务
                jobScoreboard.delTeam(jobName);
            }
            event.setCancelled(true);
        }
    }
}
