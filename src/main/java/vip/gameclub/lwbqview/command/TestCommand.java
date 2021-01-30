package vip.gameclub.lwbqview.command;

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
import pl.betoncraft.betonquest.config.Config;
import pl.betoncraft.betonquest.config.ConfigPackage;
import pl.betoncraft.betonquest.database.PlayerData;
import pl.betoncraft.betonquest.utils.LogUtils;
import pl.betoncraft.betonquest.utils.PlayerConverter;
import vip.gameclub.lwlib.model.command.BaseCommand;
import vip.gameclub.lwlib.model.enumModel.BaseCommandSenderType;
import vip.gameclub.lwlib.service.plugin.BasePlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

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
        Inventory inv = Bukkit.createInventory(null, 54, "任务列表");

        final PlayerData playerData = BetonQuest.getInstance().getPlayerData(PlayerConverter.getID(player));
        final Journal journal = playerData.getJournal();
        List<Pointer> pointerList = journal.getPointers();
        for (Pointer pointer : pointerList){
            //String pointerStr = pointer.getPointer();
            //System.out.println("pointerStr:"+pointerStr);

            // get package and name of the pointer
            final String[] parts = pointer.getPointer().split("\\.");
            final String packName = parts[0];
            final ConfigPackage pack = Config.getPackages().get(packName);
            if (pack == null) {
                continue;
            }
            final String pointerName = parts[1];
            String jobName = pointerName.split("_")[0];
            // resolve the text in player's language
            String text;
            if (pack.getJournal().getConfig().contains(pointerName)) {
                text = pack.getFormattedString("journal." + pointerName);
            } else {
                LogUtils.getLogger().log(Level.WARNING, "No defined journal entry " + pointerName + " in package " + pack.getName());
                text = "error";
            }
            System.out.println("text:"+text);
            // 实例化物品对象
            ItemStack itemStack = new ItemStack(Material.PAPER);
            List<String> list = new ArrayList<>();
            list.add(text);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(jobName);
            itemMeta.setLore(list);
            itemMeta.setLocalizedName("???????");
            itemStack.setItemMeta(itemMeta);
            inv.addItem(itemStack);
        }

        // 往其添加物品, 如果想指定添加的格子可以使用方法
        // setItem(int index, ItemStack item)
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
