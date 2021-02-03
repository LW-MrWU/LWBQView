package vip.gameclub.lwbqview.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.Journal;
import pl.betoncraft.betonquest.Pointer;
import pl.betoncraft.betonquest.database.PlayerData;
import pl.betoncraft.betonquest.utils.PlayerConverter;
import vip.gameclub.lwbqview.model.enumModel.LanguageEnum;
import vip.gameclub.lwbqview.util.JobUtil;
import vip.gameclub.lwlib.model.command.BaseCommand;
import vip.gameclub.lwlib.model.enumModel.BaseCommandSenderType;
import vip.gameclub.lwlib.service.plugin.BasePlugin;
import vip.gameclub.lwlib.service.utils.BaseStringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务命令
 *
 * @author LW-MrWU
 * @date 创建时间 2021/2/1 18:07
 */
public class JobCommand extends BaseCommand {
    public JobCommand() {
        super("jobs");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        Player player = (Player)commandSender;

        final PlayerData playerData = BetonQuest.getInstance().getPlayerData(PlayerConverter.getID(player));
        final Journal journal = playerData.getJournal();
        List<Pointer> pointerList = journal.getPointers();

        Inventory inv = Bukkit.createInventory(null, 54, BaseStringUtil.chatColorCodes(LanguageEnum.JOB_TITLE.getValue()));
        for (Pointer pointer : pointerList){
            String text = JobUtil.getPointerText(pointer);
            if(StringUtils.isEmpty(text)){
                continue;
            }

            // 实例化物品对象
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(JobUtil.getJobName(pointer));

            List<String> list = new ArrayList<>();
            if(text.startsWith("[") && text.endsWith("]")){
                for (String str : text.substring(1,text.length()-1).split(",")){
                    str = str.trim();
                    str = JobUtil.replaceVariable(player, str);
                    list.add(str);
                }
            }else{
                list.add(text);
            }
            itemMeta.setLore(list);
            itemStack.setItemMeta(itemMeta);
            inv.addItem(itemStack);
        }

        player.openInventory(inv);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] args) {
        return null;
    }

    @Override
    public BasePlugin getBasePlugin() {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public BaseCommandSenderType getCommandSenderType() {
        return BaseCommandSenderType.PLAYER;
    }

    @Override
    public String getUsageHelp() {
        return null;
    }
}
