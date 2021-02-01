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
import vip.gameclub.lwbqview.MainPlugin;
import vip.gameclub.lwbqview.model.enumModel.LanguageEnum;
import vip.gameclub.lwbqview.service.JobService;
import vip.gameclub.lwlib.model.command.BaseCommand;
import vip.gameclub.lwlib.model.enumModel.BaseCommandSenderType;
import vip.gameclub.lwlib.service.plugin.BasePlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author LW-MrWU
 * @date 创建时间 2021/1/29 14:58
 */
public class TestCommand extends BaseCommand {
    public TestCommand() {
        super("test");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        Player player = (Player)commandSender;

        final PlayerData playerData = BetonQuest.getInstance().getPlayerData(PlayerConverter.getID(player));
        final Journal journal = playerData.getJournal();
        List<Pointer> pointerList = journal.getPointers();

        Inventory inv = Bukkit.createInventory(null, 54, MainPlugin.getInstance().getBaseStringService().chatColorCodes(LanguageEnum.JOB_TITLE.getValue()));
        for (Pointer pointer : pointerList){
            String text = JobService.getInstance().getPointerText(pointer);
            if(StringUtils.isEmpty(text)){
                continue;
            }

            // 实例化物品对象
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(JobService.getInstance().getJobName(pointer));

            List<String> list = new ArrayList<>();
            if(text.contains("\n")){
                for (String str : text.split("\n")){
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
