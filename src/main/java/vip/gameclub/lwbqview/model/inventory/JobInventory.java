package vip.gameclub.lwbqview.model.inventory;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
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
import vip.gameclub.lwbqview.util.JobUtil;
import vip.gameclub.lwlib.model.inventory.BaseInventory;
import vip.gameclub.lwlib.service.utils.BasePlayerUtil;
import vip.gameclub.lwlib.service.utils.BaseStringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author LW-MrWU
 * @date 创建时间 2021/2/3 17:40
 */
public class JobInventory extends BaseInventory {
    private static JobInventory jobInventory;

    public static JobInventory getInstance(Player player){
        jobInventory = new JobInventory(player);
        return jobInventory;
    }

    public JobInventory(Player player) {
        super(MainPlugin.getInstance(), player, BaseStringUtil.chatColorCodes(LanguageEnum.JOB_TITLE.getValue()));
    }

    @Override
    public List<?> getResourceList() {
        final PlayerData playerData = BetonQuest.getInstance().getPlayerData(PlayerConverter.getID(getPlayer()));
        final Journal journal = playerData.getJournal();

        List<Pointer> pointerList = journal.getPointers();

        return pointerList;
    }

    @Override
    public void handleItem(Inventory inventory, Object object) {
        Pointer pointer = (Pointer) object;
        String text = JobUtil.getPointerText(pointer);
        if(StringUtils.isEmpty(text)){
            return;
        }

        // 实例化物品对象
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(JobUtil.getJobName(pointer));

        List<String> list = new ArrayList<>();
        if(text.startsWith("[") && text.endsWith("]")){
            for (String str : text.substring(1,text.length()-1).split(",")){
                str = str.trim();
                str = JobUtil.replaceVariable(getPlayer(), str);
                list.add(str);
            }
        }else{
            list.add(text);
        }
        itemMeta.setLore(list);
        itemStack.setItemMeta(itemMeta);
        inventory.addItem(itemStack);
    }

    @Override
    public boolean headCustom() {
        return false;
    }

    @Override
    public boolean footCustom() {
        return false;
    }

    @Override
    public boolean invClickCustomListener(InventoryClickEvent event) {
        if(event.getWhoClicked() instanceof Player == false) { return false;}
        Player player = (Player)event.getWhoClicked();
        InventoryView inventoryView = event.getView();
        String title = inventoryView.getTitle();
        if(StringUtils.isNotEmpty(title) && BaseStringUtil.chatColorCodes(LanguageEnum.JOB_TITLE.getValue()).equalsIgnoreCase(title)){
            ItemStack itemStack = event.getCurrentItem();
            if(itemStack == null){
                return false;
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            String jobName = itemMeta.getDisplayName();
            String playerID = BasePlayerUtil.getID(player);
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
                return false;
            }

            JobScoreboard jobScoreboard = JobScoreboard.getInstance(player, LanguageEnum.JOB_TITLE.getValue());
            if(event.isLeftClick()){
                //左键跟踪任务
                jobScoreboard.addJob(pointer);
                event.setCancelled(true);
            }else if(event.isRightClick()){
                //右键取消跟踪任务
                jobScoreboard.delJob(pointer);
                event.setCancelled(true);
            }
        }
        return false;
    }
}
